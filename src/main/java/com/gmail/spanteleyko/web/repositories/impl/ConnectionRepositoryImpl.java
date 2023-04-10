package com.gmail.spanteleyko.web.repositories.impl;

import com.gmail.spanteleyko.web.repositories.ConnectionRepository;
import com.gmail.spanteleyko.web.repositories.PropertyRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.gmail.spanteleyko.web.constants.PropertyConstants.*;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private static ConnectionRepository connectionRepository;
    private static HikariDataSource dataSource;

    public static ConnectionRepository getInstance() throws ClassNotFoundException {
        if (connectionRepository == null) {
            connectionRepository = new ConnectionRepositoryImpl();

            PropertyRepository propertyRepository = PropertyRepositoryImpl.getInstance();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(propertyRepository.getProperty(DATABASE_URL));
            hikariConfig.setUsername(propertyRepository.getProperty(DATABASE_USERNAME));
            hikariConfig.setPassword(propertyRepository.getProperty(DATABASE_PASSWORD));
            Class.forName(POSTGRESQL_DRIVER);
            dataSource = new HikariDataSource(hikariConfig);
        }

        return connectionRepository;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
