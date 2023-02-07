package com.doanjava.dao;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDatabaseMetaData;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author nguye
 */
public class JDBCConnection {

    private static String connectionUrl = "jdbc:sqlserver://DESKTOP-DUDLOTC\\SQLEXPRESS:1433;databaseName=QLGVJAVA";
    private static String userName = "sa";
    private static String password = "sa";

    public static String getConnectionUrl() {
        return connectionUrl;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setConnectionUrl(String connectionUrl) {
        JDBCConnection.connectionUrl = connectionUrl;
    }

    public static void setPassword(String password) {
        JDBCConnection.password = password;
    }

    public static void setUserName(String userName) {
        JDBCConnection.userName = userName;
    }

    public static Connection getJDBCConnection() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(connectionUrl, userName, password);
            if (conn != null) {
                System.out.println("Connected!");
            }
            return conn;
        } // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/*
public class JDBCConnection {

    public static Connection getJDBCConnection() {
        // Create a variable for the connection string.
        String connectionUrl = "jdbc:sqlserver://DESKTOP-DUDLOTC\\SQLEXPRESS:1433;databaseName=QLGV;user=sa;password=sa";
        try ( Connection con = DriverManager.getConnection(connectionUrl);) {
            System.out.println("Connected!");
            return DriverManager.getConnection(connectionUrl);
        } // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
 */
