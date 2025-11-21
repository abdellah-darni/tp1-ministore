package com.abdellah.tp1ministore.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        logger.trace(">>> Filter Intercept: {}", requestURI);

        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        boolean isLoginRegisterRequest =  requestURI.endsWith("/login") || requestURI.endsWith("/register");
        boolean isStaticResource = requestURI.contains("/css/");

        if (isLoggedIn || isLoginRegisterRequest || isStaticResource) {
            if(isLoggedIn) logger.trace("Access GRANTED (User Logged In): {}", requestURI);
            else logger.trace("Access GRANTED (Whitelist): {}", requestURI);

            chain.doFilter(request, response);
        } else {
            logger.warn("Access DENIED for Guest at: {}. Redirecting to Login.", requestURI);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
}
