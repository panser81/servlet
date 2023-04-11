package com.gmail.spanteleyko.web.repositories;

import com.gmail.spanteleyko.web.repositories.models.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleRepository {
    List<Role> get(Connection connection, Long userId) throws SQLException;

    List<Role> get(Connection connection) throws SQLException;

    Role add(Connection connection, Role role) throws SQLException;
}
