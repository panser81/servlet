package com.gmail.spanteleyko.web.services;

import com.gmail.spanteleyko.web.exceptions.AddUserException;
import com.gmail.spanteleyko.web.models.UserDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public List<UserDTO> get() throws SQLException;

    public UserDTO get(int id);

    UserDTO get(String user, String password);

    public UserDTO add(UserDTO roleDTO) throws AddUserException;

    void insertTemporaryUsers(Connection connection) throws AddUserException;
}
