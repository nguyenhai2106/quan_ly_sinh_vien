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
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nguye
 */
public class HocVienView extends javax.swing.JFrame {

    private String[] columnNames = new String[]{
        "STT", "Mã Học Viên", "Họ", "Tên", "Ngày Sinh", "Giới Tính", "Nơi Sinh", "Mã Lớp"};
    HocVienService studentService;
    KetQuaThiService ketQuaThiService;
    DefaultTableModel defaultTableModel;
    DefaultTableModel defaultTableBangDiemModel;
    KetQuaThiService ketQuaThiServiceTest = new KetQuaThiService();

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
            Logger.getLogger(HocVienView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportBangDiem(JTable table) {
        // TODO add your handling code here:
        String fileName = null;
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(HocVienView.this);
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
            FileOutputStream fileOut = new FileOutputStream(fileName);
            footRow.createCell(0).setCellValue("");
            footRow.createCell(1).setCellValue("Số TCTL");
            footRow.createCell(2).setCellValue(bd_soTinChiTextField.getText());
            footRow.createCell(3).setCellValue("Điểm TB");
            footRow.createCell(4).setCellValue(bd_diemTBTextField.getText());
            xSSFWorkbook.write(fileOut);
            fileOut.close();
            xSSFWorkbook.close();
            JOptionPane.showMessageDialog(HocVienView.this, "Đã in Bảng điểm của học viên " + bd_hoTenTextField.getText(), "Message", 1);
        } catch (IOException ex) {
            Logger.getLogger(KetQuaThiView.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void chooseStudentTable() {
        ListSelectionModel listSelectionModel = studentTable.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    String maHocVien = String.valueOf(studentTable.getValueAt(row, 1));
                    maHocVienTextField.setText(maHocVien);
                    bd_maHocVienTextField.setText(maHocVien);

                    String hoHocVien = String.valueOf(studentTable.getValueAt(row, 2));
                    hoHocVienTextField.setText(hoHocVien);

                    String tenHocVien = String.valueOf(studentTable.getValueAt(row, 3));
                    tenHocVienTextField.setText(tenHocVien);
                    bd_hoTenTextField.setText(hoHocVien + " " + tenHocVien);

                    java.util.Date ngaySinh;
                    try {
                        ngaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(studentTable.getValueAt(row, 4)));
                        namSinhDateChooser.setDate(ngaySinh);

                    } catch (ParseException ex) {
                        Logger.getLogger(HocVienView.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                    String noiSinh = String.valueOf(studentTable.getValueAt(row, 6));
                    noiSinhTextField.setText(noiSinh);

                    String gioiTinh = String.valueOf(studentTable.getValueAt(row, 5));
                    if (gioiTinh.equals("Nam")) {
                        namRadioButton.setSelected(true);
                        nuRadioButton.setSelected(false);
                    } else {
                        nuRadioButton.setSelected(true);
                        namRadioButton.setSelected(false);
                    }
                    String maLop = String.valueOf(studentTable.getValueAt(row, 7));
                    maLopTextField.setText(maLop);
                    bd_maLopTextField.setText(maLop);
                    // enable Edit and Delete buttons
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    renderBangDiem(maHocVien);
                    tongSoTinChi();
                }
            }
        });
    }

    /**
     * Creates new form QLGVView
     */
    public void searchStudent(String str) {
        DefaultTableModel tableModel = (DefaultTableModel) studentTable.getModel();
        TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(tableRowSorter);
        tableRowSorter.setRowFilter(RowFilter.regexFilter(str));
    }

