/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doan.service;

import com.doanjava.dao.GiaoVienDao;
import com.doanjava.dao.HocVienDao;
import com.doanjava.model.BangDiem;
import com.doanjava.model.GiangDay;
import com.doanjava.model.GiaoVien;
import com.doanjava.model.HocVien;
import com.doanjava.view.GiaoVienView;
import com.doanjava.view.KhoaView;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class GiaoVienService {

    private GiaoVienDao giaoVienDao;
    private GiaoVienView giaoVienView;

    public GiaoVienService(GiaoVienView giaoVienView) {
        this.giaoVienDao = giaoVienDao;
        this.giaoVienView = giaoVienView;
    }

    public GiaoVienService() {
        this.giaoVienDao = new GiaoVienDao();
    }

    public ArrayList<GiaoVien> getAllGiaoViens() {
        return giaoVienDao.getAllGiaoViens();
    }

    public ArrayList<GiaoVien> filterByKhoa(String maKhoa) {
        return giaoVienDao.filterByKhoa(maKhoa);
    }

    public void addGiaoVien(GiaoVien giaoVien) {
        giaoVienDao.addGiaoVien(giaoVien);
    }

    public void updateGiaoVien(GiaoVien giaoVien) {
        giaoVienDao.updateGiaoVien(giaoVien);
    }

    public void deleteGiaoVien(String maGiaoVien) {
        giaoVienDao.deleteGiaoVien(maGiaoVien);
    }

    public ArrayList<GiangDay> getGiangDay(String maGiaoVien) {
        return giaoVienDao.getGiangDay(maGiaoVien);
    }

    public GiaoVien searchGiaoVien(String maGiaoVien) {
        return giaoVienDao.searchGiaoVien(maGiaoVien);
    }

    public void showGiaoVienView() {
        giaoVienView.setVisible(true);
    }

    public boolean CheckAddGiaoVien(GiaoVien giaoVien) {
        return giaoVienDao.CheckAddGiaoVien(giaoVien);
    }
}
