package com.gmail.spanteleyko.web.repositories;

import java.sql.Connection;

public interface InitializeDbRepository {
    boolean checkAndCreateDbTables(Connection connection);
}
