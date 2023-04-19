package com.gmail.spanteleyko.web.filters;

import com.gmail.spanteleyko.web.constants.RoleConstants;
import com.gmail.spanteleyko.web.constants.ViewNameConstants;
import com.gmail.spanteleyko.web.models.RoleDTO;
import com.gmail.spanteleyko.web.models.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class AdminFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AdminFilter.class);

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        UserDTO user = (UserDTO) session.getAttribute(RoleConstants.USER_SESSION_NAME);

        List<RoleDTO> roles = user.getRoles();

        if (!roles.stream().anyMatch(role -> role.getName().equals(RoleConstants.ADMIN_ROLE_NAME))) {
            httpResponse.sendRedirect(ViewNameConstants.LOGIN_PAGE);
            return;
        }

        chain.doFilter(request, httpResponse);
    }
}
