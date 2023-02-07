/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.model;

/**
 *
 * @author nguye
 */
public class MonHoc {

    private String maMonHoc;
    private String tenMonHoc;
    private int tinChiLT;
    private int tinChiTH;
    private String maKhoaString;

    public MonHoc(String maMonHoc, String tenMonHoc, int tinChiLT, int tinChiTH, String maKhoaString) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.tinChiLT = tinChiLT;
        this.tinChiTH = tinChiTH;
        this.maKhoaString = maKhoaString;
    }

    public MonHoc() {
    }

    public MonHoc(String maMonHoc, String tenMonHoc) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public void setTinChiLT(int tinChiLT) {
        this.tinChiLT = tinChiLT;
    }

    public void setTinChiTH(int tinChiTH) {
        this.tinChiTH = tinChiTH;
    }

    public void setMaKhoaString(String maKhoaString) {
        this.maKhoaString = maKhoaString;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public int getTinChiLT() {
        return tinChiLT;
    }

    public int getTinChiTH() {
        return tinChiTH;
    }

    public String getMaKhoaString() {
        return maKhoaString;
    }

}
