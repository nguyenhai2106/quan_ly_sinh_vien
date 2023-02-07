/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.model;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author nguye
 */
public class HocVien {

    private String maHocVien;
    private String hoHocVien;
    private String tenHocVien;
    private Date ngaySinh;
    private String gioiTinh;
    private String noiSinh;
    private String maLop;

    public HocVien() {
    }

    public HocVien(String maHocVien, String hoHocVien, String tenHocVien, String maLop) {
        this.maHocVien = maHocVien;
        this.hoHocVien = hoHocVien;
        this.tenHocVien = tenHocVien;
        this.maLop = maLop;
    }

    public HocVien(String maHocVien, String hoHocVien, String tenHocVien, Date ngaySinh, String gioiTinh, String noiSinh, String maLop) {
        this.maHocVien = maHocVien;
        this.hoHocVien = hoHocVien;
        this.tenHocVien = tenHocVien;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.noiSinh = noiSinh;
        this.maLop = maLop;
    }

    public String getMaHocVien() {
        return maHocVien;
    }

    public String getHoHocVien() {
        return hoHocVien;
    }

    public String getTenHocVien() {
        return tenHocVien;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getNoiSinh() {
        return noiSinh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaHocVien(String maHocVien) {
        this.maHocVien = maHocVien;
    }

    public void setHoHocVien(String hoHocVien) {
        this.hoHocVien = hoHocVien;
    }

    public void setTenHocVien(String tenHocVien) {
        this.tenHocVien = tenHocVien;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setNoiSinh(String noiSinh) {
        this.noiSinh = noiSinh;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public void printStudent() {
        System.out.println("MAHV: " + this.maHocVien + " - HO TEN: " + this.hoHocVien + " " + this.tenHocVien + " - NAM SINH: " + this.ngaySinh);
    }

}
