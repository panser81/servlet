package com.gmail.spanteleyko.web.repositories.impl;

import com.gmail.spanteleyko.web.constants.UserConstants;
import com.gmail.spanteleyko.web.repositories.UserRepository;
import com.gmail.spanteleyko.web.repositories.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger logger = LogManager.getLogger(UserRepositoryImpl.class);
    private static UserRepository userRepository = null;

    public static synchronized UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl();
        }

        return userRepository;
    }

    @Override
    public List<User> get(Connection connection) {
        List<User> users = new ArrayList<>();
        String sql = """
                select id, username, password, createdBy 
                from "USER";""";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                User user = getUser(rs);

                users.add(user);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return users;
    }

    @Override
    public User get(Connection connection, Long userId) {
        String sql = """
                select u.id, u.username, u.password, u.createdBy 
                from "USER" u 
                where u.id=?""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = getUser(rs);

                    return user;
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public User get(Connection connection, String username, String password) {
        String sql = """
                select u.id, u.username, u.password, u.createdBy
                from "USER" u 
                where u.username=? and u.password=?""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = getUser(rs);

                    return user;
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public User add(Connection connection, User user) throws SQLException {
        String sql = """
                insert into "USER" (username, password, createdBy) 
                values(?, ?, CURRENT_TIMESTAMP) 
                RETURNING id);
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no Id obtained");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return user;
    }


    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        Long id = resultSet.getLong(UserConstants.ID_COLUMN_NAME);
        user.setId(id);

        String username = resultSet.getString(UserConstants.USERNAME_COLUMN_NAME);
        user.setUsername(username);

        String password = resultSet.getString(UserConstants.PASSWORD_COLUMN_NAME);
        user.setPassword(password);

        Date createdBy = resultSet.getDate(UserConstants.CREATED_BY_COLUMN_NAME);
        user.setCreatedBy(createdBy);

        return user;
    }
}
