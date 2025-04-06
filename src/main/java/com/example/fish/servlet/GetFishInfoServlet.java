package com.example.fish.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.fish.config.Database;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetFishInfoServlet")
public class GetFishInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        List<FishInfo> fishList = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {

            String sql = "SELECT l.location_id, f.fish_name, l.latitude, l.longitude, l.province, l.city, l.district " +
                    "FROM locations l " +
                    "JOIN fishing_records fr ON l.location_id = fr.location_id " +
                    "JOIN fish f ON fr.fish_id = f.fish_id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int locationId = rs.getInt("location_id");
                String fishName = rs.getString("fish_name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                String province = rs.getString("province");
                String city = rs.getString("city");
                String district = rs.getString("district");
                FishInfo fishInfo = new FishInfo(locationId, fishName, latitude, longitude, province, city, district);
                fishList.add(fishInfo);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String json = gson.toJson(fishList);
        response.getWriter().write(json);

    }

    class FishInfo {
        private int locationId;
        private String fishName;
        private double latitude;
        private double longitude;
        private String province;
        private String city;
        private String district;

        public FishInfo(int locationId, String fishName, double latitude, double longitude, String province, String city, String district) {
            this.locationId = locationId;
            this.fishName = fishName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.province = province;
            this.city = city;
            this.district = district;
        }

        // Getters
        public int getLocationId() {
            return locationId;
        }

        public String getFishName() {
            return fishName;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }
    }
}

