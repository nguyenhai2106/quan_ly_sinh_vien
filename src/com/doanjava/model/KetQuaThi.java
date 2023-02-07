/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.model;

import java.util.Date;

/**
 *
 * @author nguye
 */
// "STT", "Mã Học Viên", "Họ", "Tên", "Mã MH", "Tên Môn Học", "Lần Thi", "Ngày Thi", "Điểm", "Kết Quả", "Mã Lớp"};
public class KetQuaThi implements Comparable<KetQuaThi> {

    private String maHocVien;
    private String hoTenHocVien;
    private String maMonHoc;
    private String tenMonHoc;
    private int lanThi;
    private Date ngayThi;
    private Double diemThi;
    private String ketQua;
    private String maLop;

    public KetQuaThi(String maHocVien, String hoTenHocVien, String maMonHoc, String tenMonHoc, int lanThi, Date ngayThi, Double diemThi, String ketQua, String maLop) {
        this.maHocVien = maHocVien;
        this.hoTenHocVien = hoTenHocVien;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.lanThi = lanThi;
        this.ngayThi = ngayThi;
        this.diemThi = diemThi;
        this.ketQua = ketQua;
        this.maLop = maLop;
    }

    public KetQuaThi(String maHocVien, String maMonHoc, int lanThi, Date ngayThi, Double diemThi, String ketQua) {
        this.maHocVien = maHocVien;
        this.maMonHoc = maMonHoc;
        this.lanThi = lanThi;
        this.ngayThi = ngayThi;
        this.diemThi = diemThi;
        this.ketQua = ketQua;
    }

    public KetQuaThi() {
    }

    public String getMaHocVien() {
        return maHocVien;
    }

    public String getHoTenHocVien() {
        return hoTenHocVien;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public int getLanThi() {
        return lanThi;
    }

    public Date getNgayThi() {
        return ngayThi;
    }

    public Double getDiemThi() {
        return diemThi;
    }

    public String getKetQua() {
        return ketQua;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaHocVien(String maHocVien) {
        this.maHocVien = maHocVien;
    }

    public void setHoTenHocVien(String hoTenHocVien) {
        this.hoTenHocVien = hoTenHocVien;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public void setLanThi(int lanThi) {
        this.lanThi = lanThi;
    }

    public void setNgayThi(Date ngayThi) {
        this.ngayThi = ngayThi;
    }

    public void setDiemThi(Double diemThi) {
        this.diemThi = diemThi;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public void printKetQuaThi() {
        System.out.println(this.getHoTenHocVien() + " - " + this.getTenMonHoc() + " - " + this.getDiemThi());
    }

    @Override
    public int compareTo(KetQuaThi ketQuaThi) {
        return this.getDiemThi().compareTo(ketQuaThi.getDiemThi());
    }

}
