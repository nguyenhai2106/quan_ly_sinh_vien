/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doan.service;

import com.doanjava.dao.KhoaDao;
import com.doanjava.dao.HocVienDao;
import com.doanjava.model.BangDiem;
import com.doanjava.model.Khoa;
import com.doanjava.model.HocVien;
import com.doanjava.view.KhoaView;
import com.doanjava.view.HocVienView;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class KhoaService {

    private KhoaDao khoaDao;
    private KhoaView khoaView;

    public KhoaService(KhoaView khoaView) {
        this.khoaDao = khoaDao;
        this.khoaView = khoaView;
    }

    public KhoaService() {
        this.khoaDao = new KhoaDao();
    }

    public ArrayList<Khoa> getAllKhoas() {
        return khoaDao.getAllKhoas();
    }

    public void addKhoa(Khoa khoa) {
        khoaDao.addKhoa(khoa);
    }

    public void updateKhoa(Khoa khoa) {
        khoaDao.updateKhoa(khoa);
    }

    public void deleteKhoa(String maKhoa) {
        khoaDao.deleteKhoa(maKhoa);
    }

    public void showKhoaView() {
        khoaView.setVisible(true);
    }
}
