package com.sculture;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by bilal on 14/10/15.
 */
@WebServlet(name = "MainServlet")
public class MainServlet extends HttpServlet {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/user";
    static final String USER = "root";
    static final String PASS = "123456";
    Connection conn = null;

    @Override
    public void init() throws ServletException {
        super.init();
        // JDBC driver name and database URL

        //  Database credentials
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //STEP 3: Open a connection

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Creating statement...");
        Statement stmt = null;
        try {
            String password = request.getParameter("inputPassword");
            String username = request.getParameter("inputEmail");
            stmt = conn.createStatement();
            String sql;
            sql = "INSERT INTO users (username,password) VALUES (" + username + "," + password + ")";
            stmt.executeUpdate(sql);
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //STEP 2: Register JDBC driver


            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            Statement stmt = conn.createStatement();
            String sql;
            sql = "SELECT username,password FROM users";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            ArrayList<String> username = new ArrayList<String>();
            ArrayList<String> password = new ArrayList<String>();
            while (rs.next()) {
                //Retrieve by column name
                username.add(rs.getString("username"));
                password.add(rs.getString("password"));
                request.setAttribute("username", username);
                request.setAttribute("password", password);
                request.getRequestDispatcher("/user.jsp").forward(request, response);
            }
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
//        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }
}
