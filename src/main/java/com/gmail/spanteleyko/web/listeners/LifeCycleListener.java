package com.gmail.spanteleyko.web.listeners;

import com.gmail.spanteleyko.web.exceptions.AddUserException;
import com.gmail.spanteleyko.web.repositories.ConnectionRepository;
import com.gmail.spanteleyko.web.repositories.InitializeDbRepository;
import com.gmail.spanteleyko.web.repositories.impl.ConnectionRepositoryImpl;
import com.gmail.spanteleyko.web.repositories.impl.InitializeDbRepositoryImpl;
import com.gmail.spanteleyko.web.services.UserService;
import com.gmail.spanteleyko.web.services.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class LifeCycleListener implements ServletContextListener {

    private static InitializeDbRepository initializeDbRepository;
    private UserService userService;
    private ConnectionRepository connectionRepository;

    public LifeCycleListener() throws ClassNotFoundException, SQLException {
        initializeDbRepository = InitializeDbRepositoryImpl.getInstance();
        userService = UserServiceImpl.getInstance();
        connectionRepository = ConnectionRepositoryImpl.getInstance();
    }

    private static final Logger logger = LogManager.getLogger(LifeCycleListener.class);

    public void contextInitialized(ServletContextEvent sce) {

    }

    public void contextDestroyed(ServletContextEvent sce) {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                initializeDbRepository.checkAndCreateDbTables(connection);
                userService.insertTemporaryUsers(connection);
                connection.commit();
            } catch (SQLException | AddUserException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
