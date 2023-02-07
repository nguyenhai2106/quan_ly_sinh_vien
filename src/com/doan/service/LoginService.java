/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doan.service;

import com.doanjava.dao.UserDao;
import com.doanjava.model.User;
import com.doanjava.view.LoginView;
import com.doanjava.view.HocVienView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author nguye
 */
public class LoginService {

    private UserDao userDao;
    private LoginView logInView;
    private HocVienView studentView;

    public LoginService(LoginView view) {
        this.logInView = view;
        this.userDao = new UserDao();
    }

    public void showLoginView() {
        logInView.setVisible(true);
    }

}
