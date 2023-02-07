/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doan.service;

import com.doanjava.dao.HocVienDao;
import com.doanjava.model.BangDiem;
import com.doanjava.model.HocVien;
import com.doanjava.view.HocVienView;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class HocVienService {

    private HocVienDao studentDao;
    private HocVienView studentView;

    public HocVienService() {
        this.studentDao = new HocVienDao();
    }

    public HocVienService(HocVienView studentView) {
        this.studentDao = studentDao;
        this.studentView = studentView;
    }

    public ArrayList<HocVien> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public ArrayList<HocVien> filterByClass(String maLop) {
        return studentDao.filterByClass(maLop);
    }

    public void addStudent(HocVien student) {
        studentDao.addStudent(student);
    }

    public void updateStudent(HocVien student) {
        studentDao.updateStudent(student);
    }

    public void deleteStudent(String maHocVien) {
        studentDao.deleteStudent(maHocVien);
    }

    public ArrayList<BangDiem> getBangDiem(String maHocVien) {
        return studentDao.getBangDiem(maHocVien);
    }

    public void showStudentView() {
        studentView.setVisible(true);
    }

    public boolean CheckAddSinhVien(HocVien student) {
        return studentDao.CheckAddSinhVien(student);
    }
}
