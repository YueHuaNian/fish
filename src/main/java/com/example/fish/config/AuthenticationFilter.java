package com.example.fish.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器（如果需要）
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/index.jsp";
        String logoutURI = httpRequest.getContextPath() + "/logout";

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        String userType = (session != null) ? (String) session.getAttribute("userType") : null;
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isLogoutRequest = httpRequest.getRequestURI().equals(logoutURI);

        boolean isAdminPage = httpRequest.getRequestURI().endsWith("admin.jsp");
        boolean isUserPage = httpRequest.getRequestURI().endsWith("user.jsp");

        if (isLogoutRequest) {
            if (session != null) {
                session.invalidate();
            }
            httpResponse.sendRedirect(loginURI);
        } else if (isLoggedIn && isAdminPage && "admin".equals(userType)) {
            chain.doFilter(request, response);
        } else if (isLoggedIn && isUserPage && "user".equals(userType)) {
            chain.doFilter(request, response);
        } else if (isLoggedIn && (isLoginRequest || (!isAdminPage && !isUserPage))) {
            chain.doFilter(request, response);
        } else if (isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }


    @Override
    public void destroy() {
        // 销毁过滤器（如果需要）
    }
}
