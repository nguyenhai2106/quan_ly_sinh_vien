/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doan.service;

import com.doanjava.dao.KetQuaThiDao;
import com.doanjava.dao.HocVienDao;
import com.doanjava.model.BangDiem;
import com.doanjava.model.KetQuaThi;
import com.doanjava.model.MonHoc;
import com.doanjava.model.HocVien;
import com.doanjava.view.KetQuaThiView;
import com.doanjava.view.KhoaView;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class KetQuaThiService {

    private KetQuaThiDao ketQuaThiDao;
    private KetQuaThiView ketQuaThiView;

    public KetQuaThiService() {
        this.ketQuaThiDao = new KetQuaThiDao();
    }

    public KetQuaThiService(KetQuaThiView ketQuaThiView) {
        this.ketQuaThiDao = ketQuaThiDao;
        this.ketQuaThiView = ketQuaThiView;
    }

    public ArrayList<KetQuaThi> getAllKetQuaThis() {
        return ketQuaThiDao.getAllKetQuaThis();
    }

    public ArrayList<KetQuaThi> filterByClass(String maLop) {
        return ketQuaThiDao.filterByClass(maLop);
    }

    public ArrayList<KetQuaThi> filterByMonHoc(String maMonHoc) {
        return ketQuaThiDao.filterByMonHoc(maMonHoc);
    }

    public void addKetQuaThi(KetQuaThi ketQuaThi) {
        ketQuaThiDao.addKetQuaThi(ketQuaThi);
    }

    public void updateKetQuaThi(KetQuaThi ketQuaThi) {
        ketQuaThiDao.updateKetQuaThi(ketQuaThi);
    }

    public void deleteKetQuaThi(String maHocVien, String maMonHoc, int lanThi) {
        ketQuaThiDao.deleteKetQuaThi(maHocVien, maMonHoc, lanThi);
    }

    public ArrayList<BangDiem> getBangDiem(String maHocVien) {
        return ketQuaThiDao.getBangDiem(maHocVien);
    }

    public HocVien searchTenHocVien(String maHocVien) {
        return ketQuaThiDao.searchTenHocVien(maHocVien);
    }

    public MonHoc searchTenMonHoc(String maMonHoc) {
        return ketQuaThiDao.searchTenMonHoc(maMonHoc);
    }

    public ArrayList<KetQuaThi> filterDeIn(String maMonHoc, String maLop, Date ngayThi) {
        return ketQuaThiDao.filterDeIn(maMonHoc, maLop, ngayThi);
    }

    public void ShowKetQuaThiView() {
        ketQuaThiView.setVisible(true);
    }

    public boolean CheckAddSinhVien(KetQuaThi ketQuaThi) {
        return ketQuaThiDao.CheckAddSinhVien(ketQuaThi);
    }
}
