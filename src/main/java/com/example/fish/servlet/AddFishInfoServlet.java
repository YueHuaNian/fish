package com.example.fish.servlet;

import com.example.fish.config.Database;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddFishInfoServlet")
public class AddFishInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String fishName = request.getParameter("fish_name");
        String[] Fish=fishName.split(",");
        for(int i=0;i< Fish.length;++i){
            System.out.println(Fish[i]);
        }
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String street = request.getParameter("street");

        try (Connection connection = Database.getConnection()){
            String sql;
            PreparedStatement pstmt = null;
            ResultSet rs=null;
            int fish_id,location_id;

            sql = "INSERT INTO locations(latitude,longitude,province,city,district) VALUES(?,?,?,?,?) RETURNING location_id";
            pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, latitude);
            pstmt.setDouble(2, longitude);
            pstmt.setString(3, province);
            pstmt.setString(4, city);
            pstmt.setString(5, street);
            rs = pstmt.executeQuery();
            rs.next();
            location_id = rs.getInt("location_id");

            for(int i=0;i<Fish.length;++i) {
                sql = "SELECT fish_id FROM fish where fish_name=?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, Fish[i]);
                rs = pstmt.executeQuery();
                if (!rs.next()) {
                    sql = "INSERT INTO fish (fish_name) VALUES (?) RETURNING fish_id";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, Fish[i]);
                    ResultSet RS = pstmt.executeQuery();
                    RS.next();
                    fish_id = RS.getInt("fish_id");
                    RS.close();
                } else {
                    fish_id = rs.getInt("fish_id");
                }

//            System.out.println(fish_id+" "+location_id);
                sql = "INSERT INTO fishing_records(user_id,fish_id,location_id) VALUES (?,?,?)";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, 1);
                pstmt.setInt(2, fish_id);
                pstmt.setInt(3, location_id);
                pstmt.executeUpdate();
            }

            rs.close();
            pstmt.close();
            connection.close();

            response.sendRedirect("user.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            //response.sendRedirect("error.jsp");
            System.out.println("Fuck");
        }
    }
}
