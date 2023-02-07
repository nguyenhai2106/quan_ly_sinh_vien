/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.dao;

import com.doanjava.model.BangDiem;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDatabaseMetaData;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.doanjava.model.HocVien;
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
public class HocVienDao {

    private ArrayList<HocVien> studentsArrayList = this.getAllStudents();

    public HocVienDao() {
    }

    public ArrayList<HocVien> getAllStudents() {
        ArrayList<HocVien> studentsArrayList = new ArrayList<HocVien>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM HOCVIEN";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    HocVien student = new HocVien(rs.getString("MAHV"), rs.getString("HO"), rs.getString("TEN"), rs.getDate("NGSINH"), rs.getString("GIOITINH"), rs.getString("NOISINH"), rs.getString("MALOP"));
                    studentsArrayList.add(student);
                }
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentsArrayList;
    }

    public void addStudent(HocVien student) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "INSERT INTO HOCVIEN(MAHV, HO, TEN, NGSINH, NOISINH, GIOITINH, MALOP) VALUES(?, ?, ?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, student.getMaHocVien());
                preparedStatement.setString(2, student.getHoHocVien());
                preparedStatement.setString(3, student.getTenHocVien());
                preparedStatement.setDate(4, (Date) student.getNgaySinh());
                preparedStatement.setString(5, student.getNoiSinh());
                preparedStatement.setString(6, student.getGioiTinh());
                preparedStatement.setString(7, student.getMaLop());

                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateStudent(HocVien student) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "UPDATE HOCVIEN SET HO = ?, TEN = ?, NGSINH = ?, NOISINH = ?, GIOITINH = ?, MALOP = ? WHERE MAHV = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, student.getHoHocVien());
                preparedStatement.setString(2, student.getTenHocVien());
                preparedStatement.setDate(3, (Date) student.getNgaySinh());
                preparedStatement.setString(4, student.getNoiSinh());
                preparedStatement.setString(5, student.getGioiTinh());
                preparedStatement.setString(6, student.getMaLop());
                preparedStatement.setString(7, student.getMaHocVien());

                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteStudent(String maHocVien) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "DELETE FROM HOCVIEN WHERE MAHV = ?;";
            String deleteSQL = "DELETE FROM KETQUATHI WHERE MAHV = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSQL);
                deletePreparedStatement.setString(1, maHocVien);
                int rsDelete = deletePreparedStatement.executeUpdate();
                preparedStatement.setString(1, maHocVien);
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs + " " + rsDelete);
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Dữ liệu bảng điểm
    public ArrayList<BangDiem> getBangDiem(String maHocVien) {
        ArrayList<BangDiem> bangDiemsArrayList = new ArrayList<BangDiem>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL1 = "SELECT HV.MAHV, KQ.MAMH, KQ.DIEM INTO BANGPHU FROM HOCVIEN HV, KETQUATHI KQ WHERE HV.MAHV = KQ.MAHV;";
            String SQL2 = "SELECT BP.MAHV, BP.MAMH, MH.TENMH, MH.TCLT, MH.TCTH, BP.DIEM INTO BANGDIEM FROM BANGPHU BP, MONHOC MH WHERE BP.MAMH = MH.MAMH;";
            String SQL3 = "SELECT * FROM BANGDIEM WHERE MAHV = ?;";
            String SQL4 = "DROP TABLE BANGPHU, BANGDIEM;";
            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(SQL1);
                PreparedStatement preparedStatement2 = connection.prepareStatement(SQL2);
                PreparedStatement preparedStatement3 = connection.prepareStatement(SQL3);
                preparedStatement3.setString(1, maHocVien);
                preparedStatement1.executeUpdate();
                preparedStatement2.executeUpdate();
                ResultSet rs3 = preparedStatement3.executeQuery();
                while (rs3.next()) {
                    BangDiem bangDiem = new BangDiem(rs3.getString("MAHV"), rs3.getString("MAMH"), rs3.getString("TENMH"), rs3.getByte("TCLT"), rs3.getByte("TCTH"), rs3.getDouble("DIEM"));
                    bangDiemsArrayList.add(bangDiem);
                }
                PreparedStatement preparedStatement4 = connection.prepareStatement(SQL4);
                preparedStatement4.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bangDiemsArrayList;
    }
    // Kết thúc dữ liệu bảng điểm

    public ArrayList<HocVien> filterByClass(String maLop) {
        ArrayList<HocVien> studentsArrayList = new ArrayList<HocVien>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM HOCVIEN WHERE MALOP = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maLop);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    HocVien student = new HocVien(rs.getString("MAHV"), rs.getString("HO"), rs.getString("TEN"), rs.getDate("NGSINH"), rs.getString("GIOITINH"), rs.getString("NOISINH"), rs.getString("MALOP"));
                    studentsArrayList.add(student);
                }
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentsArrayList;
    }

    public boolean CheckAddSinhVien(HocVien student) {
        ArrayList<HocVien> studentsArrayList = new ArrayList<HocVien>();
        studentsArrayList = this.getAllStudents();
        for (HocVien studentCheck : studentsArrayList) {
            if (studentCheck.getMaHocVien().equals(student.getMaHocVien())) {
                return false;
            }
        }
        return true;
    }
}
