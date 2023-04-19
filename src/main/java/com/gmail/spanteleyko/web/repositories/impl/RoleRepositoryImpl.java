package com.gmail.spanteleyko.web.repositories.impl;

import com.gmail.spanteleyko.web.constants.UserConstants;
import com.gmail.spanteleyko.web.repositories.RoleRepository;
import com.gmail.spanteleyko.web.repositories.models.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepositoryImpl implements RoleRepository {
    private static final Logger logger = LogManager.getLogger(RoleRepositoryImpl.class);
    private static RoleRepository roleRepository = null;

    public static synchronized RoleRepository getInstance() {
        if (roleRepository == null) {
            roleRepository = new RoleRepositoryImpl();
        }

        return roleRepository;
    }

    @Override
    public List<Role> get(Connection connection, Long userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = """
                select user_id as id, name, description 
                from "ROLE" where user_id = ?;""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Role role = getRole(rs);

                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return roles;
    }

    @Override
    public List<Role> get(Connection connection) throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = """
                select user_id as id, name, description 
                from "ROLE";""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Role role = getRole(rs);

                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return roles;
    }

    @Override
    public Role add(Connection connection, Role role) throws SQLException {
        String sql = """
                insert into "ROLE" (user_id, name, description) 
                values 
                (?, ?, ?);
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, role.getId());
            preparedStatement.setString(2, role.getName());
            preparedStatement.setString(3, role.getDescription());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return role;
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        Long id = resultSet.getLong(UserConstants.ID_COLUMN_NAME);
        role.setId(id);

        String name = resultSet.getString(UserConstants.NAME_COLUMN_NAME);
        role.setName(name);

        String description = resultSet.getString(UserConstants.DESCRIPTION_COLUMN_NAME);
        role.setDescription(description);

        return role;
    }
}
