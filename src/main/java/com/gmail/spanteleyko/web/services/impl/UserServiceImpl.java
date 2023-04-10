package com.gmail.spanteleyko.web.services.impl;

import com.gmail.spanteleyko.web.constants.PropertyConstants;
import com.gmail.spanteleyko.web.converters.RoleConverter;
import com.gmail.spanteleyko.web.converters.UserConverter;
import com.gmail.spanteleyko.web.exceptions.AddUserException;
import com.gmail.spanteleyko.web.models.RoleDTO;
import com.gmail.spanteleyko.web.models.UserDTO;
import com.gmail.spanteleyko.web.repositories.ConnectionRepository;
import com.gmail.spanteleyko.web.repositories.UserRepository;
import com.gmail.spanteleyko.web.repositories.impl.ConnectionRepositoryImpl;
import com.gmail.spanteleyko.web.repositories.impl.UserRepositoryImpl;
import com.gmail.spanteleyko.web.repositories.models.User;
import com.gmail.spanteleyko.web.services.RoleService;
import com.gmail.spanteleyko.web.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static UserService userService = null;
    private UserRepository userRepository;
    private RoleService roleService;
    private ConnectionRepository connectionRepository;

    public UserServiceImpl() throws ClassNotFoundException, SQLException {
        userRepository = UserRepositoryImpl.getInstance();
        roleService = RoleServiceImpl.getInstance();
        connectionRepository = ConnectionRepositoryImpl.getInstance();
    }

    public static synchronized UserService getInstance() throws ClassNotFoundException, SQLException {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }

    @Override
    public List<UserDTO> get() throws SQLException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            List<User> users = userRepository.get(connection);
            return UserConverter.convert(users);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public UserDTO get(int id) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            return UserConverter.convert(userRepository.get(connection, id));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public UserDTO get(String username, String password) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            User user = userRepository.get(connection, username, password);

            if (user == null) {
                return null;
            }

            List<RoleDTO> roles = roleService.get(user.getId());

            user.setRoles(RoleConverter.convert(roles));

            return UserConverter.convert(user);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public UserDTO add(UserDTO userDTO) throws AddUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            User updatedRole = null;
            User role = UserConverter.convert(userDTO);
            try {
                updatedRole = userRepository.add(connection, role);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }

            return UserConverter.convert(updatedRole);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new AddUserException(e.getMessage());
        }
    }

    @Override
    public void insertTemporaryUsers(Connection connection) throws AddUserException {

        UserDTO firstUser = new UserDTO();

        firstUser.setUsername("admin-user");
        firstUser.setPassword("123456");

        UserDTO firstInsertedUser = add(firstUser);

        List<RoleDTO> rolesForFirstUser = new ArrayList<>();

        RoleDTO firstRole = roleService.add(getRole(firstInsertedUser.getId(), PropertyConstants.ADMIN_ROLE_NAME, "admin role"));
        RoleDTO secondRole = roleService.add(getRole(firstInsertedUser.getId(), PropertyConstants.USER_ROLE_NAME, "user role"));

        rolesForFirstUser.add(firstRole);
        rolesForFirstUser.add(secondRole);

        firstUser.setRoles(rolesForFirstUser);

        UserDTO secondUser = new UserDTO();
        secondUser.setUsername("user-user");
        secondUser.setPassword("123456");
        UserDTO secondInsertedUser = add(secondUser);

        List<RoleDTO> rolesForSecondUser = new ArrayList<>();

        RoleDTO thirdRole = roleService.add(getRole(secondInsertedUser.getId(), PropertyConstants.USER_ROLE_NAME, "user role"));
        rolesForSecondUser.add(thirdRole);

        secondUser.setRoles(rolesForSecondUser);
    }

    private RoleDTO getRole(Integer id, String name, String description) {
        RoleDTO role = new RoleDTO();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        return role;
    }
}
