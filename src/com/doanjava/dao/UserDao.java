/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doanjava.dao;

import com.doanjava.model.User;

/**
 *
 * @author nguye
 */
public class UserDao {

    public boolean checkUser(User user) {
        if (user != null) {
            if ("admin".equals(user.getUserName())
                    && "admin".equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
