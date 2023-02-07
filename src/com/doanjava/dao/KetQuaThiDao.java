/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.dao;

import com.doanjava.model.BangDiem;
import com.doanjava.model.GiaoVien;
import com.doanjava.model.KetQuaThi;
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
public class KetQuaThiDao {

    public KetQuaThiDao() {
    }

    public ArrayList<KetQuaThi> getAllKetQuaThis() {
        ArrayList<KetQuaThi> ketquaThisArrayList = new ArrayList<KetQuaThi>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT KQ.MAHV, HV.HO, HV.TEN, MH.MAMH, MH.TENMH, KQ.LANTHI, KQ.NGTHI, KQ.DIEM, KQ.KQUA, HV.MALOP FROM KETQUATHI KQ, MONHOC MH, HOCVIEN HV WHERE KQ.MAMH = MH.MAMH AND KQ.MAHV = HV.MAHV;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    KetQuaThi ketQuaThi = new KetQuaThi(rs.getString("MAHV"), rs.getString("HO") + " " + rs.getString("TEN"), rs.getString("MAMH"), rs.getString("TENMH"), rs.getInt("LANTHI"), rs.getDate("NGTHI"), rs.getDouble("DIEM"), rs.getString("KQUA"), rs.getString("MALOP"));
                    ketquaThisArrayList.add(ketQuaThi);
                }
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketquaThisArrayList;
    }

    public void addKetQuaThi(KetQuaThi ketQuaThi) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "INSERT INTO KETQUATHI(MAHV, MAMH, LANTHI, NGTHI, DIEM, KQUA) VALUES(?, ?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, ketQuaThi.getMaHocVien());
                preparedStatement.setString(2, ketQuaThi.getMaMonHoc());
                preparedStatement.setInt(3, ketQuaThi.getLanThi());
                preparedStatement.setDate(4, (Date) ketQuaThi.getNgayThi());
                preparedStatement.setDouble(5, ketQuaThi.getDiemThi());
                preparedStatement.setString(6, ketQuaThi.getKetQua());
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateKetQuaThi(KetQuaThi ketQuaThi) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "UPDATE KETQUATHI SET NGTHI = ?, DIEM = ?, KQUA = ? WHERE MAHV = ? AND MAMH = ? AND LANTHI = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setDate(1, (Date) ketQuaThi.getNgayThi());
                preparedStatement.setDouble(2, ketQuaThi.getDiemThi());
                preparedStatement.setString(3, ketQuaThi.getKetQua());
                preparedStatement.setString(4, ketQuaThi.getMaHocVien());
                preparedStatement.setString(5, ketQuaThi.getMaMonHoc());
                preparedStatement.setInt(6, ketQuaThi.getLanThi());
                int rs = preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteKetQuaThi(String maHocVien, String maMonHoc, int lanThi) {
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "DELETE FROM KETQUATHI WHERE MAHV = ? AND MAMH = ? AND LANTHI = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareCall(SQL);
                preparedStatement.setString(1, maHocVien);
                preparedStatement.setString(2, maMonHoc);
                preparedStatement.setInt(3, lanThi);
                int rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bangDiemsArrayList;
    }
    // Kết thúc dữ liệu bảng điểm

    public ArrayList<KetQuaThi> filterByClass(String maLop) {
        ArrayList<KetQuaThi> ketquaThisArrayList = new ArrayList<KetQuaThi>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT KQ.MAHV, HV.HO, HV.TEN, MH.MAMH, MH.TENMH, KQ.LANTHI, KQ.NGTHI, KQ.DIEM, KQ.KQUA, HV.MALOP FROM KETQUATHI KQ, MONHOC MH, HOCVIEN HV WHERE KQ.MAMH = MH.MAMH AND KQ.MAHV = HV.MAHV AND HV.MALOP = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maLop);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    KetQuaThi ketQuaThi = new KetQuaThi(rs.getString("MAHV"), rs.getString("HO") + " " + rs.getString("TEN"), rs.getString("MAMH"), rs.getString("TENMH"), rs.getInt("LANTHI"), rs.getDate("NGTHI"), rs.getDouble("DIEM"), rs.getString("KQUA"), rs.getString("MALOP"));
                    ketquaThisArrayList.add(ketQuaThi);
                }
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketquaThisArrayList;
    }

    public ArrayList<KetQuaThi> filterByMonHoc(String maMonHoc) {
        ArrayList<KetQuaThi> ketquaThisArrayList = new ArrayList<KetQuaThi>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT KQ.MAHV, HV.HO, HV.TEN, MH.MAMH, MH.TENMH, KQ.LANTHI, KQ.NGTHI, KQ.DIEM, KQ.KQUA, HV.MALOP FROM KETQUATHI KQ, MONHOC MH, HOCVIEN HV WHERE KQ.MAMH = MH.MAMH AND KQ.MAHV = HV.MAHV AND HV.MALOP = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maMonHoc);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    KetQuaThi ketQuaThi = new KetQuaThi(rs.getString("MAHV"), rs.getString("HO") + " " + rs.getString("TEN"), rs.getString("MAMH"), rs.getString("TENMH"), rs.getInt("LANTHI"), rs.getDate("NGTHI"), rs.getDouble("DIEM"), rs.getString("KQUA"), rs.getString("MALOP"));
                    ketquaThisArrayList.add(ketQuaThi);
                }
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketquaThisArrayList;
    }

    public HocVien searchTenHocVien(String maHocVien) {
        HocVien student = null;
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM HOCVIEN WHERE MAHV = ?";
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maHocVien);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    student = new HocVien(rs.getString("MAHV"), rs.getString("HO"), rs.getString("TEN"), rs.getString("MALOP"));

                }
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return student;
    }

    public MonHoc searchTenMonHoc(String maMonHoc) {
        MonHoc monHoc = null;
        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT * FROM MONHOC WHERE MAMH = ?";
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maMonHoc);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    monHoc = new MonHoc(rs.getString("MAMH"), rs.getString("TENMH"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KetQuaThiDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return monHoc;
    }

//    In Bảng Kết Quả Thi
    public ArrayList<KetQuaThi> filterDeIn(String maMonHoc, String maLop, java.sql.Date ngayThi) {
        ArrayList<KetQuaThi> ketquaThisArrayList = new ArrayList<KetQuaThi>();

        try {
            Connection connection = JDBCConnection.getJDBCConnection();
            String SQL = "SELECT KQ.MAHV, HV.HO, HV.TEN, MH.MAMH, MH.TENMH, KQ.LANTHI, KQ.NGTHI, KQ.DIEM, KQ.KQUA, HV.MALOP FROM KETQUATHI KQ, MONHOC MH, HOCVIEN HV WHERE KQ.MAMH = MH.MAMH AND KQ.MAHV = HV.MAHV AND MH.MAMH = ? AND KQ.NGTHI = ? AND HV.MALOP = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1, maMonHoc);
                preparedStatement.setString(3, maLop);
                preparedStatement.setDate(2, (Date) ngayThi);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    KetQuaThi ketQuaThi = new KetQuaThi(rs.getString("MAHV"), rs.getString("HO") + " " + rs.getString("TEN"), rs.getString("MAMH"), rs.getString("TENMH"), rs.getInt("LANTHI"), rs.getDate("NGTHI"), rs.getDouble("DIEM"), rs.getString("KQUA"), rs.getString("MALOP"));
                    ketquaThisArrayList.add(ketQuaThi);
                }
            } catch (SQLException ex) {
                Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HocVienDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketquaThisArrayList;
    }

    public boolean CheckAddSinhVien(KetQuaThi ketQuaThi) {
        ArrayList<KetQuaThi> ketQuaThisArrayList = new ArrayList<KetQuaThi>();
        ketQuaThisArrayList = this.getAllKetQuaThis();
        for (KetQuaThi ketQuaThiCheck : ketQuaThisArrayList) {
            if (ketQuaThiCheck.getMaHocVien().equals(ketQuaThi.getMaHocVien()) && ketQuaThiCheck.getMaMonHoc().equals(ketQuaThi.getMaMonHoc()) && ketQuaThiCheck.getLanThi() == ketQuaThi.getLanThi()) {
                return false;
            }
        }
        return true;
    }
}
