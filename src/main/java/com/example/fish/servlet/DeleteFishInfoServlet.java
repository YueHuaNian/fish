package com.example.fish.servlet;

import com.example.fish.config.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteFishInfoServlet")
public class DeleteFishInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String latitudeParam = request.getParameter("latitude");
        String longitudeParam = request.getParameter("longitude");
        if (latitudeParam == null || longitudeParam == null) {
            response.getWriter().write("{\"error\": \"Latitude or Longitude is not defined\"}");
            return;
        }

        double latitude = Double.parseDouble(latitudeParam);
        double longitude = Double.parseDouble(longitudeParam);

        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);

            // 删除 fishing_records 表中与 latitude 和 longitude 关联的记录
            String deleteFishingRecordsSQL = "DELETE FROM fishing_records WHERE location_id = " +
                    "(SELECT location_id FROM locations WHERE latitude = ? AND longitude = ?)";
            try (PreparedStatement deleteFishingRecordsStmt = connection.prepareStatement(deleteFishingRecordsSQL)) {
                deleteFishingRecordsStmt.setDouble(1, latitude);
                deleteFishingRecordsStmt.setDouble(2, longitude);
                deleteFishingRecordsStmt.executeUpdate();
            }

            // 删除 locations 表中的位置信息
            String deleteLocationsSQL = "DELETE FROM locations WHERE latitude = ? AND longitude = ?";
            try (PreparedStatement deleteLocationsStmt = connection.prepareStatement(deleteLocationsSQL)) {
                deleteLocationsStmt.setDouble(1, latitude);
                deleteLocationsStmt.setDouble(2, longitude);
                deleteLocationsStmt.executeUpdate();
            }

            connection.commit();

            response.getWriter().write("{\"message\": \"位置信息删除成功\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"删除位置信息时发生数据库错误：" + e.getMessage() + "\"}");
        }
    }
}
