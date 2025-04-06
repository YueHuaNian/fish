package com.example.fish.servlet;

import com.example.fish.config.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet("/registerLogin")
public class RegisterLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = Database.getConnection()) {
            // 检查用户名是否已存在
            String checkUserSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUserSql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        response.sendRedirect("index.jsp?error=username_taken");
                        return;
                    }
                }
            }

            // 插入新用户
            String insertUserSql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertUserSql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=registration_failed");
            return;
        }

        response.sendRedirect("index.jsp?message=registration_successful");
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        try (Connection conn = Database.getConnection()) {
            // 检查用户名和密码是否匹配
            String checkUserSql = "SELECT * FROM " + ("admin".equals(userType) ? "admins" : "users") + " WHERE username = ? AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUserSql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // 登录成功，根据用户类型重定向到不同页面
                        if ("admin".equals(userType)) {
                            response.sendRedirect("admin.jsp");
                        } else {
                            response.sendRedirect("user.jsp");
                        }
                        // 登录或注册成功后
                        HttpSession session = request.getSession();
                        session.setAttribute("user", username);
                        session.setAttribute("userType", userType); // 存储用户类型

                        return;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=login_failed");
            return;
        }

        // 登录失败
        response.sendRedirect("index.jsp?error=invalid_credentials");
    }

}
