package com.example.fish.servlet;

import com.example.fish.config.Database;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SearchTopFishServlet")
public class SearchTopFishServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String district = request.getParameter("district");

        if (district != null && !district.isEmpty()) {
            List<String> topFishNames = new ArrayList<>();

            try (Connection connection = Database.getConnection()) {
                String sql = "SELECT f.fish_name " +
                        "FROM fishing_records fr " +
                        "JOIN locations l ON fr.location_id = l.location_id " +
                        "JOIN fish f ON fr.fish_id = f.fish_id " +
                        "WHERE l.district = ? " +
                        "GROUP BY f.fish_name " +
                        "ORDER BY COUNT(*) DESC " +
                        "LIMIT 3";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, district);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String fishName = rs.getString("fish_name");
                    topFishNames.add(fishName);
                }

                rs.close();
                stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 将查询结果转换为 JSON 格式并发送回客户端
            Gson gson = new Gson();
            String json = gson.toJson(topFishNames);
            response.getWriter().write(json);
        }
    }
}
