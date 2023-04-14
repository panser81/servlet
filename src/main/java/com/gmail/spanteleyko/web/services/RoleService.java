package com.gmail.spanteleyko.web.services;

import com.gmail.spanteleyko.web.exceptions.AddUserException;
import com.gmail.spanteleyko.web.models.RoleDTO;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    public List<RoleDTO> get(int userId) throws SQLException;

    public List<RoleDTO> get() throws SQLException;

    public RoleDTO add(RoleDTO roleDTO) throws AddUserException;
}
