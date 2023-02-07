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
public class Khoa {

    private String maKhoa;
    private String tenKhoa;
    private Date ngayThanhLap;
    private String trươngKhoa;
    private String tenTruongKhoa;

    public Khoa(String maKhoa, String tenKhoa, Date ngayThanhLap, String trươngKhoa, String tenTruongKhoa) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
        this.ngayThanhLap = ngayThanhLap;
        this.trươngKhoa = trươngKhoa;
        this.tenTruongKhoa = tenTruongKhoa;
    }

    public Khoa(String maKhoa, String tenKhoa, Date ngayThanhLap, String trươngKhoa) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
        this.ngayThanhLap = ngayThanhLap;
        this.trươngKhoa = trươngKhoa;
    }

    public Khoa() {
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public Date getNgayThanhLap() {
        return ngayThanhLap;
    }

    public String getTrươngKhoa() {
        return trươngKhoa;
    }

    public String getTenTruongKhoa() {
        return tenTruongKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }

    public void setNgayThanhLap(Date ngayThanhLap) {
        this.ngayThanhLap = ngayThanhLap;
    }

    public void setTrươngKhoa(String trươngKhoa) {
        this.trươngKhoa = trươngKhoa;
    }

    public void setTenTruongKhoa(String tenTruongKhoa) {
        this.tenTruongKhoa = tenTruongKhoa;
    }

}
