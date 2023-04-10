package com.gmail.spanteleyko.web.controllers;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RolesServlet", value = "/roles")
public class RolesServlet extends HttpServlet {
    private static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/roles.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
