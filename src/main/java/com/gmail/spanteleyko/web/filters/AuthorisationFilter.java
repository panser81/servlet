package com.gmail.spanteleyko.web.filters;

import com.gmail.spanteleyko.web.constants.RoleConstants;
import com.gmail.spanteleyko.web.constants.ViewNameConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorisationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AuthorisationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (session.getAttribute(RoleConstants.USER_SESSION_NAME) == null) {
            response.sendRedirect(ViewNameConstants.LOGIN_PAGE);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
