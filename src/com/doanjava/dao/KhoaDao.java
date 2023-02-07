/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.dao;

import com.doanjava.model.GiaoVien;
import com.doanjava.model.Khoa;
import com.doanjava.model.HocVien;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class KhoaDao {

    public KhoaDao() {
    }

    public ArrayList<Khoa> getAllKhoas() {
        ArrayList<Khoa> khoasArrayList = new ArrayList<Khoa>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT K.MAKHOA, K.TENKHOA, K.NGTLAP, K.TRGKHOA, GV.HOTEN FROM KHOA K, GIAOVIEN GV WHERE K.TRGKHOA = GV.MAGV;";
            String SQL1 = "SELECT TOP 100 * FROM KHOA WHERE TRGKHOA IS NULL;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                ResultSet rs = preparedStatement.executeQuery();
                PreparedStatement preparedStatement1 = connection.prepareStatement(SQL1);
                ResultSet rs1 = preparedStatement1.executeQuery();

                while (rs.next()) {
                    Khoa khoa = new Khoa(rs.getString("MAKHOA"), rs.getString("TENKHOA"), rs.getDate("NGTLAP"), rs.getString("TRGKHOA"), rs.getString("HOTEN"));
                    khoasArrayList.add(khoa);
                }
                while (rs1.next()) {
                    Khoa khoa = new Khoa(rs1.getString("MAKHOA"), rs1.getString("TENKHOA"), rs1.getDate("NGTLAP"), rs1.getString("TRGKHOA"));
                    khoasArrayList.add(khoa);
                }

            } catch (SQLException ex) {
                Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return khoasArrayList;
    }

    public void addKhoa(Khoa khoa) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "INSERT INTO KHOA(MAKHOA, TENKHOA, NGTLAP, TRGKHOA) VALUES(?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);

                preparedStatement.setString(1, khoa.getMaKhoa());
                preparedStatement.setString(2, khoa.getTenKhoa());
                preparedStatement.setDate(3, (Date) khoa.getNgayThanhLap());
                preparedStatement.setString(4, khoa.getTrươngKhoa());
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateKhoa(Khoa khoa) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "UPDATE KHOA SET TENKHOA = ?, NGTLAP = ?, TRGKHOA = ? WHERE MAKHOA = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, khoa.getTenKhoa());
                preparedStatement.setDate(2, (Date) khoa.getNgayThanhLap());
                preparedStatement.setString(3, khoa.getTrươngKhoa());
                preparedStatement.setString(4, khoa.getMaKhoa());
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteKhoa(String maKhoa) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "DELETE FROM KHOA WHERE MAKHOA = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, maKhoa);
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KhoaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GiaoVien searchGiaoVien(String maGiaoVien) {
        GiaoVien giaoVien = null;
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM GIAOVIEN WHERE MAGV = ?";
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maGiaoVien);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    giaoVien = new GiaoVien(rs.getString("MAGV"), rs.getString("HOTEN"), rs.getString("MAKHOA"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return giaoVien;
    }
}
