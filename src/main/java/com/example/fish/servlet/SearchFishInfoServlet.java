package com.example.fish.servlet;

import com.example.fish.config.Database;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SearchFishInfoServlet")
public class SearchFishInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String fishName = request.getParameter("search_fish_name");
        int kind = Integer.parseInt(request.getParameter("kind"));
        double searchLat = Double.parseDouble(request.getParameter("latitude"));
        double searchLng = Double.parseDouble(request.getParameter("longitude"));
        double bj = kind == 0 ? Double.parseDouble(request.getParameter("bj")) : 10000;

        List<FishInfo> fishList = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {
            String sql;
            PreparedStatement pstmt;
            ResultSet rs;

            sql = "SELECT fish_id FROM fish WHERE fish_name=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, fishName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int fish_id = rs.getInt("fish_id");

                if (kind == 1) {
                    sql = "WITH fish_locations AS (" +
                            "    SELECT loc.latitude, loc.longitude,loc.province,loc.city,loc.district" +
                            "    FROM fishing_records fr" +
                            "    JOIN locations loc ON fr.location_id = loc.location_id" +
                            "    WHERE fr.fish_id = ?" +
                            ")," +
                            "distances AS (" +
                            "    SELECT * ," +
                            "        (6371 * acos(cos(radians(?)) * cos(radians(latitude)) * cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)))) AS distance" +
                            "    FROM fish_locations" +
                            ")" +
                            "SELECT latitude, longitude,province,city,district FROM distances ORDER BY distance LIMIT 1;";
                } else if (kind == 0) {
                    sql = "WITH fish_locations AS (" +
                            "    SELECT loc.latitude, loc.longitude,loc.province,loc.city,loc.district" +
                            "    FROM fishing_records fr" +
                            "    JOIN locations loc ON fr.location_id = loc.location_id" +
                            "    WHERE fr.fish_id = ?" +
                            ")," +
                            "distances AS (" +
                            "    SELECT * ," +
                            "        (6371 * acos(cos(radians(?)) * cos(radians(latitude)) * cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)))) AS distance" +
                            "    FROM fish_locations" +
                            ")" +
                            "SELECT latitude, longitude,province,city,district FROM distances WHERE distance < ? ORDER BY distance;";
                } else {
                    sql =   "WITH fish_locations AS (" +
                            "    SELECT loc.latitude, loc.longitude , loc.province , loc.city , loc.district " +
                            "    FROM fishing_records fr" +
                            "    JOIN locations loc ON fr.location_id = loc.location_id" +
                            "    WHERE fr.fish_id = ?" +
                            ")," +
                            "distances AS (" +
                            "    SELECT * ," +
                            "        (6371 * acos(cos(radians(?)) * cos(radians(latitude)) * cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)))) AS distance" +
                            "    FROM fish_locations" +
                            ")" +
                            "SELECT latitude, longitude,province,city,district FROM distances ORDER BY distance;";
                }

                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, fish_id);
                pstmt.setDouble(2, searchLat);
                pstmt.setDouble(3, searchLng);
                pstmt.setDouble(4, searchLat);
                if (kind == 0) pstmt.setDouble(5, bj);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");
                    String province = rs.getString("province");
                    String city = rs.getString("city");
                    String district = rs.getString("district");
                    fishList.add(new FishInfo(latitude, longitude,province,city,district));
                }
            }

            rs.close();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String json = gson.toJson(fishList);
        response.getWriter().write(json);
    }

    class FishInfo {
        private double latitude;
        private double longitude;
        private String province;
        private String city;
        private String district;

        public FishInfo(double latitude, double longitude,String province,String city,String district) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.province=province;
            this.city=city;
            this.district=district;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
