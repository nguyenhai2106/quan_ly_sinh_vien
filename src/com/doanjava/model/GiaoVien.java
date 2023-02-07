/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author nguye
 */
public class GiaoVien {

    private String maGiaoVien;
    private String hoTenGiaoVien;
    private String hocHam;
    private String hocVi;
    private String gioiTinh;
    private Date ngaySinh;
    private Date ngayVaoLam;
    private double heSo;
    private BigDecimal mucLuong;
    private String maKhoa;

    public GiaoVien() {
    }

    public GiaoVien(String maGiaoVien, String hoTenGiaoVien, String hocHam, String hocVi, String gioiTinh, Date ngaySinh, Date ngayVaoLam, double heSo, BigDecimal mucLuong, String maKhoa) {
        this.maGiaoVien = maGiaoVien;
        this.hoTenGiaoVien = hoTenGiaoVien;
        this.hocHam = hocHam;
        this.hocVi = hocVi;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.heSo = heSo;
        this.mucLuong = mucLuong;
        this.maKhoa = maKhoa;
    }

    public GiaoVien(String maGiaoVien, String hoTenGiaoVien, String maKhoa) {
        this.maGiaoVien = maGiaoVien;
        this.hoTenGiaoVien = hoTenGiaoVien;
        this.maKhoa = maKhoa;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setMaGiaoVien(String maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }

    public void setHoTenGiaoVien(String hoTenGiaoVien) {
        this.hoTenGiaoVien = hoTenGiaoVien;
    }

    public void setHocHam(String hocHam) {
        this.hocHam = hocHam;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public void setHeSo(double heSo) {
        this.heSo = heSo;
    }

    public void setMucLuong(BigDecimal mucLuong) {
        this.mucLuong = mucLuong;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getMaGiaoVien() {
        return maGiaoVien;
    }

    public String getHoTenGiaoVien() {
        return hoTenGiaoVien;
    }

    public String getHocHam() {
        return hocHam;
    }

    public String getHocVi() {
        return hocVi;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public double getHeSo() {
        return heSo;
    }

    public BigDecimal getMucLuong() {
        return mucLuong;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void printGiaoVien() {
        System.out.println("MAHV: " + this.maGiaoVien + " - HO TEN: " + this.hoTenGiaoVien + " - NAM SINH: " + this.ngaySinh);
    }

}
