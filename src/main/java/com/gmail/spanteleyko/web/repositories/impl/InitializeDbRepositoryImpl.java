package com.gmail.spanteleyko.web.repositories.impl;

import com.gmail.spanteleyko.web.repositories.InitializeDbRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class InitializeDbRepositoryImpl implements InitializeDbRepository {
    private static final Logger logger = LogManager.getLogger(InitializeDbRepositoryImpl.class);
    private static InitializeDbRepository initializeDbRepository = null;

    public static InitializeDbRepository getInstance() {
        if (initializeDbRepository == null) {
            initializeDbRepository = new InitializeDbRepositoryImpl();
        }

        return initializeDbRepository;
    }

    public boolean checkAndCreateDbTables(Connection connection) {
        String existsUserTableSql = """
                SELECT COUNT(table_name) 
                FROM information_schema.tables 
                WHERE table_schema LIKE 'public' 
                AND  table_type LIKE 'BASE TABLE' AND table_name = ?;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(existsUserTableSql)) {
            String userTableName = "USER";

            preparedStatement.setString(1, userTableName);

            try (ResultSet existsUserTable = preparedStatement.executeQuery()) {
                boolean hasUserTable = false;

                while (existsUserTable.next()) {
                    hasUserTable = existsUserTable.getInt(1) == 1;
                }

                if (hasUserTable) {
                    deleteDbTables(connection);
                }

                return createDbTables(connection);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private boolean createDbTables(Connection connection) {
        String sql = """
                CREATE TABLE "USER"
                    (id SERIAL PRIMARY KEY,
                    username VARCHAR(40) NOT NULL,
                    password VARCHAR(40) NOT NULL,
                    createdBy TIMESTAMP NOT NULL);
                CREATE TABLE "ROLE" 
                    (user_id INTEGER NOT NULL,
                    name VARCHAR(40) NOT NULL,
                    description VARCHAR(40) NOT NULL);
                ALTER TABLE "ROLE" 
                    ADD CONSTRAINT FK_User_Role FOREIGN KEY(user_id) 
                    REFERENCES "USER" (id);
                """;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.info(sql);
        }

        return false;
    }

    private boolean deleteDbTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {

            String sql = """
                    drop table "ROLE";
                    drop table "USER";
                    """;

            statement.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

}
