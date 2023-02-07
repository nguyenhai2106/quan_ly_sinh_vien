/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.doanjava.view;

import com.doan.service.KetQuaThiService;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.doanjava.model.HocVien;
import java.sql.SQLException;
import com.doan.service.HocVienService;
import com.doanjava.model.BangDiem;
import com.doanjava.model.DiemThi;
import com.doanjava.model.KetQuaThi;
import com.doanjava.model.MonHoc;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Collections;
import javax.swing.table.JTableHeader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nguye
 */
public class KetQuaThiView extends javax.swing.JFrame {

    private String[] columnNames = new String[]{
        "STT", "Mã HV", "Họ Tên", "Mã MH", "Tên Môn Học", "Lần Thi", "Ngày Thi", "Điểm", "Kết Quả", "Mã Lớp", "Search"};
    HocVienService studentService;
    KetQuaThiService ketQuaThiService;
    DefaultTableModel defaultTableModel;
    DefaultTableModel defaultTableBangDiemModel;
    public int rowIndexCurrent;
    public int tangDan = 1;
    public int tangDanTen = 1;

    public void export(JTable table) throws FileNotFoundException, IOException {

        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(KetQuaThiView.this);
        String fileName = null;
        if (option == JFileChooser.APPROVE_OPTION) {
            String name = fileChooser.getSelectedFile().getName();
            String path = fileChooser.getSelectedFile().getParentFile().getPath();
            fileName = path + "\\" + name + ".xlsx";
        }

        TableModel tableModel = table.getModel();
        System.out.println(tableModel.getRowCount());
        ArrayList<DiemThi> diemThiArrayList = new ArrayList<>();
        ArrayList<KetQuaThi> ketQuaThiArrayList = new ArrayList<KetQuaThi>();

        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        XSSFSheet xSSFSheet = xSSFWorkbook.createSheet("Bảng Điểm");
        XSSFRow headerRow = xSSFSheet.createRow(0);
        headerRow.createCell(0).setCellValue("STT");
        headerRow.createCell(1).setCellValue("Mã HV");
        headerRow.createCell(2).setCellValue("Họ Tên");
        headerRow.createCell(3).setCellValue("Mã MH");
        headerRow.createCell(4).setCellValue("Tên Môn Học");
        headerRow.createCell(5).setCellValue("Lần Thi");
        headerRow.createCell(6).setCellValue("Ngày Thi");
        headerRow.createCell(7).setCellValue("Điểm");
        headerRow.createCell(8).setCellValue("Kết Quả");
        headerRow.createCell(9).setCellValue("Mã Lớp");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            XSSFRow row = xSSFSheet.createRow(i + 1);
            row.createCell(0).setCellValue(Integer.parseInt(tableModel.getValueAt(i, 0).toString()));
            row.createCell(1).setCellValue(tableModel.getValueAt(i, 1).toString());
            row.createCell(2).setCellValue(tableModel.getValueAt(i, 2).toString());
            row.createCell(3).setCellValue(tableModel.getValueAt(i, 3).toString());
            row.createCell(4).setCellValue(tableModel.getValueAt(i, 4).toString());
            row.createCell(5).setCellValue(Integer.parseInt(tableModel.getValueAt(i, 5).toString()));
            row.createCell(6).setCellValue(tableModel.getValueAt(i, 6).toString());
            row.createCell(7).setCellValue(Double.valueOf(tableModel.getValueAt(i, 7).toString()));
            row.createCell(8).setCellValue(tableModel.getValueAt(i, 8).toString());
            row.createCell(9).setCellValue(tableModel.getValueAt(i, 9).toString());
        }
        FileOutputStream fileOut = new FileOutputStream(fileName);
        xSSFWorkbook.write(fileOut);
        fileOut.close();
        xSSFWorkbook.close();
        JOptionPane.showMessageDialog(KetQuaThiView.this, "Đã in Kết Quả Thi", "Message", 1);
    }

    public void exportBangDiem(JTable table) {
        // TODO add your handling code here:
        String[] columnNamesPrint = new String[]{
            "STT", "Ma Mon Hoc", "Ten Mon Hoc", "So Tin Chi", "Diem"};
        String fileName = null;
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(KetQuaThiView.this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String name = fileChooser.getSelectedFile().getName();
            String path = fileChooser.getSelectedFile().getParentFile().getPath();
            fileName = path + "\\" + name + ".xls";
        }
        try {
            TableModel tableModel = table.getModel();
            ArrayList<DiemThi> diemThisArrayList = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); ++i) {
                DiemThi diemThi = new DiemThi();
                diemThi.setMaMonHoc(tableModel.getValueAt(i, 1).toString());
                diemThi.setTenMonHoc(tableModel.getValueAt(i, 2).toString());
                diemThi.setSoTinChi(Integer.parseInt(tableModel.getValueAt(i, 3).toString()));
                diemThi.setDiemThi(Double.parseDouble(tableModel.getValueAt(i, 4).toString()));
                diemThisArrayList.add(diemThi);
            }
            XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
            XSSFSheet xSSFSheet = xSSFWorkbook.createSheet("Bảng Điểm");
            xSSFSheet.setColumnWidth(0, 3 * 256);
            xSSFSheet.setColumnWidth(1, 12 * 256);
            xSSFSheet.setColumnWidth(2, 27 * 256);
            XSSFRow headerRow = xSSFSheet.createRow(0);
            headerRow.createCell(0).setCellValue("STT");
            headerRow.createCell(1).setCellValue("Mã Môn Học");
            headerRow.createCell(2).setCellValue("Tên Môn Học");
            headerRow.createCell(3).setCellValue("Số Tín Chỉ");
            headerRow.createCell(4).setCellValue("Điểm");

            for (int i = 1; i <= diemThisArrayList.size(); ++i) {
                XSSFRow xSSFRow = xSSFSheet.createRow(i);
                xSSFRow.createCell(0).setCellValue(i);
                xSSFRow.createCell(1).setCellValue(diemThisArrayList.get(i - 1).getMaMonHoc());
                xSSFRow.createCell(2).setCellValue(diemThisArrayList.get(i - 1).getTenMonHoc());
                xSSFRow.createCell(3).setCellValue(diemThisArrayList.get(i - 1).getSoTinChi());
                xSSFRow.createCell(4).setCellValue(diemThisArrayList.get(i - 1).getDiemThi());
            }
            XSSFRow enter = xSSFSheet.createRow(diemThisArrayList.size());
            XSSFRow footRow = xSSFSheet.createRow(diemThisArrayList.size() + 1);

            footRow.createCell(0).setCellValue("");
            footRow.createCell(1).setCellValue("Số TCTL");
            footRow.createCell(2).setCellValue(bd_soTinChiTextField.getText());
            footRow.createCell(3).setCellValue("Điểm TB");
            footRow.createCell(4).setCellValue(bd_diemTBTextField.getText());

            FileOutputStream fileOut = new FileOutputStream(fileName);
            xSSFWorkbook.write(fileOut);
            fileOut.close();
            xSSFWorkbook.close();
            JOptionPane.showMessageDialog(KetQuaThiView.this, "Đã in Bảng điểm của học viên " + bd_hoTenTextField.getText(), "Message", 1);
        } catch (IOException ex) {
            Logger.getLogger(KetQuaThiView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chooseKetQuaThiTable() {
        ListSelectionModel listSelectionModel = ketQuaThiTable.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = ketQuaThiTable.getSelectedRow();
                if (row >= 0) {
                    String maHocVien = String.valueOf(ketQuaThiTable.getValueAt(row, 1));
                    maHocVienTextField.setText(maHocVien);
                    bd_maHocVienTextField.setText(maHocVien);

                    String maLop = String.valueOf(ketQuaThiTable.getValueAt(row, 9));
                    maLopTextField.setText(maLop);
                    bd_maLopTextField.setText(maLop);

                    String hoTenHocVien = String.valueOf(ketQuaThiTable.getValueAt(row, 2));
                    hoTenHocVienTextField.setText(hoTenHocVien);
                    bd_hoTenTextField.setText(hoTenHocVien);

                    String maMonHoc = String.valueOf(ketQuaThiTable.getValueAt(row, 3));
                    maMonHocTextField.setText(maMonHoc);

                    String tenMonHoc = String.valueOf(ketQuaThiTable.getValueAt(row, 4));
                    tenMonHocTextField.setText(tenMonHoc);

                    String lanThi = String.valueOf(ketQuaThiTable.getValueAt(row, 5));
                    lanThiSpinner.setValue(Integer.valueOf(lanThi));

                    java.util.Date ngayThi;
                    try {
                        ngayThi = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(ketQuaThiTable.getValueAt(row, 6)));
                        ngayThiDateChooser.setDate(ngayThi);

                    } catch (ParseException ex) {
                        Logger.getLogger(KetQuaThiView.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                    String diemThi = String.valueOf(ketQuaThiTable.getValueAt(row, 7));
                    diemThiTextField.setText(diemThi);

                    String ketQua = Double.valueOf(diemThi) >= 5 ? "Dat" : "Khong Dat";
                    ketQuaTextField.setText(ketQua);

                    bd_maLopTextField.setText(maLop);
                    // enable Edit and Delete buttons
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    ketQuaTextField.setEnabled(false);
                    hoTenHocVienTextField.setEnabled(false);
                    tenMonHocTextField.setEnabled(false);
                    renderBangDiem(maHocVien);
                    tongSoTinChi();
                }
            }
        });
    }

    /**
     * Creates new form QLGVView
     */
