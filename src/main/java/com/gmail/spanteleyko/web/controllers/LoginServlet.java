package com.gmail.spanteleyko.web.controllers;

import com.gmail.spanteleyko.web.constants.PropertyConstants;
import com.gmail.spanteleyko.web.constants.UserConstants;
import com.gmail.spanteleyko.web.models.RoleDTO;
import com.gmail.spanteleyko.web.models.UserDTO;
import com.gmail.spanteleyko.web.services.UserService;
import com.gmail.spanteleyko.web.services.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LoginServlet.class);
    private static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/login.jsp";
    private UserService userService;

    public LoginServlet() throws ClassNotFoundException, SQLException {
        userService = UserServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(MediaType.TEXT_HTML);

        UserDTO userDTO = getUserDTO(request);

        HttpSession session = request.getSession();
        session.setAttribute(PropertyConstants.USER_SESSION_NAME, null);
        UserDTO user = userService.get(userDTO.getUsername(), userDTO.getPassword());

        if (user == null) {
            response.sendRedirect(PropertyConstants.LOGIN_PAGE);
        } else {
            session.setAttribute(PropertyConstants.USER_SESSION_NAME, user);
        }

        String url = PropertyConstants.ROLES_PAGE;

        List<RoleDTO> roles = user.getRoles();

        if (roles.stream().anyMatch(role -> role.getName().equals(PropertyConstants.USER_ROLE_NAME))) {
            url = PropertyConstants.USERS_PAGE;
        }

        response.sendRedirect(url);
    }

    private UserDTO getUserDTO(HttpServletRequest request) {
        UserDTO roleDTO = new UserDTO();

        String username = request.getParameter(UserConstants.USERNAME_COLUMN_NAME);
        roleDTO.setUsername(username);

        String password = request.getParameter(UserConstants.PASSWORD_COLUMN_NAME);
        roleDTO.setPassword(password);

        return roleDTO;
    }
}
