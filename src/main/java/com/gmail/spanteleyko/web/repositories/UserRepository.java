package com.gmail.spanteleyko.web.repositories;

import com.gmail.spanteleyko.web.repositories.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    List<User> get(Connection connection);

    User get(Connection connection, int userId);

    User get(Connection connection, String username, String password);

    User add(Connection connection, User role) throws SQLException;
}
