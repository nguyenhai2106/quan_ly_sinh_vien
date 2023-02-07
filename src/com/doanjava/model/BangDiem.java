/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.model;

/**
 *
 * @author nguye
 */
public class BangDiem {

    private String maHocVien;
    private String maMonHoc;
    private String tenMonHoc;
    private short tinhChiLT;
    private short tinChiTH;
    private double diemThi;

    public BangDiem(String maHocVien, String maMonHocString, String tenMonHocString, short tinhChiLT, short tinChiTH, double diemThi) {
        this.maHocVien = maHocVien;
        this.maMonHoc = maMonHocString;
        this.tenMonHoc = tenMonHocString;
        this.tinhChiLT = tinhChiLT;
        this.tinChiTH = tinChiTH;
        this.diemThi = diemThi;
    }

    public void setMaHocVien(String maHocVien) {
        this.maHocVien = maHocVien;
    }

    public void setMaMonHocString(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public void setTenMonHocString(String tenMonHocString) {
        this.tenMonHoc = tenMonHoc;
    }

    public void setTinhChiLT(short tinhChiLT) {
        this.tinhChiLT = tinhChiLT;
    }

    public void setTinChiTH(short tinChiTH) {
        this.tinChiTH = tinChiTH;
    }

    public void setDiemThi(double diemThi) {
        this.diemThi = diemThi;
    }

    public String getMaHocVien() {
        return maHocVien;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public int getTinhChiLT() {
        return tinhChiLT;
    }

    public int getTinChiTH() {
        return tinChiTH;
    }

    public double getDiemThi() {
        return diemThi;
    }

    public void printBangDiem() {
        System.out.println(this.getMaMonHoc() + " " + this.getTenMonHoc() + " - " + this.getTinChiTH());
    }
}
