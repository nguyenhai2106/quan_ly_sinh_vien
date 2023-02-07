/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.dao;

import com.doanjava.model.BangDiem;
import com.doanjava.model.GiangDay;
import com.doanjava.model.GiaoVien;
import com.doanjava.model.MonHoc;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDatabaseMetaData;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.doanjava.model.HocVien;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class GiaoVienDao {

    public GiaoVienDao() {
    }

    public ArrayList<GiaoVien> getAllGiaoViens() {
        ArrayList<GiaoVien> giaoViensArrayList = new ArrayList<GiaoVien>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM GIAOVIEN";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    GiaoVien giaoVien = new GiaoVien(rs.getString("MAGV"), rs.getString("HOTEN"), rs.getString("HOCHAM"), rs.getString("HOCVI"), rs.getString("GIOITINH"), rs.getDate("NGSINH"), rs.getDate("NGVL"), rs.getDouble("HESO"), rs.getBigDecimal("MUCLUONG"), rs.getString("MAKHOA"));
                    giaoViensArrayList.add(giaoVien);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return giaoViensArrayList;
    }

    public void addGiaoVien(GiaoVien giaoVien) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "INSERT INTO GIAOVIEN(MAGV, HOTEN, HOCVI, HOCHAM, GIOITINH, NGSINH, NGVL, HESO, MUCLUONG, MAKHOA) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, giaoVien.getMaGiaoVien());
                preparedStatement.setString(2, giaoVien.getHoTenGiaoVien());
                preparedStatement.setString(3, giaoVien.getHocVi());
                preparedStatement.setString(4, giaoVien.getHocHam());
                preparedStatement.setString(5, giaoVien.getGioiTinh());
                preparedStatement.setDate(6, (Date) giaoVien.getNgaySinh());
                preparedStatement.setDate(7, (Date) giaoVien.getNgayVaoLam());
                preparedStatement.setDouble(8, giaoVien.getHeSo());
                preparedStatement.setBigDecimal(9, giaoVien.getMucLuong());
                preparedStatement.setString(10, giaoVien.getMaKhoa());
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateGiaoVien(GiaoVien giaoVien) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "UPDATE GIAOVIEN SET HOTEN = ?, HOCVI = ?, HOCHAM = ?, GIOITINH = ?, NGSINH = ?, NGVL = ?, HESO = ?, MUCLUONG = ?, MAKHOA = ? WHERE MAGV = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);

                preparedStatement.setString(1, giaoVien.getHoTenGiaoVien());
                preparedStatement.setString(2, giaoVien.getHocVi());
                preparedStatement.setString(3, giaoVien.getHocHam());
                preparedStatement.setString(4, giaoVien.getGioiTinh());
                preparedStatement.setDate(5, (Date) giaoVien.getNgaySinh());
                preparedStatement.setDate(6, (Date) giaoVien.getNgayVaoLam());
                preparedStatement.setDouble(7, giaoVien.getHeSo());
                preparedStatement.setBigDecimal(8, giaoVien.getMucLuong());
                preparedStatement.setString(9, giaoVien.getMaKhoa());
                preparedStatement.setString(10, giaoVien.getMaGiaoVien());

                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteGiaoVien(String maGiaoVien) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "DELETE FROM GIAOVIEN WHERE MAGV = ?;";
            String updateKhoaSQL = "UPDATE KHOA SET TRGKHOA = NULL WHERE TRGKHOA = ?;";
            String updateLopSQL = "UPDATE LOP SET MAGVCN = NULL WHERE MAGVCN = ?;";
            String updateGiangDaySQL = "UPDATE GIANGDAY SET MAGV = NULL WHERE MAGV = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                PreparedStatement updateKhoaPreparedStatement = connection.prepareCall(updateKhoaSQL);
                PreparedStatement updateLopPreparedStatement = connection.prepareCall(updateLopSQL);
                PreparedStatement updateGiangDayPreparedStatement = connection.prepareCall(updateGiangDaySQL);
                updateKhoaPreparedStatement.setString(1, maGiaoVien);
                updateLopPreparedStatement.setString(1, maGiaoVien);
                updateGiangDayPreparedStatement.setString(1, maGiaoVien);

                int rsUpdateKhoa = updateKhoaPreparedStatement.executeUpdate();
                int rsUpdateLop = updateLopPreparedStatement.executeUpdate();
                int rsUpdateGiangDay = updateGiangDayPreparedStatement.executeUpdate();

                preparedStatement.setString(1, maGiaoVien);
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs + " " + rsUpdateKhoa + rsUpdateLop + rsUpdateGiangDay);
            } catch (SQLException ex) {
                Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Dữ liệu bảng điểm
    public ArrayList<GiangDay> getGiangDay(String maGiaoVien) {
        ArrayList<GiangDay> giangDaysArrayList = new ArrayList<GiangDay>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT GD.MALOP, GD.MAMH, MH.TENMH, GD.HOCKY, GD.NAM, GD.TUNGAY, GD.DENNGAY FROM GIANGDAY GD, MONHOC MH WHERE GD.MAMH = MH.MAMH AND GD.MAGV = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maGiaoVien);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    GiangDay giangDay = new GiangDay(rs.getString("MALOP"), rs.getString("MAMH"), rs.getString("TENMH"), rs.getInt("HOCKY"), rs.getInt("NAM"), rs.getDate("TUNGAY"), rs.getDate("DENNGAY"));
                    giangDaysArrayList.add(giangDay);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return giangDaysArrayList;
    }
    // Kết thúc dữ liệu bảng điểm

    public ArrayList<GiaoVien> filterByKhoa(String maKhoa) {
        ArrayList<GiaoVien> giaoViensArrayList = new ArrayList<GiaoVien>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM GIAOVIEN WHERE MAKHOA = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maKhoa);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    GiaoVien giaoVien = new GiaoVien(rs.getString("MAGV"), rs.getString("HOTEN"), rs.getString("HOCHAM"), rs.getString("HOCVI"), rs.getString("GIOITINH"), rs.getDate("NGSINH"), rs.getDate("NGVL"), rs.getDouble("HESO"), rs.getBigDecimal("MUCLUONG"), rs.getString("MAKHOA"));
                    giaoViensArrayList.add(giaoVien);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return giaoViensArrayList;
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

    public boolean CheckAddGiaoVien(GiaoVien giaoVien) {
        ArrayList<GiaoVien> giaoViensArrayList = new ArrayList<GiaoVien>();
        giaoViensArrayList = this.getAllGiaoViens();
        for (GiaoVien giaoVienCheck : giaoViensArrayList) {
            if (giaoVienCheck.getMaGiaoVien().equals(giaoVien.getMaGiaoVien())) {
                return false;
            }
        }
        return true;
    }

}