//    Hàm search
    public void searchKetQuaThi(String str) {
        DefaultTableModel tableModel = (DefaultTableModel) ketQuaThiTable.getModel();
        TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(tableModel);
        ketQuaThiTable.setRowSorter(tableRowSorter);
        tableRowSorter.setRowFilter(RowFilter.regexFilter(str));
    }

    public void multiSortKetQuaThi(String str, String str2) {
        DefaultTableModel tableModel = (DefaultTableModel) ketQuaThiTable.getModel();
        TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(tableModel);
        ketQuaThiTable.setRowSorter(tableRowSorter);
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
        filters.add(RowFilter.regexFilter(str, 0));
        filters.add(RowFilter.regexFilter(str2, 1));
        RowFilter rf = RowFilter.andFilter(filters);
        tableRowSorter.setRowFilter(rf);
    }

    public void setDataTable(ArrayList<KetQuaThi> ketQuaThisArrayList) {
        int index = 0;
        for (KetQuaThi ketQuaThi : ketQuaThisArrayList) {
            defaultTableModel.addRow(new Object[]{
                index + 1, ketQuaThi.getMaHocVien(), ketQuaThi.getHoTenHocVien(), ketQuaThi.getMaMonHoc(), ketQuaThi.getTenMonHoc(), ketQuaThi.getLanThi(), ketQuaThi.getNgayThi(), ketQuaThi.getDiemThi(), ketQuaThi.getKetQua(), ketQuaThi.getMaLop(), ketQuaThi.getMaMonHoc() + ketQuaThi.getMaLop() + ketQuaThi.getNgayThi() + ketQuaThi.getMaMonHoc() + ketQuaThi.getNgayThi()});
            index++;
        }
    }

    public void setDataBangDiemTable(ArrayList<BangDiem> bangDiemsArrayList) {
        int index = 0;
        for (BangDiem bangDiem : bangDiemsArrayList) {
            defaultTableBangDiemModel.addRow(new Object[]{
                index + 1, bangDiem.getMaMonHoc(), bangDiem.getTenMonHoc(), (byte) bangDiem.getTinhChiLT() + (byte) bangDiem.getTinChiTH(), bangDiem.getDiemThi()});
            index++;
        }
    }

    public KetQuaThiView() {
        initComponents();
        chooseKetQuaThiTable();
        maLopComboBox();
        maMonHocComboBox();
        setResizable(false);
        ketQuaThiService = new KetQuaThiService();
        defaultTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        ketQuaThiTable.setModel(defaultTableModel);
        ArrayList<KetQuaThi> ketQuaThisArrayList = ketQuaThiService.getAllKetQuaThis();
        setDataTable(ketQuaThisArrayList);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = ketQuaThiTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(50);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(130);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);

        columnModel.getColumn(4).setPreferredWidth(180);
        columnModel.getColumn(5).setPreferredWidth(40);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);
        columnModel.getColumn(7).setCellRenderer(centerRenderer);
        columnModel.getColumn(8).setCellRenderer(centerRenderer);
        columnModel.getColumn(9).setPreferredWidth(40);
        columnModel.getColumn(9).setCellRenderer(centerRenderer);

        columnModel.getColumn(10).setMaxWidth(0);
        columnModel.getColumn(10).setMinWidth(0);
        columnModel.getColumn(10).setPreferredWidth(0);

        JTableHeader tableHeader = ketQuaThiTable.getTableHeader();
        JTableHeader tableHeaderBD = bangDiemTable.getTableHeader();
        Font headerFont = new Font("Segoe UI", Font.BOLD, 13);
        tableHeader.setFont(headerFont);
        tableHeaderBD.setFont(headerFont);

        ketQuaTextField.setEnabled(false);
        hoTenHocVienTextField.setEnabled(false);
        tenMonHocTextField.setEnabled(false);
        maLopTextField.setEnabled(false);
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
        maHocVienTextField = new javax.swing.JTextField();
        hoTenHocVienTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        maMonHocTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        diemThiTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ketQuaTextField = new javax.swing.JTextField();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        tenMonHocTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        maLopTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        lanThiSpinner = new javax.swing.JSpinner();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        kiemTraDiemThiLabel = new javax.swing.JLabel();
        ngayThiDateChooser = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        bangDiemTable = new javax.swing.JTable();
        bd_maHocVienLabel = new javax.swing.JLabel();
        bd_maHocVienTextField = new javax.swing.JTextField();
        bd_hoTenLabel = new javax.swing.JLabel();
        bd_hoTenTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        bd_maLopTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        bd_soTinChiTextField = new javax.swing.JTextField();
        diemTBLabel = new javax.swing.JLabel();
        bd_diemTBTextField = new javax.swing.JTextField();
        bd_refreshButton = new javax.swing.JButton();
        bd_inBangDiemButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        in_maMonHocTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        in_tenMonHocTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        in_maLopTextField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        in_xemButton = new javax.swing.JButton();
        in_inButton = new javax.swing.JButton();
        in_ngayThiDateChooser = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        ketQuaThiTable = new javax.swing.JTable();
        refreshButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        maLopComboBox = new javax.swing.JComboBox<>();
        xuatKetQuaThiVienButton = new javax.swing.JButton();
        maMonHocComboBox = new javax.swing.JComboBox<>();
        sortTheoDiem = new javax.swing.JButton();
        sortTheoTen = new javax.swing.JButton();
        nhapKetQuaImport = new javax.swing.JButton();
        ngayThiFilterDateChooser = new com.toedter.calendar.JDateChooser();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUẢN LÝ KẾT QUẢ THI");

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jPanel2.setPreferredSize(new java.awt.Dimension(450, 401));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Mã Học Viên");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Họ và Tên");

        maHocVienTextField.setPreferredSize(new java.awt.Dimension(50, 22));
        maHocVienTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                maHocVienTextFieldKeyReleased(evt);
            }
        });

        hoTenHocVienTextField.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        hoTenHocVienTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Mã Môn Học");

        maMonHocTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                maMonHocTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Ngày Thi");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Điểm Thi");

        diemThiTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                diemThiTextFieldKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Kết Quả");

        ketQuaTextField.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        ketQuaTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));

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

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Tên Môn Học");

        tenMonHocTextField.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tenMonHocTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Mã Lớp");

        maLopTextField.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        maLopTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Lần Thi");

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        kiemTraDiemThiLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        kiemTraDiemThiLabel.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(maHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ketQuaTextField)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(addButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(editButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(deleteButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(clearButton))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(diemThiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(backButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(nextButton)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(kiemTraDiemThiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(3, 3, 3)))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(maMonHocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tenMonHocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(hoTenHocVienTextField)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(ngayThiDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lanThiSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, 0))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel4, jLabel5, jLabel7});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, deleteButton, editButton});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(maHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(hoTenHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maMonHocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(tenMonHocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lanThiSpinner)
                    .addComponent(ngayThiDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(diemThiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton)
                    .addComponent(nextButton))
                .addGap(0, 0, 0)
                .addComponent(kiemTraDiemThiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ketQuaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton)
                    .addComponent(clearButton)
                    .addComponent(deleteButton))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {backButton, diemThiTextField, hoTenHocVienTextField, jLabel1, jLabel12, jLabel2, jLabel4, ketQuaTextField, maHocVienTextField, maLopTextField, nextButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel11, jLabel3, maMonHocTextField, tenMonHocTextField});

        lanThiSpinner.setValue(1);

        jTabbedPane1.addTab("Thông Tin", jPanel2);

        bangDiemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã MH", "Tên Môn Học", "Số Tín Chỉ", "Điểm"
            }
        ));
        bangDiemTable.setShowGrid(true);
        bangDiemTable.setRowHeight(30);
        jScrollPane2.setViewportView(bangDiemTable);
        if (bangDiemTable.getColumnModel().getColumnCount() > 0) {
            bangDiemTable.getColumnModel().getColumn(0).setPreferredWidth(20);
            bangDiemTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            bangDiemTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        bd_maHocVienLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bd_maHocVienLabel.setText("Mã Học Viên");
        bd_maHocVienLabel.setMaximumSize(new java.awt.Dimension(72, 30));
        bd_maHocVienLabel.setMinimumSize(new java.awt.Dimension(72, 30));
        bd_maHocVienLabel.setPreferredSize(new java.awt.Dimension(72, 30));

        bd_maHocVienTextField.setEditable(false);

        bd_hoTenLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bd_hoTenLabel.setText("Họ và Tên");

        bd_hoTenTextField.setEditable(false);

        jLabel9.setText("Mã Lớp");

        bd_maLopTextField.setEditable(false);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Số TCTL");

        bd_soTinChiTextField.setEditable(false);

        diemTBLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        diemTBLabel.setText("Điểm TB");

        bd_diemTBTextField.setEditable(false);

        bd_refreshButton.setText("Refresh");
        bd_refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bd_refreshButtonActionPerformed(evt);
            }
        });

        bd_inBangDiemButton.setText("In Bảng Điểm");
        bd_inBangDiemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bd_inBangDiemButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(bd_maHocVienLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bd_maHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(bd_hoTenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bd_hoTenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bd_maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bd_soTinChiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(diemTBLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bd_diemTBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(bd_refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bd_inBangDiemButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bd_hoTenLabel, bd_maHocVienLabel, bd_maHocVienTextField, diemTBLabel, jLabel10});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bd_diemTBTextField, bd_soTinChiTextField});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bd_maHocVienLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bd_maHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bd_hoTenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bd_hoTenLabel)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bd_maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bd_soTinChiTextField)
                        .addComponent(diemTBLabel)
                        .addComponent(bd_diemTBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bd_refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bd_inBangDiemButton)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bd_diemTBTextField, bd_hoTenLabel, bd_hoTenTextField, bd_maHocVienLabel, bd_maHocVienTextField, bd_maLopTextField, bd_soTinChiTextField, diemTBLabel, jLabel10});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bd_inBangDiemButton, bd_refreshButton});

        jTabbedPane1.addTab("Bảng Điểm", jPanel1);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Mã MH");
        jLabel6.setPreferredSize(new java.awt.Dimension(40, 30));

        in_maMonHocTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                in_maMonHocTextFieldKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Tên MH");

        in_tenMonHocTextField.setEditable(false);
        in_tenMonHocTextField.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Mã Lớp");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Ngày Thi");

        in_xemButton.setText("Xem");
        in_xemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                in_xemButtonActionPerformed(evt);
            }
        });

        in_inButton.setText("In");
        in_inButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                in_inButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(in_xemButton)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(in_maMonHocTextField))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(in_maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14))))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(in_tenMonHocTextField)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(in_inButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(in_ngayThiDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(in_maMonHocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(in_tenMonHocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(in_maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(in_ngayThiDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(in_xemButton)
                    .addComponent(in_inButton))
                .addContainerGap(275, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {in_inButton, in_maLopTextField, in_maMonHocTextField, in_tenMonHocTextField, in_xemButton, jLabel14, jLabel15, jLabel16, jLabel6});

        jTabbedPane1.addTab("In Kết Quả Thi", jPanel3);

        ketQuaThiTable.setAutoCreateRowSorter(true);
        ketQuaThiTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        ketQuaThiTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        ketQuaThiTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        ketQuaThiTable.setShowGrid(true);
        jScrollPane1.setViewportView(ketQuaThiTable);
        ketQuaThiTable.setRowHeight(30);

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

        maLopComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maLopComboBoxActionPerformed(evt);
            }
        });

        xuatKetQuaThiVienButton.setText("Xuất Kết Quả Thi");
        xuatKetQuaThiVienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xuatKetQuaThiVienButtonActionPerformed(evt);
            }
        });

        maMonHocComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maMonHocComboBoxActionPerformed(evt);
            }
        });

        sortTheoDiem.setText("Sắp Xếp Theo Điểm");
        sortTheoDiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sortTheoDiemMouseClicked(evt);
            }
        });

        sortTheoTen.setText("Sắp Xếp Theo Tên");
        sortTheoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortTheoTenActionPerformed(evt);
            }
        });

        nhapKetQuaImport.setText("Nhập Kết Quả Thi");
        nhapKetQuaImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhapKetQuaImportActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshButton)
                        .addGap(18, 18, 18)
                        .addComponent(xuatKetQuaThiVienButton)
                        .addGap(26, 26, 26)
                        .addComponent(sortTheoDiem)
                        .addGap(18, 18, 18)
                        .addComponent(sortTheoTen)
                        .addGap(18, 18, 18)
                        .addComponent(nhapKetQuaImport))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTextField)
                        .addGap(18, 18, 18)
                        .addComponent(maMonHocComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(maLopComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ngayThiFilterDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {maLopComboBox, maMonHocComboBox});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nhapKetQuaImport, sortTheoTen});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maLopComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maMonHocComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ngayThiFilterDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(xuatKetQuaThiVienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sortTheoDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sortTheoTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhapKetQuaImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, maLopComboBox, maMonHocComboBox, searchTextField});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableModel.setRowCount(0);
        setDataTable(ketQuaThiService.getAllKetQuaThis());
    }//GEN-LAST:event_refreshButtonActionPerformed

    private boolean validateMaMonHoc() {
        String maMonHoc = maMonHocTextField.getText();
        if (maMonHoc == null || "".equals(maMonHoc.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateDiemThi() {
        String diemThi = diemThiTextField.getText();
        if (diemThiTextField.getText() == null || "".equals(diemThi.trim())) {
            return false;
        } else {
            Double diemThiDouble = Double.valueOf(diemThiTextField.getText());
            if (diemThiDouble < 0 || diemThiDouble > 10) {
                return false;
            }
        }
        return true;
    }

    private boolean validateMaHocVien() {
        String maHocVien = maHocVienTextField.getText();
        if (maHocVien == null || "".equals(maHocVien.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateLanThi() {
        int lanThi = (int) lanThiSpinner.getValue();
        if (lanThi < 1 || lanThiSpinner.getValue() == null) {
            return false;
        }
        return true;
    }

    private boolean validateNgayThi() {
        if (ngayThiDateChooser.getDate() == null) {
            return false;
        }
        return true;
    }

    private boolean validationKetQuaThi() {
        return validateMaHocVien() && validateMaMonHoc() && validateNgayThi() && validateLanThi() && validateDiemThi();
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        ketQuaThiService = new KetQuaThiService();
        KetQuaThi ketQuaThi = new KetQuaThi();
        if (validationKetQuaThi()) {
            ketQuaThi.setMaHocVien(maHocVienTextField.getText());
            ketQuaThi.setHoTenHocVien(hoTenHocVienTextField.getText());
            ketQuaThi.setMaLop(maLopTextField.getText());
            ketQuaThi.setMaMonHoc(maMonHocTextField.getText());
            ketQuaThi.setTenMonHoc(tenMonHocTextField.getText());
            ketQuaThi.setLanThi((int) lanThiSpinner.getValue());
            java.sql.Date ngayThi = new java.sql.Date(ngayThiDateChooser.getDate().getTime());
            ketQuaThi.setNgayThi(ngayThi);
            ketQuaThi.setDiemThi(Double.valueOf(diemThiTextField.getText()));
            ketQuaThi.setKetQua(ketQuaTextField.getText());
        } else {
            JOptionPane.showMessageDialog(KetQuaThiView.this, "Vui lòng kiểm tra lại các thông tin đã nhập!", "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (ketQuaThiService.CheckAddSinhVien(ketQuaThi)) {
            ketQuaThiService.addKetQuaThi(ketQuaThi);
            defaultTableModel.setRowCount(0);
            setDataTable(ketQuaThiService.getAllKetQuaThis());
        } else {
            JOptionPane.showMessageDialog(KetQuaThiView.this, "Kết Quả Thi đã tồn tại trên hệ thống. Vui lòng kiểm tra lại!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int row = ketQuaThiTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(KetQuaThiView.this, "Vui lòng chọn Học Viên muốn xóa!", "Lỗi chưa chọn đối tượng", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(KetQuaThiView.this, "Bạn chắc chắn muốn xóa?");
            if (confirm == JOptionPane.YES_OPTION) {
                String maHocVien = (String) ketQuaThiTable.getValueAt(row, 1);
                String maMonHoc = (String) ketQuaThiTable.getValueAt(row, 3);
                int lanThi = (int) ketQuaThiTable.getValueAt(row, 5);
                ketQuaThiService.deleteKetQuaThi(maHocVien, maMonHoc, lanThi);
                defaultTableModel.setRowCount(0);
                setDataTable(ketQuaThiService.getAllKetQuaThis());
                clearButtonActionPerformed(evt);
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        maHocVienTextField.setText("");
        hoTenHocVienTextField.setText("");
        maMonHocTextField.setText("");
        tenMonHocTextField.setText("");
        ngayThiDateChooser.setCalendar(null);
        lanThiSpinner.setValue(1);
        diemThiTextField.setText("");
        ketQuaTextField.setText("");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }//GEN-LAST:event_clearButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        rowIndexCurrent = ketQuaThiTable.getSelectedRow();
        ketQuaThiService = new KetQuaThiService();
        KetQuaThi ketQuaThi = new KetQuaThi();

        if (validationKetQuaThi()) {
            ketQuaThi.setMaHocVien(maHocVienTextField.getText());
            ketQuaThi.setHoTenHocVien(hoTenHocVienTextField.getText());
            ketQuaThi.setMaLop(maLopTextField.getText());
            ketQuaThi.setMaMonHoc(maMonHocTextField.getText());
            ketQuaThi.setTenMonHoc(tenMonHocTextField.getText());
            ketQuaThi.setLanThi((int) lanThiSpinner.getValue());
            java.sql.Date ngayThi = new java.sql.Date(ngayThiDateChooser.getDate().getTime());
            ketQuaThi.setNgayThi(ngayThi);
            ketQuaThi.setDiemThi(Double.valueOf(diemThiTextField.getText()));
            ketQuaThi.setKetQua(ketQuaTextField.getText());
            ketQuaThiService.updateKetQuaThi(ketQuaThi);
            defaultTableModel.setRowCount(0);
            setDataTable(ketQuaThiService.getAllKetQuaThis());
            ketQuaThiTable.setRowSelectionInterval(rowIndexCurrent, rowIndexCurrent);
        } else {
            JOptionPane.showMessageDialog(KetQuaThiView.this, "Vui lòng kiểm tra lại các thông tin đã nhập!", "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void maLopComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maLopComboBoxActionPerformed
//        TODO add your handling code here
        if (ngayThiFilterDateChooser.getDate() != null) {
            String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
            String valueMaMonHoc = maMonHocComboBox.getItemAt(maMonHocComboBox.getSelectedIndex());
            String ngayThi = new SimpleDateFormat("yyyy-MM-dd").format(ngayThiFilterDateChooser.getDate().getTime());
            String searchValue = valueMaMonHoc + valueMaLop;
            if (searchValue.equals("AllAll")) {
                searchKetQuaThi("" + ngayThi);
            } else if (searchValue.equals("All" + valueMaLop)) {
                searchKetQuaThi(valueMaLop + ngayThi);
            } else if (searchValue.equals(valueMaMonHoc + "All")) {
                searchKetQuaThi(valueMaMonHoc);
            } else {
                searchKetQuaThi(searchValue + ngayThi);
            }
        } else {
            String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
            String valueMaMonHoc = maMonHocComboBox.getItemAt(maMonHocComboBox.getSelectedIndex());
            String searchValue = valueMaMonHoc + valueMaLop;
            String valueNgayThi = null;
            if (ngayThiFilterDateChooser.getDate() != null) {
                valueNgayThi = new SimpleDateFormat("yyyy-MM-dd").format(ngayThiFilterDateChooser.getDate().getTime());
                System.out.println(valueNgayThi);
            }
            if (searchValue.equals("AllAll")) {
                searchKetQuaThi("");
            } else if (searchValue.equals("All" + valueMaLop)) {
                searchKetQuaThi(valueMaLop);
            } else if (searchValue.equals(valueMaMonHoc + "All")) {
                searchKetQuaThi(valueMaMonHoc);
            } else {
                searchKetQuaThi(searchValue);
            }
        }
    }//GEN-LAST:event_maLopComboBoxActionPerformed
//Hàm search
    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        // TODO add your handling code here:
        String searchString = searchTextField.getText();
        searchKetQuaThi(searchString);
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void bd_refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bd_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableBangDiemModel.setRowCount(0);
        if (bd_maHocVienTextField.getText() != "") {
            setDataBangDiemTable(studentService.getBangDiem(bd_maHocVienTextField.getText()));
        }
    }//GEN-LAST:event_bd_refreshButtonActionPerformed

    private void xuatKetQuaThiVienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xuatKetQuaThiVienButtonActionPerformed
        try {
            export(ketQuaThiTable);
        } catch (IOException ex) {
            Logger.getLogger(KetQuaThiView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_xuatKetQuaThiVienButtonActionPerformed

    private void bd_inBangDiemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bd_inBangDiemButtonActionPerformed
        // TODO add your handling code here:
        exportBangDiem(bangDiemTable);

    }//GEN-LAST:event_bd_inBangDiemButtonActionPerformed

    private void diemThiTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diemThiTextFieldKeyReleased
        // TODO add your handling code here:
        String diemThi = diemThiTextField.getText();
        if (!"".equals(diemThi)) {
            String ketQua = Double.valueOf(diemThi) >= 5 ? "Dat" : "Khong Dat";
            ketQuaTextField.setText(ketQua);
            Double diemThiDouble = Double.valueOf(diemThiTextField.getText());
            if (diemThiDouble < 0 || diemThiDouble > 10) {
                kiemTraDiemThiLabel.setForeground(Color.red);
                kiemTraDiemThiLabel.setText("Điểm Thi không hợp lệ!");
            } else {
                kiemTraDiemThiLabel.setText("");
            }
        } else {
            ketQuaTextField.setText("");
        }
    }//GEN-LAST:event_diemThiTextFieldKeyReleased

    private void maHocVienTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maHocVienTextFieldKeyReleased
        // TODO add your handling code here:
        String maHoCVien = maHocVienTextField.getText();
        if (!"".equals(maHoCVien)) {
            HocVien student = ketQuaThiService.searchTenHocVien(maHoCVien);
            if (student != null) {
                hoTenHocVienTextField.setText(student.getHoHocVien() + " " + student.getTenHocVien());
                maLopTextField.setText(student.getMaLop());
            } else {
                hoTenHocVienTextField.setForeground(Color.red);
                hoTenHocVienTextField.setText("Không tìm thấy Học Viên!");
                maLopTextField.setText("");
            }
        } else {
            hoTenHocVienTextField.setText("");
            maLopTextField.setText("");
        }
    }//GEN-LAST:event_maHocVienTextFieldKeyReleased

    private void maMonHocTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maMonHocTextFieldKeyReleased
        // TODO add your handling code here:
        String maMonHoc = maMonHocTextField.getText();
        if (!"".equals(maMonHoc)) {
            MonHoc monHoc = ketQuaThiService.searchTenMonHoc(maMonHoc);
            if (monHoc != null) {
                tenMonHocTextField.setText(monHoc.getTenMonHoc());
            } else {
                tenMonHocTextField.setForeground(Color.red);
                tenMonHocTextField.setText("Không tìm thấy Môn Học!");
            }
        } else {
            tenMonHocTextField.setText("");
        }
    }//GEN-LAST:event_maMonHocTextFieldKeyReleased

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        int rowIndex = ketQuaThiTable.getSelectedRow();
        if (rowIndex != -1) {
            if (rowIndex > 0) {
                rowIndex--;
                ketQuaThiTable.changeSelection(rowIndex, rowIndex, rootPaneCheckingEnabled, rootPaneCheckingEnabled);
            } else {
                ketQuaThiTable.changeSelection(0, rowIndex, rootPaneCheckingEnabled, rootPaneCheckingEnabled);
            }
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        // TODO add your handling code here:
        int rowIndex = ketQuaThiTable.getSelectedRow();
        if (rowIndex != -1) {
            if (rowIndex < ketQuaThiTable.getRowCount() - 1) {
                rowIndex++;
                ketQuaThiTable.changeSelection(rowIndex, rowIndex, rootPaneCheckingEnabled, rootPaneCheckingEnabled);
            } else {
                ketQuaThiTable.changeSelection(0, rowIndex, rootPaneCheckingEnabled, rootPaneCheckingEnabled);
            }
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void ngayThiFilterDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ngayThiFilterDateChooserPropertyChange
        // TODO add your handling code here:
        if (ngayThiFilterDateChooser.getDate() != null) {
            String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
            String valueMaMonHoc = maMonHocComboBox.getItemAt(maMonHocComboBox.getSelectedIndex());
            String ngayThi = new SimpleDateFormat("yyyy-MM-dd").format(ngayThiFilterDateChooser.getDate().getTime());
            String searchValue = valueMaMonHoc + valueMaLop;
            if (searchValue.equals("AllAll")) {
                searchKetQuaThi("" + ngayThi);
            } else if (searchValue.equals("All" + valueMaLop)) {
                searchKetQuaThi(valueMaLop + ngayThi);
            } else if (searchValue.equals(valueMaMonHoc + "All")) {
                searchKetQuaThi(valueMaMonHoc);
            } else {
                searchKetQuaThi(searchValue + ngayThi);
            }
        } else {
            String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
            String valueMaMonHoc = maMonHocComboBox.getItemAt(maMonHocComboBox.getSelectedIndex());
            String searchValue = valueMaMonHoc + valueMaLop;
            String valueNgayThi = null;
            if (ngayThiFilterDateChooser.getDate() != null) {
                valueNgayThi = new SimpleDateFormat("yyyy-MM-dd").format(ngayThiFilterDateChooser.getDate().getTime());
                System.out.println(valueNgayThi);
            }
            if (searchValue.equals("AllAll")) {
                searchKetQuaThi("");
            } else if (searchValue.equals("All" + valueMaLop)) {
                searchKetQuaThi(valueMaLop);
            } else if (searchValue.equals(valueMaMonHoc + "All")) {
                searchKetQuaThi(valueMaMonHoc);
            } else {
                searchKetQuaThi(searchValue);
            }
        }
    }//GEN-LAST:event_ngayThiFilterDateChooserPropertyChange

    private void maMonHocComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maMonHocComboBoxActionPerformed
        if (ngayThiFilterDateChooser.getDate() != null) {
            String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
            String valueMaMonHoc = maMonHocComboBox.getItemAt(maMonHocComboBox.getSelectedIndex());
            String ngayThi = new SimpleDateFormat("yyyy-MM-dd").format(ngayThiFilterDateChooser.getDate().getTime());
            String searchValue = valueMaMonHoc + valueMaLop;
            if (searchValue.equals("AllAll")) {
                searchKetQuaThi("" + ngayThi);
            } else if (searchValue.equals("All" + valueMaLop)) {
                searchKetQuaThi(valueMaLop + ngayThi);
            } else if (searchValue.equals(valueMaMonHoc + "All")) {
                searchKetQuaThi(valueMaMonHoc);
            } else {
                searchKetQuaThi(searchValue + ngayThi);
            }
        } else {
            String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
            String valueMaMonHoc = maMonHocComboBox.getItemAt(maMonHocComboBox.getSelectedIndex());
            String searchValue = valueMaMonHoc + valueMaLop;
            String valueNgayThi = null;
            if (ngayThiFilterDateChooser.getDate() != null) {
                valueNgayThi = new SimpleDateFormat("yyyy-MM-dd").format(ngayThiFilterDateChooser.getDate().getTime());
                System.out.println(valueNgayThi);
            }
            if (searchValue.equals("AllAll")) {
                searchKetQuaThi("");
            } else if (searchValue.equals("All" + valueMaLop)) {
                searchKetQuaThi(valueMaLop);
            } else if (searchValue.equals(valueMaMonHoc + "All")) {
                searchKetQuaThi(valueMaMonHoc);
            } else {
                searchKetQuaThi(searchValue);
            }
        }
    }//GEN-LAST:event_maMonHocComboBoxActionPerformed

    private void sortTheoDiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sortTheoDiemMouseClicked
        // TODO add your handling code here:
        ArrayList<KetQuaThi> ketQuaThis = ketQuaThiService.getAllKetQuaThis();
        Collections.sort(ketQuaThis);
        defaultTableModel.setRowCount(0);
        if (tangDan == 1) {
            Collections.sort(ketQuaThis);
            setDataTable(ketQuaThis);
            tangDan = 0;
        } else {
            Collections.reverse(ketQuaThis);
            setDataTable(ketQuaThis);
            tangDan = 1;
        }

    }//GEN-LAST:event_sortTheoDiemMouseClicked

    private void sortTheoTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortTheoTenActionPerformed
        // TODO add your handling code here:
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(ketQuaThiTable.getModel());
        ketQuaThiTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        int columnIndexToSort = 1;
        if (tangDanTen == 1) {
            sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
            tangDanTen = 0;
        } else {
            sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));
            tangDanTen = 1;
        }
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }//GEN-LAST:event_sortTheoTenActionPerformed

    private void in_maMonHocTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_in_maMonHocTextFieldKeyReleased
        // TODO add your handling code here:
        String maMonHoc = in_maMonHocTextField.getText();
        if (!"".equals(maMonHoc)) {
            MonHoc monHoc = ketQuaThiService.searchTenMonHoc(maMonHoc);
            if (monHoc != null) {
                in_tenMonHocTextField.setText(monHoc.getTenMonHoc());
            } else {
                in_tenMonHocTextField.setText("Không tìm thấy Môn Học!");
            }
        } else {
            in_tenMonHocTextField.setText("");
        }
    }//GEN-LAST:event_in_maMonHocTextFieldKeyReleased

    private void in_xemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_in_xemButtonActionPerformed
        // TODO add your handling code here:
        ketQuaThiService = new KetQuaThiService();
        String maMonHoc = in_maMonHocTextField.getText();
        String maLop = in_maLopTextField.getText();
        java.sql.Date ngayThi = new java.sql.Date(in_ngayThiDateChooser.getDate().getTime());
        System.out.println(ngayThi);
        defaultTableModel.setRowCount(0);
        setDataTable(ketQuaThiService.filterDeIn(maMonHoc, maLop, ngayThi));

    }//GEN-LAST:event_in_xemButtonActionPerformed

    private void in_inButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_in_inButtonActionPerformed
        // TODO add your handling code here:
        try {
            export(ketQuaThiTable);
        } catch (IOException ex) {
            Logger.getLogger(KetQuaThiView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_in_inButtonActionPerformed

    private void nhapKetQuaImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhapKetQuaImportActionPerformed
        // TODO add your handling code here:
        File excelFile;
        FileInputStream fin = null;
        BufferedInputStream excelBIS = null;
        JFileChooser excelFileChooser = new JFileChooser();
        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            excelFile = excelFileChooser.getSelectedFile();
            try {
                fin = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(fin);
                try {
                    XSSFWorkbook excelJTableImport = new XSSFWorkbook(excelBIS);
                    XSSFSheet excelFSheet = excelJTableImport.getSheetAt(0);
                    for (int row = 1; row <= excelFSheet.getLastRowNum(); row++) {
                        XSSFRow excelFRow = excelFSheet.getRow(row);

                        ArrayList<KetQuaThi> ketquaThiArrayList = new ArrayList<KetQuaThi>();

                        DataFormatter formatter = new DataFormatter();

                        String maHocVien = formatter.formatCellValue(excelFSheet.getRow(row).getCell(1));
                        String hoTenHocVien = formatter.formatCellValue(excelFSheet.getRow(row).getCell(2));
                        String maMonHoc = formatter.formatCellValue(excelFSheet.getRow(row).getCell(3));
                        String tenMonHoc = formatter.formatCellValue(excelFSheet.getRow(row).getCell(4));
                        String lanThi = formatter.formatCellValue(excelFSheet.getRow(row).getCell(5));
                        String ngayThi = formatter.formatCellValue(excelFSheet.getRow(row).getCell(6));

                        java.util.Date ngayThiDate = null;
                        java.sql.Date sqlStartDate = null;

                        try {
                            ngayThiDate = new SimpleDateFormat("yyyy-MM-dd").parse(ngayThi);
                            sqlStartDate = new java.sql.Date(ngayThiDate.getTime());

                        } catch (ParseException ex) {
                            Logger.getLogger(KetQuaThiView.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }

                        String diemThi = formatter.formatCellValue(excelFSheet.getRow(row).getCell(7));
                        String ketQua = Double.parseDouble(diemThi) >= 5 ? "Đạt" : "Không Đạt";
                        String maLop = formatter.formatCellValue(excelFSheet.getRow(row).getCell(9));

                        KetQuaThi ketquathi = new KetQuaThi(maHocVien, maMonHoc, Integer.parseInt(lanThi), sqlStartDate, Double.parseDouble(diemThi), ketQua);
                        ketQuaThiService.addKetQuaThi(ketquathi);
                    }
                    JOptionPane.showMessageDialog(KetQuaThiView.this, "Đã nhập dữ liệu vào Kết quả thi. Nhấn Refresh để kiểm tra dữ liệu.");
                } catch (IOException ex) {
                    Logger.getLogger(KetQuaThiView.class
                            .getName()).log(Level.SEVERE, null, ex);

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(KetQuaThiView.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_nhapKetQuaImportActionPerformed

    /**
     * @param args the command line arguments
     */
    public void maLopComboBox() {
        ketQuaThiService = new KetQuaThiService();
        ArrayList<KetQuaThi> ketQuaThisArrayList = ketQuaThiService.getAllKetQuaThis();
        Set<String> treeSetString = new TreeSet();
        treeSetString.add("All");
        for (int i = 0; i < ketQuaThisArrayList.size(); ++i) {
            treeSetString.add(ketQuaThisArrayList.get(i).getMaLop());
        }
        DefaultComboBoxModel maLopBoxModel = new DefaultComboBoxModel();
        for (String element : treeSetString) {
            maLopBoxModel.addElement(element);
        }
        maLopComboBox.setModel(maLopBoxModel);
    }

    public void maMonHocComboBox() {
        ketQuaThiService = new KetQuaThiService();
        ArrayList<KetQuaThi> ketQuaThisArrayList = ketQuaThiService.getAllKetQuaThis();
        Set<String> treeSetString = new TreeSet();
        treeSetString.add("All");
        for (int i = 0; i < ketQuaThisArrayList.size(); ++i) {
            treeSetString.add(ketQuaThisArrayList.get(i).getMaMonHoc());
        }
        DefaultComboBoxModel maMonHocBoxModel = new DefaultComboBoxModel();
        for (String element : treeSetString) {
            maMonHocBoxModel.addElement(element);
        }
        maMonHocComboBox.setModel(maMonHocBoxModel);
    }

    public void tongSoTinChi() {
        ArrayList<DiemThi> diemThiArrayList = new ArrayList<DiemThi>();
        ArrayList<DiemThi> diemThiArrayListN = new ArrayList<DiemThi>();
        Set<DiemThi> diemThiSet = new HashSet<DiemThi>();
        TableModel tableModelBD = bangDiemTable.getModel();
        if (tableModelBD.getRowCount() != 0) {
            for (int i = 0; i < tableModelBD.getRowCount(); ++i) {
                if ((Double) tableModelBD.getValueAt(i, 4) >= 5) {
                    DiemThi diemThi = new DiemThi((String) tableModelBD.getValueAt(i, 1), (Double) tableModelBD.getValueAt(i, 4), (int) tableModelBD.getValueAt(i, 3));
                    diemThiSet.add(diemThi);
                }
            }
            for (int i = 0; i < tableModelBD.getRowCount(); ++i) {
                DiemThi diemThi = new DiemThi((String) tableModelBD.getValueAt(i, 1), (Double) tableModelBD.getValueAt(i, 4), (int) tableModelBD.getValueAt(i, 3));
                diemThiArrayList.add(diemThi);
            }

            int soTinChi = 0;
            for (DiemThi diemThi : diemThiSet) {
                soTinChi += diemThi.getSoTinChi();
            }

            for (int i = 0; i < diemThiArrayList.size(); ++i) {
                int trungLap = 0;
                for (int j = 0; j < i; ++j) {
                    if (diemThiArrayList.get(i).getMaMonHoc().equals(diemThiArrayList.get(j).getMaMonHoc())) {
                        trungLap = 1;
                    }
                }
                if (trungLap == 0) {
                    diemThiArrayListN.add(diemThiArrayList.get(i));
                }
            }
            int tongTinChi = 0;
            for (DiemThi diemThiN : diemThiArrayListN) {
                tongTinChi += diemThiN.getSoTinChi();
                for (DiemThi diemThi : diemThiArrayList) {
                    if (diemThiN.getMaMonHoc().equals(diemThi.getMaMonHoc())) {
                        if (diemThiN.getDiemThi() < diemThi.getDiemThi()) {
                            diemThiN.setDiemThi(diemThi.getDiemThi());
                        }
                    }
                }
            }

            double diemTB = 0;
            for (DiemThi diemThi : diemThiArrayListN) {
                diemTB += diemThi.getDiemThi() * diemThi.getSoTinChi() / tongTinChi;
            }
            diemTB = (double) Math.round(diemTB * 100) / 100;

            if (soTinChi > 0) {
                bd_diemTBTextField.setText(String.valueOf(diemTB));
                bd_soTinChiTextField.setText(String.valueOf(soTinChi));
            }
        } else {
            bd_diemTBTextField.setText("");
            bd_soTinChiTextField.setText("");
        }
    }

    // Bảng điểm Sinh viên
    public void renderBangDiem(String maHocVien) {
        studentService = new HocVienService();
        String[] columnNamesBangDiem = new String[]{
            "STT", "Mã MH", "Tên Môn Học", "Số Tín Chỉ", "Điểm"};
        defaultTableBangDiemModel = new DefaultTableModel(columnNamesBangDiem, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        bangDiemTable.setModel(defaultTableBangDiemModel);
        setDataBangDiemTable(studentService.getBangDiem(maHocVien));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = bangDiemTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(50);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(180);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
    }

//    public ArrayList<KetQuaThi> filterKetQuaThi() {
//
//    }
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
                java.util.logging.Logger.getLogger(KetQuaThiView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(KetQuaThiView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(KetQuaThiView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(KetQuaThiView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            //</editor-fold>
            /* Create and display the form */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new KetQuaThiView().setVisible(true);
                }
            });

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(KetQuaThiView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton backButton;
    private javax.swing.JTable bangDiemTable;
    private javax.swing.JTextField bd_diemTBTextField;
    private javax.swing.JLabel bd_hoTenLabel;
    private javax.swing.JTextField bd_hoTenTextField;
    private javax.swing.JButton bd_inBangDiemButton;
    private javax.swing.JLabel bd_maHocVienLabel;
    private javax.swing.JTextField bd_maHocVienTextField;
    private javax.swing.JTextField bd_maLopTextField;
    private javax.swing.JButton bd_refreshButton;
    private javax.swing.JTextField bd_soTinChiTextField;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel diemTBLabel;
    private javax.swing.JTextField diemThiTextField;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField hoTenHocVienTextField;
    private javax.swing.JButton in_inButton;
    private javax.swing.JTextField in_maLopTextField;
    private javax.swing.JTextField in_maMonHocTextField;
    private com.toedter.calendar.JDateChooser in_ngayThiDateChooser;
    private javax.swing.JTextField in_tenMonHocTextField;
    private javax.swing.JButton in_xemButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField ketQuaTextField;
    private javax.swing.JTable ketQuaThiTable;
    private javax.swing.JLabel kiemTraDiemThiLabel;
    private javax.swing.JSpinner lanThiSpinner;
    private javax.swing.JTextField maHocVienTextField;
    private javax.swing.JComboBox<String> maLopComboBox;
    private javax.swing.JTextField maLopTextField;
    private javax.swing.JComboBox<String> maMonHocComboBox;
    private javax.swing.JTextField maMonHocTextField;
    private javax.swing.JButton nextButton;
    private com.toedter.calendar.JDateChooser ngayThiDateChooser;
    private com.toedter.calendar.JDateChooser ngayThiFilterDateChooser;
    private javax.swing.JButton nhapKetQuaImport;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton sortTheoDiem;
    private javax.swing.JButton sortTheoTen;
    private javax.swing.JTextField tenMonHocTextField;
    private javax.swing.JButton xuatKetQuaThiVienButton;
    // End of variables declaration//GEN-END:variables
}