    public void setDataTable(ArrayList<HocVien> studentsArrayList) {
        int index = 0;
        for (HocVien student : studentsArrayList) {
            defaultTableModel.addRow(new Object[]{
                index + 1, student.getMaHocVien(), student.getHoHocVien(), student.getTenHocVien(), student.getNgaySinh(), student.getGioiTinh(), student.getNoiSinh(), student.getMaLop()});
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

//    Hiện Thông Báo
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
//    Hết Hàm hiện thông báo

    public HocVienView() {
        initComponents();
        chooseStudentTable();
        maLopComboBox();
        setResizable(false);
        studentService = new HocVienService();
        defaultTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        studentTable.setModel(defaultTableModel);

        ArrayList<HocVien> studentsArrayList = studentService.getAllStudents();
        setDataTable(studentsArrayList);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = studentTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(50);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(180);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);
        columnModel.getColumn(7).setCellRenderer(centerRenderer);
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
        hoHocVienTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tenHocVienTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        noiSinhTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        namRadioButton = new javax.swing.JRadioButton();
        nuRadioButton = new javax.swing.JRadioButton();
        maLopTextField = new javax.swing.JTextField();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        nhapThieuHoLabel = new javax.swing.JLabel();
        nhapThieuMaHVLabel = new javax.swing.JLabel();
        nhapThieuNoiSinhLabel = new javax.swing.JLabel();
        nhapThieuMaLopLabel = new javax.swing.JLabel();
        namSinhDateChooser = new com.toedter.calendar.JDateChooser();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        refreshButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        maLopComboBox = new javax.swing.JComboBox<>();
        xuatDSHocVienButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUẢN LÝ HỌC VIÊN");

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jPanel2.setPreferredSize(new java.awt.Dimension(450, 401));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Mã Học Viên");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Họ");

        maHocVienTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                maHocVienTextFieldFocusLost(evt);
            }
        });
        maHocVienTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                maHocVienTextFieldKeyReleased(evt);
            }
        });

        hoHocVienTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                hoHocVienTextFieldFocusLost(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Tên");

        tenHocVienTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tenHocVienTextFieldFocusLost(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Năm Sinh");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Nơi Sinh");

        noiSinhTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                noiSinhTextFieldFocusLost(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Giới Tính");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Mã Lớp");

        buttonGroup1.add(namRadioButton);
        namRadioButton.setText("Nam");

        buttonGroup1.add(nuRadioButton);
        nuRadioButton.setText("Nữ");

        maLopTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                maLopTextFieldFocusLost(evt);
            }
        });

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

        nhapThieuHoLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        nhapThieuHoLabel.setText(" ");

        nhapThieuMaHVLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        nhapThieuMaHVLabel.setText(" ");

        nhapThieuNoiSinhLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        nhapThieuNoiSinhLabel.setText(" ");

        nhapThieuMaLopLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        nhapThieuMaLopLabel.setText(" ");

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
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(namRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(nuRadioButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(nhapThieuMaLopLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(maLopTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noiSinhTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nhapThieuNoiSinhLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(25, 25, 25))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maHocVienTextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(hoHocVienTextField)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tenHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(nhapThieuMaHVLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nhapThieuHoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namSinhDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(25, 25, 25))))
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
                .addGap(38, 38, 38))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel4, jLabel5, jLabel6, jLabel7, namRadioButton, nuRadioButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(maHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(nhapThieuMaHVLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(hoHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tenHocVienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(nhapThieuHoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namSinhDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(noiSinhTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(nhapThieuNoiSinhLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(namRadioButton)
                    .addComponent(nuRadioButton))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(maLopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhapThieuMaLopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton)
                    .addComponent(clearButton)
                    .addComponent(deleteButton))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {hoHocVienTextField, jLabel1, jLabel2, jLabel4, jLabel5, jLabel6, maHocVienTextField, maLopTextField, namRadioButton, nhapThieuNoiSinhLabel, noiSinhTextField, nuRadioButton, tenHocVienTextField});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addButton, clearButton, deleteButton, editButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {nhapThieuHoLabel, nhapThieuMaHVLabel});

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
        bangDiemTable.setRowHeight(30);
        bangDiemTable.setShowGrid(true);
        jScrollPane2.setViewportView(bangDiemTable);

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

        studentTable.setAutoCreateRowSorter(true);
        studentTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        studentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentTable.setShowGrid(true);
        jScrollPane1.setViewportView(studentTable);
        if (studentTable.getColumnModel().getColumnCount() > 0) {
            studentTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            studentTable.getColumnModel().getColumn(1).setMinWidth(300);
            studentTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            studentTable.getColumnModel().getColumn(1).setMaxWidth(300);
            studentTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        }
        studentTable.setRowHeight(30);

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

        xuatDSHocVienButton.setText("Xuất DS Học Viên");
        xuatDSHocVienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xuatDSHocVienButtonActionPerformed(evt);
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
                        .addComponent(xuatDSHocVienButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTextField)
                        .addGap(18, 18, 18)
                        .addComponent(maLopComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maLopComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(xuatDSHocVienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, maLopComboBox, searchTextField});

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private boolean validateTen() {
        String tenHocVien = tenHocVienTextField.getText();
        if (tenHocVien == null || "".equals(tenHocVien.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateHo() {
        String hoHocVien = hoHocVienTextField.getText();
        if (hoHocVien == null || "".equals(hoHocVien.trim())) {
            return false;
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

    private boolean validateMaLop() {
        String maLop = maLopTextField.getText();
        if (maLop == null || "".equals(maLop.trim())) {
            return false;
        }
        return true;
    }

    private boolean validateNoiSinh() {
        String noiSinh = noiSinhTextField.getText();
        if (noiSinh == null || "".equals(noiSinh.trim())) {
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

    private boolean validateGioiTinh() {
        if (!namRadioButton.isSelected() && !nuRadioButton.isSelected()) {
            return false;
        }
        return true;
    }

    private boolean validationStudent() {
        return validateMaHocVien() && validateHo() && validateTen() && validateNgaySinh() && validateGioiTinh() && validateNoiSinh() && validateMaLop();
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableModel.setRowCount(0);
        setDataTable(studentService.getAllStudents());
    }//GEN-LAST:event_refreshButtonActionPerformed

//Sự kiện nhấn nút Add Sinh Viên
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        studentService = new HocVienService();

        HocVien student = new HocVien();
        if (validationStudent()) {
            student.setMaHocVien(maHocVienTextField.getText());
            student.setHoHocVien(hoHocVienTextField.getText());
            student.setTenHocVien(tenHocVienTextField.getText());
            java.sql.Date ngaySinh = new java.sql.Date(namSinhDateChooser.getDate().getTime());
            student.setNgaySinh(ngaySinh);
            String gioiTinh = namRadioButton.isSelected() ? "Nam" : "Nữ";
            student.setGioiTinh(gioiTinh);
            student.setNoiSinh(noiSinhTextField.getText());
            student.setMaLop(maLopTextField.getText());
        } else {
            showMessage("Vui lòng kiểm tra lại các thông tin đã nhập!");
            return;
        }

        if (studentService.CheckAddSinhVien(student)) {
            studentService.addStudent(student);
            defaultTableModel.setRowCount(0);
            setDataTable(studentService.getAllStudents());
        } else {
            nhapThieuMaHVLabel.setForeground(Color.RED);
            nhapThieuMaHVLabel.setText("Mã Học viên đã tồn tại!");
            JOptionPane.showMessageDialog(HocVienView.this, "Học Viên đã tồn tại trên hệ thống. Vui lòng kiểm tra lại Mã Học Viên!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(HocVienView.this, "Vui lòng chọn Học Viên muốn xóa!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(HocVienView.this, "Bạn chắc chắn muốn xóa?");
            if (confirm == JOptionPane.YES_OPTION) {
                String studentMaHocVien = (String) studentTable.getValueAt(row, 1);
                studentService.deleteStudent(studentMaHocVien);
                defaultTableModel.setRowCount(0);
                setDataTable(studentService.getAllStudents());
                clearButtonActionPerformed(evt);
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        maHocVienTextField.setText("");
        hoHocVienTextField.setText("");
        tenHocVienTextField.setText("");
        namSinhDateChooser.setCalendar(null);
        noiSinhTextField.setText("");
        buttonGroup1.clearSelection();
        maLopTextField.setText("");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        nhapThieuHoLabel.setText("");
        nhapThieuMaHVLabel.setText("");
        nhapThieuMaLopLabel.setText("");
        nhapThieuNoiSinhLabel.setText("");
    }//GEN-LAST:event_clearButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        studentService = new HocVienService();
        HocVien student = new HocVien();

        if (validationStudent()) {
            student.setMaHocVien(maHocVienTextField.getText());
            student.setHoHocVien(hoHocVienTextField.getText());
            student.setTenHocVien(tenHocVienTextField.getText());
            java.sql.Date ngaySinh = new java.sql.Date(namSinhDateChooser.getDate().getTime());
            student.setNgaySinh(ngaySinh);
            String gioiTinh = namRadioButton.isSelected() ? "Nam" : "Nữ";
            student.setGioiTinh(gioiTinh);
            student.setNoiSinh(noiSinhTextField.getText());
            student.setMaLop(maLopTextField.getText());
            studentService.updateStudent(student);
            defaultTableModel.setRowCount(0);
            setDataTable(studentService.getAllStudents());
        } else {
            showMessage("Vui lòng kiểm tra lại các thông tin đã nhập!");
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void maLopComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maLopComboBoxActionPerformed
        // TODO add your handling code here:
        studentService = new HocVienService();
        String valueMaLop = maLopComboBox.getItemAt(maLopComboBox.getSelectedIndex());
        if (valueMaLop.equals("All")) {
            defaultTableModel.setRowCount(0);
            setDataTable(studentService.getAllStudents());
        } else {
            defaultTableModel.setRowCount(0);
            setDataTable(studentService.filterByClass(valueMaLop));
        }
    }//GEN-LAST:event_maLopComboBoxActionPerformed

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        // TODO add your handling code here:
        String searchString = searchTextField.getText();
        searchStudent(searchString);
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void bd_refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bd_refreshButtonActionPerformed
        // TODO add your handling code here:
        defaultTableBangDiemModel.setRowCount(0);
        if (bd_maHocVienTextField.getText() != "") {
            setDataBangDiemTable(studentService.getBangDiem(bd_maHocVienTextField.getText()));
        }
    }//GEN-LAST:event_bd_refreshButtonActionPerformed

    private void xuatDSHocVienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xuatDSHocVienButtonActionPerformed
        // TODO add your handling code here:
        String[] columnNamesPrint = new String[]{
            "STT", "Ma hoc vien", "Ho", "Ten", "Ngay Sinh", "Gioi Tinh", "Noi Sinh", "Ma Lop"};
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(HocVienView.this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String name = fileChooser.getSelectedFile().getName();
            String path = fileChooser.getSelectedFile().getParentFile().getPath();
            String file = path + "\\" + name + ".xls";
            export(studentTable, new File(file), columnNamesPrint);
        }
    }//GEN-LAST:event_xuatDSHocVienButtonActionPerformed

    private void bd_inBangDiemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bd_inBangDiemButtonActionPerformed
        // TODO add your handling code here:
        exportBangDiem(bangDiemTable);
    }//GEN-LAST:event_bd_inBangDiemButtonActionPerformed

    private void maHocVienTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maHocVienTextFieldKeyReleased

        String maHocVien = maHocVienTextField.getText();
        if (!"".equals(maHocVien) && !" ".equals(maHocVien)) {
            HocVien student = ketQuaThiServiceTest.searchTenHocVien(maHocVien);
            if (student != null) {
                nhapThieuMaHVLabel.setForeground(Color.RED);
                nhapThieuMaHVLabel.setText("Mã Học viên đã tồn tại!");
            } else {
                nhapThieuMaHVLabel.setForeground(Color.BLUE);
                nhapThieuMaHVLabel.setText("Mã Học viên hợp lệ.");
            }
        } else {
            nhapThieuMaHVLabel.setForeground(Color.RED);
            nhapThieuMaHVLabel.setText("Không được để trống Mã Học Viên!");
        }

    }//GEN-LAST:event_maHocVienTextFieldKeyReleased

    private void maHocVienTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_maHocVienTextFieldFocusLost
        // TODO add your handling code here:
        String maHocVien = maHocVienTextField.getText();
        if (!"".equals(maHocVien) && !" ".equals(maHocVien) && maHocVienTextField.getText() != null) {
            HocVien student = ketQuaThiServiceTest.searchTenHocVien(maHocVien);
            if (student != null) {
                nhapThieuMaHVLabel.setForeground(Color.RED);
                nhapThieuMaHVLabel.setText("Mã Học viên đã tồn tại!");
            } else {
                nhapThieuMaHVLabel.setForeground(Color.BLUE);
                nhapThieuMaHVLabel.setText("Mã Học viên hợp lệ.");
            }
        } else {
            nhapThieuMaHVLabel.setForeground(Color.RED);
            nhapThieuMaHVLabel.setText("Không được để trống Mã Học Viên!");
        }
    }//GEN-LAST:event_maHocVienTextFieldFocusLost

    private void hoHocVienTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hoHocVienTextFieldFocusLost
        // TODO add your handling code here:
        // TODO add your handling code here:
        String hoHocVien = hoHocVienTextField.getText();
        if (!"".equals(hoHocVien) && !" ".equals(hoHocVien) && hoHocVienTextField.getText() != null) {
            nhapThieuHoLabel.setText("");
        } else {
            nhapThieuHoLabel.setForeground(Color.RED);
            nhapThieuHoLabel.setText("Không được để trống Họ hoặc Tên!");
        }
    }//GEN-LAST:event_hoHocVienTextFieldFocusLost

    private void tenHocVienTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tenHocVienTextFieldFocusLost
        String tenHocVien = tenHocVienTextField.getText();
        if (!"".equals(tenHocVien) && !" ".equals(tenHocVien) && tenHocVienTextField.getText() != null) {
            nhapThieuHoLabel.setText("");
        } else {
            nhapThieuHoLabel.setForeground(Color.RED);
            nhapThieuHoLabel.setText("Không được để trống Họ hoặc Tên!");
        }
    }//GEN-LAST:event_tenHocVienTextFieldFocusLost

    private void noiSinhTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_noiSinhTextFieldFocusLost
        // TODO add your handling code here:
        String noiSinh = noiSinhTextField.getText();
        if (!"".equals(noiSinh) && !" ".equals(noiSinh) && noiSinhTextField.getText() != null) {
            nhapThieuNoiSinhLabel.setText("");
        } else {
            nhapThieuNoiSinhLabel.setForeground(Color.RED);
            nhapThieuNoiSinhLabel.setText("Không được để trống Nơi Sinh!");
        }
    }//GEN-LAST:event_noiSinhTextFieldFocusLost

    private void maLopTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_maLopTextFieldFocusLost
        // TODO add your handling code here:
        String maLop = maLopTextField.getText();
        if (!"".equals(maLop) && !" ".equals(maLop) && maLopTextField.getText() != null) {
            nhapThieuMaLopLabel.setText("");
        } else {
            nhapThieuMaLopLabel.setForeground(Color.RED);
            nhapThieuMaLopLabel.setText("Không được để trống Mã Lớp!");
        }
    }//GEN-LAST:event_maLopTextFieldFocusLost

    /**
     * @param args the command line arguments
     */
    public void maLopComboBox() {
        studentService = new HocVienService();
        ArrayList<HocVien> studentsArrayList = studentService.getAllStudents();
        Set<String> treeSetString = new TreeSet();
        treeSetString.add("All");
        for (int i = 0; i < studentsArrayList.size(); ++i) {
            treeSetString.add(studentsArrayList.get(i).getMaLop());
        }
        DefaultComboBoxModel maLopBoxModel = new DefaultComboBoxModel();
        for (String element : treeSetString) {
            maLopBoxModel.addElement(element);
        }
        maLopComboBox.setModel(maLopBoxModel);
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
                java.util.logging.Logger.getLogger(HocVienView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(HocVienView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(HocVienView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(HocVienView.class
                        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            //</editor-fold>
            /* Create and display the form */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new HocVienView().setVisible(true);
                }
            });

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(HocVienView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
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
    private javax.swing.JButton editButton;
    private javax.swing.JTextField hoHocVienTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField maHocVienTextField;
    private javax.swing.JComboBox<String> maLopComboBox;
    private javax.swing.JTextField maLopTextField;
    private javax.swing.JRadioButton namRadioButton;
    private com.toedter.calendar.JDateChooser namSinhDateChooser;
    private javax.swing.JLabel nhapThieuHoLabel;
    private javax.swing.JLabel nhapThieuMaHVLabel;
    private javax.swing.JLabel nhapThieuMaLopLabel;
    private javax.swing.JLabel nhapThieuNoiSinhLabel;
    private javax.swing.JTextField noiSinhTextField;
    private javax.swing.JRadioButton nuRadioButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable studentTable;
    private javax.swing.JTextField tenHocVienTextField;
    private javax.swing.JButton xuatDSHocVienButton;
    // End of variables declaration//GEN-END:variables
}
