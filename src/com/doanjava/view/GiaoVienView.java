/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.doanjava.view;

import com.doan.service.GiaoVienService;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.doanjava.model.HocVien;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.doanjava.dao.JDBCConnection;
import com.doan.service.HocVienService;
import com.doanjava.model.BangDiem;
import com.doanjava.model.GiangDay;
import com.doanjava.model.GiaoVien;
import com.doanjava.model.MonHoc;
import java.awt.Color;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author nguye
 */
public class GiaoVienView extends javax.swing.JFrame {

    private String[] columnNames = new String[]{
        "STT", "Mã GV", "Họ Tên", "Học Hàm", "Học Vị", "Ngày Sinh", "Giới Tính", "Ngày Vào Làm", "Hệ Số", "Mức Lương", "Mã Khoa"};
    GiaoVienService giaoVienService;
    DefaultTableModel defaultTableModel;
    DefaultTableModel defaultTableGiangDayModel;

    public void chooseGiaoVienTable() {
        ListSelectionModel listSelectionModel = giaoVienTable.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = giaoVienTable.getSelectedRow();
                if (row >= 0) {
                    String maGiaoVien = String.valueOf(giaoVienTable.getValueAt(row, 1));
                    maGiaoVienTextField.setText(maGiaoVien);
                    gd_maGiaoVienTextField.setText(maGiaoVien);

                    String hoTenGiaoVien = String.valueOf(giaoVienTable.getValueAt(row, 2));
                    hoTenGiaoVienTextField.setText(hoTenGiaoVien);
                    gd_hoTenTextField.setText(hoTenGiaoVien);

                    String hocHam = String.valueOf(giaoVienTable.getValueAt(row, 3));
                    hocHamTextField.setText(hocHam);
                    String hocVi = String.valueOf(giaoVienTable.getValueAt(row, 4));
                    hocViTextField.setText(hocVi);

                    java.util.Date ngaySinh;
                    try {
                        ngaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(giaoVienTable.getValueAt(row, 5)));
                        namSinhDateChooser.setDate(ngaySinh);
                    } catch (ParseException ex) {
                        Logger.getLogger(GiaoVienView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String gioiTinh = String.valueOf(giaoVienTable.getValueAt(row, 6));
                    if (gioiTinh.equals("Nam")) {
                        namRadioButton.setSelected(true);
                        nuRadioButton.setSelected(false);
                    } else {
                        nuRadioButton.setSelected(true);
                        namRadioButton.setSelected(false);
                    }

                    java.util.Date ngayVL;
                    try {
                        ngayVL = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(giaoVienTable.getValueAt(row, 7)));
                        ngayVaoLamDateChooser.setDate(ngayVL);
                    } catch (ParseException ex) {
                        Logger.getLogger(GiaoVienView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String heSo = String.valueOf(giaoVienTable.getValueAt(row, 8));
                    heSoTextField.setText(heSo);

                    String mucLuong = String.valueOf(giaoVienTable.getValueAt(row, 9));
                    mucLuongTextField.setText(mucLuong);

                    String maKhoa = String.valueOf(giaoVienTable.getValueAt(row, 10));

                    maKhoaComboBoxThongTin.setSelectedItem(maKhoa);

                    gd_maKhoaTextField.setText(maKhoa);
                    kiemTraMaGiaoVienLabel.setText("");
                    // enable Edit and Delete buttons
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    renderGiangDay(maGiaoVien);
                }
            }
        });
    }

    /**
     * Creates new form QLGVView
     */
    public void searchGiaoVien(String str) {
        DefaultTableModel tableModel = (DefaultTableModel) giaoVienTable.getModel();
        TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(tableModel);
        giaoVienTable.setRowSorter(tableRowSorter);
        tableRowSorter.setRowFilter(RowFilter.regexFilter(str));
    }

    public void setDataTable(ArrayList<GiaoVien> giaoVienArrayList) {
        int index = 0;
        for (GiaoVien giaoVien : giaoVienArrayList) {
            defaultTableModel.addRow(new Object[]{
                index + 1, giaoVien.getMaGiaoVien(), giaoVien.getHoTenGiaoVien(), giaoVien.getHocHam(), giaoVien.getHocVi(), giaoVien.getNgaySinh(), giaoVien.getGioiTinh(), giaoVien.getNgayVaoLam(), giaoVien.getHeSo(), giaoVien.getMucLuong().setScale(0), giaoVien.getMaKhoa()});
            index++;
        }
    }

    public void setDataGiangDayTable(ArrayList<GiangDay> giangDaysArrayList) {
        int index = 0;
        for (GiangDay giangDay : giangDaysArrayList) {
            defaultTableGiangDayModel.addRow(new Object[]{
                index + 1, giangDay.getMaLop(), giangDay.getMaMonHocString(), giangDay.getTenMonHoc(), giangDay.getHocKy(), giangDay.getNamHoc(), giangDay.getTuNgay(), giangDay.getDenNgay()});
            index++;
        }
    }

    public GiaoVienView() {
        initComponents();
        chooseGiaoVienTable();
        maKhoaComboBox();
        maKhoaComboBoxThongTin();
        setResizable(false);
        giaoVienService = new GiaoVienService();
        defaultTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        giaoVienTable.setModel(defaultTableModel);
        ArrayList<GiaoVien> giaoViensArrayList = giaoVienService.getAllGiaoViens();
        setDataTable(giaoViensArrayList);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        TableColumnModel columnModel = giaoVienTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
        columnModel.getColumn(5).setPreferredWidth(60);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);
        columnModel.getColumn(6).setPreferredWidth(60);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);
        columnModel.getColumn(7).setCellRenderer(centerRenderer);
        columnModel.getColumn(8).setPreferredWidth(60);
        columnModel.getColumn(8).setCellRenderer(rightRenderer);
        columnModel.getColumn(9).setCellRenderer(rightRenderer);
        columnModel.getColumn(10).setCellRenderer(centerRenderer);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        maGiaoVienTextField = new javax.swing.JTextField();
        hoTenGiaoVienTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hocHamTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        namRadioButton = new javax.swing.JRadioButton();
        nuRadioButton = new javax.swing.JRadioButton();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        ngayVaoLamDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        hocViTextField = new javax.swing.JTextField();
        namSinhDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        heSoTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        mucLuongTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        maKhoaComboBoxThongTin = new javax.swing.JComboBox<>();
        kiemTraMaGiaoVienLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        giangDayTable = new javax.swing.JTable();
        bd_maHocVienLabel = new javax.swing.JLabel();
        gd_maGiaoVienTextField = new javax.swing.JTextField();
        bd_hoTenLabel = new javax.swing.JLabel();
        gd_hoTenTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        gd_maKhoaTextField = new javax.swing.JTextField();
        bd_refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        giaoVienTable = new javax.swing.JTable();
        refreshButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        maKhoaComboBox = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUẢN LÝ GIÁO VIÊN");

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jPanel2.setPreferredSize(new java.awt.Dimension(450, 401));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Mã Giáo Viên");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Họ Tên");

        maGiaoVienTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                maGiaoVienTextFieldFocusLost(evt);
            }
        });
        maGiaoVienTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                maGiaoVienTextFieldKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Học Hàm");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Năm Sinh");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Giới Tính");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Ngày Vào Làm");

        buttonGroup1.add(namRadioButton);
        namRadioButton.setText("Nam");

        buttonGroup1.add(nuRadioButton);
        nuRadioButton.setText("Nữ");

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Học Vị");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Hệ Số");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Mức Lương");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Mã Khoa");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namSinhDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maGiaoVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maKhoaComboBoxThongTin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6))
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ngayVaoLamDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(namRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(nuRadioButton)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(heSoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addComponent(mucLuongTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(hocHamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hocViTextField))
                            .addComponent(hoTenGiaoVienTextField)
                            .addComponent(kiemTraMaGiaoVienLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(25, 25, 25))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(editButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton)
                        .addGap(18, 18, 18)
                        .addComponent(clearButton))
                    .addComponent(addButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel4, jLabel6, jLabel7, namRadioButton, nuRadioButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {heSoTextField, hocHamTextField, maGiaoVienTextField, mucLuongTextField});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel13});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(maGiaoVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(maKhoaComboBoxThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(kiemTraMaGiaoVienLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(hoTenGiaoVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(hocHamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hocViTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(namSinhDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel6)
                            .addComponent(namRadioButton)
                            .addComponent(nuRadioButton))
                        .addGap(25, 25, 25)
                        .addComponent(jLabel7))
                    .addComponent(ngayVaoLamDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heSoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mucLuongTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton)
                    .addComponent(clearButton)
                    .addComponent(deleteButton))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {hoTenGiaoVienTextField, hocHamTextField, hocViTextField, jLabel10, jLabel13, jLabel3, jLabel4, jLabel7, maGiaoVienTextField, maKhoaComboBoxThongTin, namSinhDateChooser, ngayVaoLamDateChooser});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {heSoTextField, jLabel11, jLabel12, mucLuongTextField});

        jTabbedPane1.addTab("Thông Tin", jPanel2);

        giangDayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Lớp", "Mã MH", "Tên MH", "HK", "Năm", "Từ Ngày", "Đến Ngày"
            }
        ));
        jScrollPane2.setViewportView(giangDayTable);
        if (giangDayTable.getColumnModel().getColumnCount() > 0) {
            giangDayTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            giangDayTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            giangDayTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            giangDayTable.getColumnModel().getColumn(3).setPreferredWidth(120);
            giangDayTable.getColumnModel().getColumn(4).setPreferredWidth(30);
            giangDayTable.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        bd_maHocVienLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bd_maHocVienLabel.setText("Mã Giáo Viên");
        bd_maHocVienLabel.setMaximumSize(new java.awt.Dimension(72, 30));
        bd_maHocVienLabel.setMinimumSize(new java.awt.Dimension(72, 30));
        bd_maHocVienLabel.setPreferredSize(new java.awt.Dimension(72, 30));

        gd_maGiaoVienTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                gd_maGiaoVienTextFieldKeyReleased(evt);
            }
        });

        bd_hoTenLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bd_hoTenLabel.setText("Họ và Tên");

        gd_hoTenTextField.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Mã Khoa");

        gd_maKhoaTextField.setEditable(false);

        bd_refreshButton.setText("Refresh");
        bd_refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bd_refreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(bd_maHocVienLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gd_maGiaoVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bd_hoTenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gd_hoTenTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gd_maKhoaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(bd_refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bd_maHocVienLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gd_maGiaoVienTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bd_hoTenLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gd_hoTenTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gd_maKhoaTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addGap(32, 32, 32)
                .addComponent(bd_refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bd_hoTenLabel, bd_maHocVienLabel, gd_hoTenTextField, gd_maGiaoVienTextField, gd_maKhoaTextField});

        jTabbedPane1.addTab("Giảng Dạy", jPanel1);

        giaoVienTable.setAutoCreateRowSorter(true);
        giaoVienTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        giaoVienTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"
            }
        ));
        giaoVienTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        giaoVienTable.setShowGrid(true);
        jScrollPane1.setViewportView(giaoVienTable);
        giaoVienTable.setRowHeight(30);

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText(" Tìm Kiếm");
        jLabel8.setMaximumSize(new java.awt.Dimension(72, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(72, 22));
        jLabel8.setPreferredSize(new java.awt.Dimension(72, 22));

        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });

        maKhoaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maKhoaComboBoxActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refreshButton)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTextField)
                        .addGap(18, 18, 18)
                        .addComponent(maKhoaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maKhoaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, maKhoaComboBox, searchTextField});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableModel.setRowCount(0);
        setDataTable(giaoVienService.getAllGiaoViens());
    }//GEN-LAST:event_refreshButtonActionPerformed

    private boolean validateMaGiaoVien() {
        String maGiaoVien = maGiaoVienTextField.getText();
        if (maGiaoVien == null || "".equals(maGiaoVien.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateHoTenGiaoVien() {
        String hotenGiaoVien = hoTenGiaoVienTextField.getText();
        if (hotenGiaoVien == null || "".equals(hotenGiaoVien.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateHocHam() {
        String hocHam = hocHamTextField.getText();
        if (hocHam == null || "".equals(hocHam.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateHocVi() {
        String hocVi = hocViTextField.getText();
        if (hocVi == null || "".equals(hocVi.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateNgaySinh() {
        if (namSinhDateChooser.getDate() == null) {
            return false;
        }
        return true;
    }

    private boolean validateNgayVL() {
        if (ngayVaoLamDateChooser.getDate() == null) {
            return false;
        }
        return true;
    }

    private boolean validateGioiTinh() {
        if (!namRadioButton.isSelected() && !nuRadioButton.isSelected()) {
            return false;
        }
        return true;
    }

    private boolean validateMaKhoa() {
        String maKhoa = maKhoaComboBoxThongTin.getSelectedItem().toString();
        if (maKhoa.equals("All")) {
            return false;
        }
        return true;
    }

    private boolean validateHeSo() {
        String heSo = heSoTextField.getText();
        if (heSoTextField.getText() == null || "".equals(heSo.trim())) {
            return false;
        } else {
            Double heSoDouble = Double.valueOf(heSoTextField.getText());
            if (heSoDouble < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean validateMucLuong() {
        String mucLuong = mucLuongTextField.getText();
        if (mucLuongTextField.getText() == null || "".equals(mucLuong.trim())) {
            return false;
        } else {
            Double mucLuongDouble = Double.valueOf(mucLuongTextField.getText());
            if (mucLuongDouble < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean validationGiaoVien() {
        return validateMaGiaoVien() && validateMaKhoa() && validateHoTenGiaoVien() && validateHocHam() && validateHocVi() && validateNgaySinh() && validateNgayVL() && validateGioiTinh() && validateHeSo() && validateMucLuong();
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        giaoVienService = new GiaoVienService();
        GiaoVien giaoVien = new GiaoVien();
        if (validationGiaoVien()) {
            giaoVien.setMaGiaoVien(maGiaoVienTextField.getText());
            giaoVien.setHoTenGiaoVien(hoTenGiaoVienTextField.getText());
            giaoVien.setHocHam(hocHamTextField.getText());
            giaoVien.setHocVi(hocViTextField.getText());
            String gioiTinh = namRadioButton.isSelected() ? "Nam" : "Nu";
            giaoVien.setGioiTinh(gioiTinh);
            java.sql.Date ngaySinh = new java.sql.Date(namSinhDateChooser.getDate().getTime());
            giaoVien.setNgaySinh(ngaySinh);
            java.sql.Date ngayVL = new java.sql.Date(ngayVaoLamDateChooser.getDate().getTime());
            giaoVien.setNgayVaoLam(ngayVL);
            giaoVien.setHeSo(Double.valueOf(heSoTextField.getText()));
            giaoVien.setMucLuong(BigDecimal.valueOf(Double.valueOf(mucLuongTextField.getText())));
            giaoVien.setMaKhoa(maKhoaComboBoxThongTin.getSelectedItem().toString());
        } else {
            JOptionPane.showMessageDialog(GiaoVienView.this, "Vui lòng kiểm tra lại thông tin đã nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (giaoVienService.CheckAddGiaoVien(giaoVien)) {
            giaoVienService.addGiaoVien(giaoVien);
            defaultTableModel.setRowCount(0);
            setDataTable(giaoVienService.getAllGiaoViens());
        } else {
            JOptionPane.showMessageDialog(GiaoVienView.this, "Giáo Viên đã tồn tại trên hệ thống. Vui lòng kiểm tra lại dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int row = giaoVienTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(GiaoVienView.this, "Vui lòng chọn Giao Viên muốn xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(GiaoVienView.this, "Bạn chắc chắn muốn xóa?");
            if (confirm == JOptionPane.YES_OPTION) {
                String giaoVienMaGiaoVien = (String) giaoVienTable.getValueAt(row, 1);
                giaoVienService.deleteGiaoVien(giaoVienMaGiaoVien);
                defaultTableModel.setRowCount(0);
                setDataTable(giaoVienService.getAllGiaoViens());
                clearButtonActionPerformed(evt);
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        maGiaoVienTextField.setText("");
        hoTenGiaoVienTextField.setText("");
        hocHamTextField.setText("");
        hocViTextField.setText("");
        namSinhDateChooser.setCalendar(null);
        ngayVaoLamDateChooser.setCalendar(null);
        buttonGroup1.clearSelection();
        heSoTextField.setText("");
        mucLuongTextField.setText("");
        maKhoaComboBoxThongTin.setSelectedIndex(0);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        kiemTraMaGiaoVienLabel.setText("");
    }//GEN-LAST:event_clearButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        giaoVienService = new GiaoVienService();
        GiaoVien giaoVien = new GiaoVien();
        if (validationGiaoVien()) {
            giaoVien.setMaGiaoVien(maGiaoVienTextField.getText());
            giaoVien.setHoTenGiaoVien(hoTenGiaoVienTextField.getText());
            giaoVien.setHocHam(hocHamTextField.getText());
            giaoVien.setHocVi(hocViTextField.getText());
            String gioiTinh = namRadioButton.isSelected() ? "Nam" : "Nữ";
            giaoVien.setGioiTinh(gioiTinh);
            java.sql.Date ngaySinh = new java.sql.Date(namSinhDateChooser.getDate().getTime());
            giaoVien.setNgaySinh(ngaySinh);
            java.sql.Date ngayVL = new java.sql.Date(ngayVaoLamDateChooser.getDate().getTime());
            giaoVien.setNgayVaoLam(ngayVL);
            giaoVien.setHeSo(Double.valueOf(heSoTextField.getText()));
            giaoVien.setMucLuong(BigDecimal.valueOf(Double.valueOf(mucLuongTextField.getText())));
            giaoVien.setMaKhoa(maKhoaComboBoxThongTin.getItemAt(maKhoaComboBoxThongTin.getSelectedIndex()));
            giaoVienService.updateGiaoVien(giaoVien);
            defaultTableModel.setRowCount(0);
            setDataTable(giaoVienService.getAllGiaoViens());
        } else {
            JOptionPane.showMessageDialog(GiaoVienView.this, "Vui lòng kiểm tra lại thông tin đã nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_editButtonActionPerformed

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        // TODO add your handling code here:
        String searchString = searchTextField.getText();
        searchGiaoVien(searchString);
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void bd_refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bd_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableGiangDayModel.setRowCount(0);
        if (gd_maGiaoVienTextField.getText() != "") {
            setDataGiangDayTable(giaoVienService.getGiangDay(gd_maGiaoVienTextField.getText()));
        }
    }//GEN-LAST:event_bd_refreshButtonActionPerformed

    private void gd_maGiaoVienTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gd_maGiaoVienTextFieldKeyReleased
        String maGiaoVien = gd_maGiaoVienTextField.getText();
        if (!"".equals(maGiaoVien)) {
            GiaoVien giaoVien = giaoVienService.searchGiaoVien(maGiaoVien);
            if (giaoVien != null) {
                gd_hoTenTextField.setText(giaoVien.getHoTenGiaoVien());
                gd_maKhoaTextField.setText(giaoVien.getMaKhoa());
                renderGiangDay(maGiaoVien);
            } else {
                gd_hoTenTextField.setText("Không tìm thấy Giao Vien!");
            }
        } else {
            gd_hoTenTextField.setText("");
            gd_maKhoaTextField.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_gd_maGiaoVienTextFieldKeyReleased

    private void maKhoaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maKhoaComboBoxActionPerformed
        // TODO add your handling code here:
        giaoVienService = new GiaoVienService();
        String valueMaKhoa = maKhoaComboBox.getItemAt(maKhoaComboBox.getSelectedIndex());
        if (valueMaKhoa.equals("All")) {
            defaultTableModel.setRowCount(0);
            setDataTable(giaoVienService.getAllGiaoViens());
        } else {
            defaultTableModel.setRowCount(0);
            setDataTable(giaoVienService.filterByKhoa(valueMaKhoa));
        }
    }//GEN-LAST:event_maKhoaComboBoxActionPerformed

    private void maGiaoVienTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maGiaoVienTextFieldKeyReleased
        // TODO add your handling code here:
        String maGiaoVien = maGiaoVienTextField.getText();
        if (!"".equals(maGiaoVien) && !" ".equals(maGiaoVien)) {
            GiaoVien giaoVien = giaoVienService.searchGiaoVien(maGiaoVien);
            if (giaoVien != null) {
                kiemTraMaGiaoVienLabel.setForeground(Color.RED);
                kiemTraMaGiaoVienLabel.setText("Mã Giáo Viên đã tồn tại!");
            } else {
                kiemTraMaGiaoVienLabel.setForeground(Color.BLUE);
                kiemTraMaGiaoVienLabel.setText("");
            }
        } else {
            kiemTraMaGiaoVienLabel.setForeground(Color.RED);
            kiemTraMaGiaoVienLabel.setText("Không được để trống Mã Giáo Viên!");
        }
    }//GEN-LAST:event_maGiaoVienTextFieldKeyReleased

    private void maGiaoVienTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_maGiaoVienTextFieldFocusLost
        // TODO add your handling code here:
        if (!validateMaGiaoVien()) {
            kiemTraMaGiaoVienLabel.setForeground(Color.RED);
            kiemTraMaGiaoVienLabel.setText("Không được để trống Mã Giáo Viên!");
        } else {
            kiemTraMaGiaoVienLabel.setText("");
        }
    }//GEN-LAST:event_maGiaoVienTextFieldFocusLost

    /**
     * @param args the command line arguments
     */
    public void maKhoaComboBox() {
        giaoVienService = new GiaoVienService();
        ArrayList<GiaoVien> giaoViensArrayList = giaoVienService.getAllGiaoViens();
        Set<String> treeSetString = new TreeSet();
        treeSetString.add("All");
        for (int i = 0; i < giaoViensArrayList.size(); ++i) {
            treeSetString.add(giaoViensArrayList.get(i).getMaKhoa());
        }
        DefaultComboBoxModel maKhoaBoxModel = new DefaultComboBoxModel();
        for (String element : treeSetString) {
            maKhoaBoxModel.addElement(element);
        }
        maKhoaComboBox.setModel(maKhoaBoxModel);
    }

    public void maKhoaComboBoxThongTin() {
        giaoVienService = new GiaoVienService();
        ArrayList<GiaoVien> giaoViensArrayList = giaoVienService.getAllGiaoViens();
        Set<String> treeSetString = new TreeSet();
        treeSetString.add("All");
        for (int i = 0; i < giaoViensArrayList.size(); ++i) {
            treeSetString.add(giaoViensArrayList.get(i).getMaKhoa());
        }
        DefaultComboBoxModel maKhoaBoxModel = new DefaultComboBoxModel();
        for (String element : treeSetString) {
            maKhoaBoxModel.addElement(element);
        }
        maKhoaComboBoxThongTin.setModel(maKhoaBoxModel);
    }

    // Bảng điểm Sinh viên
    public void renderGiangDay(String maGiaoVien) {
        giaoVienService = new GiaoVienService();
        String[] columnNamesBangDiem = new String[]{
            "STT", "Mã Lớp", "Mã MH", "Tên Môn Hoc", "HK", "Nam", "Từ Ngày", "Đến Ngày"};
        defaultTableGiangDayModel = new DefaultTableModel(columnNamesBangDiem, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        giangDayTable.setModel(defaultTableGiangDayModel);
        setDataGiangDayTable(giaoVienService.getGiangDay(maGiaoVien));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = giangDayTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(40);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(40);
        columnModel.getColumn(3).setPreferredWidth(120);
        columnModel.getColumn(4).setPreferredWidth(30);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
        columnModel.getColumn(5).setPreferredWidth(30);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);
        columnModel.getColumn(7).setCellRenderer(centerRenderer);
    }

    public static void main(String args[]) throws SQLException {
        ArrayList<HocVien> studentsArrayList = new ArrayList<HocVien>();

        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Mental".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(GiaoVienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(GiaoVienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(GiaoVienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(GiaoVienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            //</editor-fold>
            /* Create and display the form */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new GiaoVienView().setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GiaoVienView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel bd_hoTenLabel;
    private javax.swing.JLabel bd_maHocVienLabel;
    private javax.swing.JButton bd_refreshButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField gd_hoTenTextField;
    private javax.swing.JTextField gd_maGiaoVienTextField;
    private javax.swing.JTextField gd_maKhoaTextField;
    private javax.swing.JTable giangDayTable;
    private javax.swing.JTable giaoVienTable;
    private javax.swing.JTextField heSoTextField;
    private javax.swing.JTextField hoTenGiaoVienTextField;
    private javax.swing.JTextField hocHamTextField;
    private javax.swing.JTextField hocViTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel kiemTraMaGiaoVienLabel;
    private javax.swing.JTextField maGiaoVienTextField;
    private javax.swing.JComboBox<String> maKhoaComboBox;
    private javax.swing.JComboBox<String> maKhoaComboBoxThongTin;
    private javax.swing.JTextField mucLuongTextField;
    private javax.swing.JRadioButton namRadioButton;
    private com.toedter.calendar.JDateChooser namSinhDateChooser;
    private com.toedter.calendar.JDateChooser ngayVaoLamDateChooser;
    private javax.swing.JRadioButton nuRadioButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
