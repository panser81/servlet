package com.gmail.spanteleyko.web.services.impl;

import com.gmail.spanteleyko.web.converters.RoleConverter;
import com.gmail.spanteleyko.web.exceptions.AddUserException;
import com.gmail.spanteleyko.web.models.RoleDTO;
import com.gmail.spanteleyko.web.repositories.ConnectionRepository;
import com.gmail.spanteleyko.web.repositories.RoleRepository;
import com.gmail.spanteleyko.web.repositories.impl.ConnectionRepositoryImpl;
import com.gmail.spanteleyko.web.repositories.impl.RoleRepositoryImpl;
import com.gmail.spanteleyko.web.repositories.models.Role;
import com.gmail.spanteleyko.web.services.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);
    private static RoleService roleService = null;
    private RoleRepository roleRepository;
    private ConnectionRepository connectionRepository;

    public static synchronized RoleService getInstance() throws ClassNotFoundException, SQLException {
        if (roleService == null) {
            roleService = new RoleServiceImpl();
        }
        return roleService;
    }

    private RoleServiceImpl() throws ClassNotFoundException {
        roleRepository = RoleRepositoryImpl.getInstance();
        connectionRepository = ConnectionRepositoryImpl.getInstance();
    }

    @Override
    public List<RoleDTO> get(Long id) throws SQLException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Role> roles = roleRepository.get(connection, id);
                connection.commit();
                return RoleConverter.convertToDTO(roles);
            } catch (SQLException se) {
                connection.rollback();
                logger.error(se.getMessage(), se);
            }
        } catch (SQLException e) {

            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<RoleDTO> get() throws SQLException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Role> roles = roleRepository.get(connection);
                connection.commit();
                return RoleConverter.convertToDTO(roles);
            } catch (SQLException se) {
                connection.rollback();
                logger.error(se.getMessage(), se);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public RoleDTO add(RoleDTO roleDTO) throws AddUserException {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            Role updatedRole = null;
            Role role = RoleConverter.convert(roleDTO);
            try {
                updatedRole = roleRepository.add(connection, role);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }

            return RoleConverter.convert(updatedRole);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new AddUserException(e.getMessage());
        }
    }
}
