package com.doan.app;

import com.doan.service.LoginService;
import com.doanjava.view.LoginView;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {

    public static void main(String[] args) {
        try {
            /* Create and display the form */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginView logInView = new LoginView();
                LoginService loginService = new LoginService(logInView);
                loginService.showLoginView();
            }
        });
    }
}
