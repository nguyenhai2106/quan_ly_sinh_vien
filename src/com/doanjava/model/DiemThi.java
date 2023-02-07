/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.model;

/**
 *
 * @author nguye
 */
public class DiemThi {

    private String maMonHoc;
    private String tenMonHoc;
    private Double diemThi;
    private int soTinChi;

    public DiemThi() {
    }

    public DiemThi(String maMonHoc, Double diemThi, int soTinChi
    ) {
        this.maMonHoc = maMonHoc;
        this.diemThi = diemThi;
        this.soTinChi = soTinChi;
    }

    public DiemThi(String maMonHoc, String tenMonHoc, Double diemThi, int soTinChi) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.diemThi = diemThi;
        this.soTinChi = soTinChi;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public Double getDiemThi() {
        return diemThi;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public void setDiemThi(Double diemThi) {
        this.diemThi = diemThi;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public void printDiemThi() {
        System.out.println(this.maMonHoc + " - " + this.soTinChi + " - " + this.diemThi);
    }
}
