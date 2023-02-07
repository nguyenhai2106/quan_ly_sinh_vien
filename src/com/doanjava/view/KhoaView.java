/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.doanjava.view;

import com.doan.service.GiaoVienService;
import com.doan.service.KhoaService;
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
import com.doanjava.model.DiemThi;
import com.doanjava.model.GiaoVien;
import com.doanjava.model.Khoa;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author nguye
 */
public class KhoaView extends javax.swing.JFrame {

    private String[] columnNames = new String[]{
        "STT", "Mã Khoa", "Tên Khoa", "Ngày Thành Lập", "Trưởng Khoa", "Tên Trưởng Khoa"};
    KhoaService khoaService;
    DefaultTableModel defaultTableModel;

    public void export(JTable table, File file, String[] columnNamesPrint) {
        try {
            TableModel tableModel = table.getModel();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("\t\t\t" + "Danh Sach Hoc Vien" + "\n");
            for (int i = 0; i < columnNamesPrint.length; ++i) {
                fileWriter.write(columnNamesPrint[i] + "\t");
            }
            fileWriter.write("\n");
            for (int i = 0; i < tableModel.getRowCount(); ++i) {
                for (int j = 0; j < tableModel.getColumnCount(); ++j) {
                    fileWriter.write(tableModel.getValueAt(i, j).toString() + "\t");
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(KhoaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chooseKhoaTable() {
        ListSelectionModel listSelectionModel = khoaTable.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = khoaTable.getSelectedRow();
                if (row >= 0) {
                    String maKhoa = String.valueOf(khoaTable.getValueAt(row, 1));
                    maKhoaTextField.setText(maKhoa);

                    String tenKhoa = String.valueOf(khoaTable.getValueAt(row, 2));
                    tenKhoaTextField.setText(tenKhoa);

                    java.util.Date ngaySinh;
                    try {
                        ngaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(khoaTable.getValueAt(row, 3)));
                        ngayThanhLapDateChooser.setDate(ngaySinh);

                    } catch (ParseException ex) {
                        Logger.getLogger(KhoaView.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                    String truongKhoa = String.valueOf(khoaTable.getValueAt(row, 4));
                    System.out.println(truongKhoa);
                    String tenTruongKhoa = String.valueOf(khoaTable.getValueAt(row, 5));
                    String truongKhoaChoose = truongKhoa + " - " + tenTruongKhoa;
//                    maTruongKhoaComboBox.setSelectedItem(truongKhoaChoose);
                    if (khoaTable.getValueAt(row, 4) == null) {
                        maTruongKhoaComboBox.setSelectedIndex(0);
                    } else {
                        maTruongKhoaComboBox.setSelectedItem(truongKhoaChoose);
                    }

                    // enable Edit and Delete buttons
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                }
            }
        });
    }

    /**
     * Creates new form QLGVView
     */
    public void searchKhoa(String str) {
        DefaultTableModel tableModel = (DefaultTableModel) khoaTable.getModel();
        TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(tableModel);
        khoaTable.setRowSorter(tableRowSorter);
        tableRowSorter.setRowFilter(RowFilter.regexFilter(str));
    }

    public void setDataTable(ArrayList<Khoa> khoasArrayList) {
        int index = 0;
        for (Khoa khoa : khoasArrayList) {
            defaultTableModel.addRow(new Object[]{
                index + 1, khoa.getMaKhoa(), khoa.getTenKhoa(), khoa.getNgayThanhLap(), khoa.getTrươngKhoa(), khoa.getTenTruongKhoa(),});
            index++;
        }
    }

    public KhoaView() {
        initComponents();
        chooseKhoaTable();
        maGiaoVienComboBox();
        setResizable(false);
        khoaService = new KhoaService();
        defaultTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        khoaTable.setModel(defaultTableModel);

        ArrayList<Khoa> studentsArrayList = khoaService.getAllKhoas();
        setDataTable(studentsArrayList);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = khoaTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setPreferredWidth(50);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
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
        maKhoaTextField = new javax.swing.JTextField();
        tenKhoaTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tenTruongKhoaTextField = new javax.swing.JTextField();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        ngayThanhLapDateChooser = new com.toedter.calendar.JDateChooser();
        maTruongKhoaComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        khoaTable = new javax.swing.JTable();
        refreshButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUẢN LÝ KHOA");

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jPanel2.setPreferredSize(new java.awt.Dimension(450, 401));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Mã Khoa");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Tên Khoa");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Ngày Thành Lập");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Trưởng Khoa");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Tên Trưởng Khoa");

        tenTruongKhoaTextField.setEditable(false);

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

        maTruongKhoaComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        maTruongKhoaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maTruongKhoaComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(maTruongKhoaComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tenTruongKhoaTextField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ngayThanhLapDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maKhoaTextField))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tenKhoaTextField)))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(editButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton)
                        .addGap(18, 18, 18)
                        .addComponent(clearButton))
                    .addComponent(addButton))
                .addGap(44, 44, 44))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel4, jLabel6, jLabel7});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(maKhoaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(tenKhoaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ngayThanhLapDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(maTruongKhoaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tenTruongKhoaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton)
                    .addComponent(clearButton)
                    .addComponent(deleteButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jLabel6, maKhoaTextField, maTruongKhoaComboBox, tenKhoaTextField, tenTruongKhoaTextField});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jTabbedPane1.addTab("Thông Tin", jPanel2);

        khoaTable.setAutoCreateRowSorter(true);
        khoaTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        khoaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        khoaTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        khoaTable.setShowGrid(true);
        jScrollPane1.setViewportView(khoaTable);
        khoaTable.setRowHeight(30);

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
                        .addComponent(searchTextField))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, searchTextField});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableModel.setRowCount(0);
        setDataTable(khoaService.getAllKhoas());
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        // TODO add your handling code here:
        String searchString = searchTextField.getText();
        searchKhoa(searchString);
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        maKhoaTextField.setText("");
        tenKhoaTextField.setText("");

        ngayThanhLapDateChooser.setCalendar(null);

        buttonGroup1.clearSelection();
        tenTruongKhoaTextField.setText("");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }//GEN-LAST:event_clearButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int row = khoaTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(KhoaView.this, "Vui lòng chọn Khoa muốn xóa!", "Lỗi chưa chọn đối tượng", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(KhoaView.this, "Bạn chắc chắn muốn xóa?");
            if (confirm == JOptionPane.YES_OPTION) {
                String maKhoa = (String) khoaTable.getValueAt(row, 1);
                khoaService.deleteKhoa(maKhoa);
                defaultTableModel.setRowCount(0);
                setDataTable(khoaService.getAllKhoas());
                clearButtonActionPerformed(evt);
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        khoaService = new KhoaService();
        Khoa khoa = new Khoa();
        khoa.setMaKhoa(maKhoaTextField.getText());
        khoa.setTenKhoa(tenKhoaTextField.getText());

        java.sql.Date ngayThanhLap = new java.sql.Date(ngayThanhLapDateChooser.getDate().getTime());
        khoa.setNgayThanhLap(ngayThanhLap);
        String valuemaTruongKhoa = maTruongKhoaComboBox.getItemAt(maTruongKhoaComboBox.getSelectedIndex());
        String findTruongKhoa = valuemaTruongKhoa.substring(0, 4);
        khoa.setTenTruongKhoa(tenTruongKhoaTextField.getText());
        if (valuemaTruongKhoa.equals(maTruongKhoaComboBox.getItemAt(0))) {
            khoa.setTrươngKhoa(null);
        } else {
            khoa.setTrươngKhoa(findTruongKhoa);
        }
        khoaService.addKhoa(khoa);
        defaultTableModel.setRowCount(0);
        setDataTable(khoaService.getAllKhoas());
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        khoaService = new KhoaService();
        Khoa khoa = new Khoa();

        khoa.setMaKhoa(maKhoaTextField.getText());
        khoa.setTenKhoa(tenKhoaTextField.getText());

        java.sql.Date ngayThanhLap = new java.sql.Date(ngayThanhLapDateChooser.getDate().getTime());
        khoa.setNgayThanhLap(ngayThanhLap);
//Tìm mã trưởng khoa
        String valuemaTruongKhoa = maTruongKhoaComboBox.getItemAt(maTruongKhoaComboBox.getSelectedIndex());
        String findTruongKhoa = valuemaTruongKhoa.substring(0, 4);
        if (valuemaTruongKhoa.equals(maTruongKhoaComboBox.getItemAt(0))) {
            khoa.setTrươngKhoa(null);
        } else {
            khoa.setTrươngKhoa(findTruongKhoa);
        }
        khoa.setTenTruongKhoa(tenTruongKhoaTextField.getText());
        khoaService.updateKhoa(khoa);
        defaultTableModel.setRowCount(0);
        setDataTable(khoaService.getAllKhoas());
    }//GEN-LAST:event_editButtonActionPerformed

    private void maTruongKhoaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maTruongKhoaComboBoxActionPerformed
        // TODO add your handling code here:
        GiaoVienService giaoVienService = new GiaoVienService();
        String valuemaTruongKhoa = maTruongKhoaComboBox.getItemAt(maTruongKhoaComboBox.getSelectedIndex());
        String findTruongKhoa = valuemaTruongKhoa.substring(0, 4);
        GiaoVien giaoVien = giaoVienService.searchGiaoVien(findTruongKhoa);
        if (giaoVien != null) {
            tenTruongKhoaTextField.setText(giaoVien.getHoTenGiaoVien());
        } else {
            tenTruongKhoaTextField.setText("");
        }
    }//GEN-LAST:event_maTruongKhoaComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public void maGiaoVienComboBox() {
        GiaoVienService giaoVienService = new GiaoVienService();
        ArrayList<GiaoVien> giaoViensArrayList = giaoVienService.getAllGiaoViens();
        Set<String> treeSetString = new TreeSet();
        for (int i = 0; i < giaoViensArrayList.size(); ++i) {
            treeSetString.add(giaoViensArrayList.get(i).getMaGiaoVien() + " - " + giaoViensArrayList.get(i).getHoTenGiaoVien());
        }
        treeSetString.add("Chưa có Trưởng Khoa.");
        DefaultComboBoxModel maGiaoVienBoxModel = new DefaultComboBoxModel();
        for (String element : treeSetString) {
            maGiaoVienBoxModel.addElement(element);
        }

        maTruongKhoaComboBox.setModel(maGiaoVienBoxModel);
    }

    // Bảng điểm Sinh viên
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
                java.util.logging.Logger.getLogger(KhoaView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(KhoaView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(KhoaView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(KhoaView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            //</editor-fold>
            /* Create and display the form */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new KhoaView().setVisible(true);
                }
            });

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(KhoaView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable khoaTable;
    private javax.swing.JTextField maKhoaTextField;
    private javax.swing.JComboBox<String> maTruongKhoaComboBox;
    private com.toedter.calendar.JDateChooser ngayThanhLapDateChooser;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTextField tenKhoaTextField;
    private javax.swing.JTextField tenTruongKhoaTextField;
    // End of variables declaration//GEN-END:variables
}
