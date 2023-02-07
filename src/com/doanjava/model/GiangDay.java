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
public class GiangDay {

    private String maLop;
    private String maMonHoc;
    private String tenMonHoc;
    private String maGiaoVien;
    private int hocKy;
    private int namHoc;
    private Date tuNgay;
    private Date denNgay;

    public GiangDay(String maLop, String maMonHoc, String tenMonHoc, String maGiaoVien, int hocKy, int namHoc, Date tuNgay, Date denNgay) {
        this.maLop = maLop;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.maGiaoVien = maGiaoVien;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    public GiangDay(String maLop, String maMonHoc, String tenMonHoc, int hocKy, int namHoc, Date tuNgay, Date denNgay) {
        this.maLop = maLop;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public GiangDay() {
    }

    public GiangDay(String maLop, String maGiaoVien) {
        this.maLop = maLop;
        this.maGiaoVien = maGiaoVien;
    }

    public String getMaLop() {
        return maLop;
    }

    public String getMaMonHocString() {
        return maMonHoc;
    }

    public String getMaGiaoVien() {
        return maGiaoVien;
    }

    public int getHocKy() {
        return hocKy;
    }

    public int getNamHoc() {
        return namHoc;
    }

    public Date getTuNgay() {
        return tuNgay;
    }

    public Date getDenNgay() {
        return denNgay;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public void setMaMonHocString(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public void setMaGiaoVien(String maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public void setNamHoc(int namHoc) {
        this.namHoc = namHoc;
    }

    public void setTuNgay(Date tuNgay) {
        this.tuNgay = tuNgay;
    }

    public void setDenNgay(Date denNgay) {
        this.denNgay = denNgay;
    }

}
