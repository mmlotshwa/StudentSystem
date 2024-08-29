/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem;

import com.mandla.studentsystem.gui.*;
import com.mandla.studentsystem.entities.*;
import com.mandla.studentsystem.repositories.*;
import com.mandla.studentsystem.utils.DataTableModel;
import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.hibernate.HibernateException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mlots
 */
public class MainForm extends javax.swing.JFrame {
    private List<GradeTeacher> gradeTeachers;
    private final GradeTeacherRepository repository;
    private int currentRec = - 1;
    private int numRecords = -1;
    private enum Status {VIEW, EDIT, NEW};
    private Status viewStatus;
    private List<GradeName> grades;
    private ArrayList<Field> colNames;
    private ArrayList<Method> colMethods;
    private ArrayList<Method> colSetMethods;
    private DataTableModel<GradeTeacher> dataModel;
    private TableRowSorter<TableModel> sorter;
    private JTable resultTable;
    private List<Teacher> teacherData;
    private int currentYear;
    private int currentTerm;
    private List<TermSystem> terms;
    private Map<String,Integer> teachers;
    JComboBox teacherCombo; //For allowing the user to select the teacher on the table
    JComboBox gradeCombo; //For allowing the user to change the grade on the table
    private boolean updated; //Flag for all records status; true-if records read from database, false-if records created for new term
    private boolean pending; //Flag to show records set for update
    private Map<Integer,GradeTeacher> pendingRecords; //To store updated or added records while updated is true and pending false
    
    
    /**
     * Creates new form GradeTeacherForm
     */
    public MainForm() {
        initComponents();
        updated = pending = false;
        pendingRecords = new HashMap<>();
        repository = new GradeTeacherRepository();
        loadComboBoxes();
        java.awt.EventQueue.invokeLater(() -> {
            createAllCharts();
        });
        activateListeners();
    }
    
    private void loadComboBoxes(){
        try {
            LocalDate currentDate = LocalDate.now();

            TermSystemRepository termRepository = new TermSystemRepository();

            terms = new ArrayList<>();
            terms = termRepository.getAllTerms();
            DefaultComboBoxModel termModel = new DefaultComboBoxModel(terms.toArray());
            txtTerm.setModel(termModel);

            currentYear = currentDate.getYear();

            DefaultComboBoxModel yearModel = new DefaultComboBoxModel();
            for(int i = currentYear - 10; i<=currentYear + 10; i++){
                yearModel.addElement((Object) i);
            }
            txtTYear.setModel(yearModel);

            txtTYear.setSelectedItem((Object) currentYear);
            TermSystem thisTerm = terms.get(0);

            for(int i = terms.size() - 1; i >=0 ; i--){
                if(currentDate.getMonthValue() >= terms.get(i).getStartMonth()){
                    thisTerm = terms.get(i);
                    break;
                }
            }

            txtTerm.setSelectedItem(thisTerm);
            currentTerm = thisTerm.getTerm();
        } catch (Exception e) {
            dialogMessage("Load ComboBoxes: Failure at Start-Up! This operation cannot proceed!!\n" 
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    private void activateListeners(){
        txtTerm.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTermActionPerformed(evt);
        });
        
        txtTYear.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTYearActionPerformed(evt);
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuItem1 = new javax.swing.JMenuItem();
        txtTYear2 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTerm2 = new javax.swing.JComboBox<>();
        headerPanel = new javax.swing.JPanel();
        bodyPanel = new javax.swing.JPanel();
        headingPanel = new javax.swing.JPanel();
        txtTYear = new javax.swing.JComboBox<>();
        txtTerm = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        settingsButton = new javax.swing.JButton();
        studentButton = new javax.swing.JButton();
        subjectButton = new javax.swing.JButton();
        registerAllButton = new javax.swing.JButton();
        teacherButton = new javax.swing.JButton();
        gradeTeacherButton = new javax.swing.JButton();
        registerStudentButton = new javax.swing.JButton();
        gradeAllButton = new javax.swing.JButton();
        termCreateButton = new javax.swing.JButton();
        subjectTeacherButton = new javax.swing.JButton();
        leftChartPanel = new javax.swing.JPanel();
        mainChartPanel = new javax.swing.JPanel();
        rightChartPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        dataMenu = new javax.swing.JMenu();
        studentMenuItem = new javax.swing.JMenuItem();
        subjectMenuItem = new javax.swing.JMenuItem();
        teacherMenuItem = new javax.swing.JMenuItem();
        registrationMenu = new javax.swing.JMenu();
        gradeTeacherMenuItem = new javax.swing.JMenuItem();
        subjectTeacherMenuItem = new javax.swing.JMenuItem();
        registerMenu = new javax.swing.JMenu();
        registerAllMenuItem = new javax.swing.JMenuItem();
        registerStudentMenuItem = new javax.swing.JMenuItem();
        marksMenu = new javax.swing.JMenu();
        byGradeMenuItem = new javax.swing.JMenuItem();
        marksByStudentMenuItem = new javax.swing.JMenuItem();

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.setText("jTextField1");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("StudentID:");

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        jMenuItem1.setText("jMenuItem1");

        txtTYear2.setPreferredSize(new java.awt.Dimension(72, 26));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Year:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Term:");

        txtTerm2.setPreferredSize(new java.awt.Dimension(72, 26));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Studet Report System");
        setBackground(java.awt.SystemColor.control);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        headerPanel.setBackground(new java.awt.Color(51, 51, 255));

        bodyPanel.setBackground(new java.awt.Color(51, 220, 246));

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        txtTYear.setPreferredSize(new java.awt.Dimension(72, 26));

        txtTerm.setPreferredSize(new java.awt.Dimension(72, 26));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Term:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Year:");

        settingsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        settingsButton.setForeground(new java.awt.Color(0, 0, 204));
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/settings.png"))); // NOI18N
        settingsButton.setToolTipText("System Settings");
        settingsButton.setActionCommand("Settings");
        settingsButton.setBorder(null);
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        settingsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        settingsButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/settingshover.png"))); // NOI18N
        settingsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headingPanelLayout = new javax.swing.GroupLayout(headingPanel);
        headingPanel.setLayout(headingPanelLayout);
        headingPanelLayout.setHorizontalGroup(
            headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headingPanelLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTYear, 0, 253, Short.MAX_VALUE)
                .addGap(523, 523, 523)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(143, 143, 143)
                .addComponent(settingsButton)
                .addContainerGap())
        );
        headingPanelLayout.setVerticalGroup(
            headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(settingsButton))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        studentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        studentButton.setForeground(new java.awt.Color(0, 0, 204));
        studentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addstudent90.png"))); // NOI18N
        studentButton.setText("Students");
        studentButton.setBorder(null);
        studentButton.setBorderPainted(false);
        studentButton.setContentAreaFilled(false);
        studentButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        studentButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        studentButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addstudenthover90.png"))); // NOI18N
        studentButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        studentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentButtonActionPerformed(evt);
            }
        });

        subjectButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subjectButton.setForeground(new java.awt.Color(0, 0, 204));
        subjectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addsubject90.png"))); // NOI18N
        subjectButton.setText("Subjects");
        subjectButton.setBorder(null);
        subjectButton.setBorderPainted(false);
        subjectButton.setContentAreaFilled(false);
        subjectButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        subjectButton.setDisabledIcon(null);
        subjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        subjectButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addsubjecthover90.png"))); // NOI18N
        subjectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        subjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectButtonActionPerformed(evt);
            }
        });

        registerAllButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registerAllButton.setForeground(new java.awt.Color(0, 0, 204));
        registerAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/registergrade90.png"))); // NOI18N
        registerAllButton.setText("Register All");
        registerAllButton.setBorder(null);
        registerAllButton.setBorderPainted(false);
        registerAllButton.setContentAreaFilled(false);
        registerAllButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        registerAllButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        registerAllButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/registergradehover90.png"))); // NOI18N
        registerAllButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        registerAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerAllButtonActionPerformed(evt);
            }
        });

        teacherButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        teacherButton.setForeground(new java.awt.Color(0, 0, 204));
        teacherButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addteacher90.png"))); // NOI18N
        teacherButton.setText("Teachers");
        teacherButton.setBorder(null);
        teacherButton.setBorderPainted(false);
        teacherButton.setContentAreaFilled(false);
        teacherButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        teacherButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        teacherButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addteacherhover90.png"))); // NOI18N
        teacherButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        teacherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherButtonActionPerformed(evt);
            }
        });

        gradeTeacherButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        gradeTeacherButton.setForeground(new java.awt.Color(0, 0, 204));
        gradeTeacherButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/gradeteach90.png"))); // NOI18N
        gradeTeacherButton.setText("Grade Teacher");
        gradeTeacherButton.setBorder(null);
        gradeTeacherButton.setBorderPainted(false);
        gradeTeacherButton.setContentAreaFilled(false);
        gradeTeacherButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        gradeTeacherButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gradeTeacherButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/gradeteachover90.png"))); // NOI18N
        gradeTeacherButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gradeTeacherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeTeacherButtonActionPerformed(evt);
            }
        });

        registerStudentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registerStudentButton.setForeground(new java.awt.Color(0, 0, 204));
        registerStudentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/studentregister90.png"))); // NOI18N
        registerStudentButton.setText("Grade/Register Student");
        registerStudentButton.setBorder(null);
        registerStudentButton.setBorderPainted(false);
        registerStudentButton.setContentAreaFilled(false);
        registerStudentButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        registerStudentButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        registerStudentButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/studentregisterhover90.png"))); // NOI18N
        registerStudentButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        registerStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerStudentButtonActionPerformed(evt);
            }
        });

        gradeAllButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        gradeAllButton.setForeground(new java.awt.Color(0, 0, 204));
        gradeAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/grade90.png"))); // NOI18N
        gradeAllButton.setText("Assess By Grade");
        gradeAllButton.setBorder(null);
        gradeAllButton.setBorderPainted(false);
        gradeAllButton.setContentAreaFilled(false);
        gradeAllButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        gradeAllButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gradeAllButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/gradehover90.png"))); // NOI18N
        gradeAllButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gradeAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeAllButtonActionPerformed(evt);
            }
        });

        termCreateButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        termCreateButton.setForeground(new java.awt.Color(0, 0, 204));
        termCreateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/termcreate.png"))); // NOI18N
        termCreateButton.setText("Create Term Registrations");
        termCreateButton.setBorder(null);
        termCreateButton.setBorderPainted(false);
        termCreateButton.setContentAreaFilled(false);
        termCreateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        termCreateButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/termcreatedisabled.png"))); // NOI18N
        termCreateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        termCreateButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/termcreatehover.png"))); // NOI18N
        termCreateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        termCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termCreateButtonActionPerformed(evt);
            }
        });

        subjectTeacherButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subjectTeacherButton.setForeground(new java.awt.Color(0, 0, 204));
        subjectTeacherButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/subjectteach90.png"))); // NOI18N
        subjectTeacherButton.setText("Subject Teacher");
        subjectTeacherButton.setBorder(null);
        subjectTeacherButton.setBorderPainted(false);
        subjectTeacherButton.setContentAreaFilled(false);
        subjectTeacherButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        subjectTeacherButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        subjectTeacherButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/subjectteachover90.png"))); // NOI18N
        subjectTeacherButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        subjectTeacherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectTeacherButtonActionPerformed(evt);
            }
        });

        leftChartPanel.setBackground(new java.awt.Color(51, 220, 246));
        leftChartPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout leftChartPanelLayout = new javax.swing.GroupLayout(leftChartPanel);
        leftChartPanel.setLayout(leftChartPanelLayout);
        leftChartPanelLayout.setHorizontalGroup(
            leftChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        leftChartPanelLayout.setVerticalGroup(
            leftChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );

        mainChartPanel.setBackground(new java.awt.Color(51, 220, 246));
        mainChartPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout mainChartPanelLayout = new javax.swing.GroupLayout(mainChartPanel);
        mainChartPanel.setLayout(mainChartPanelLayout);
        mainChartPanelLayout.setHorizontalGroup(
            mainChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mainChartPanelLayout.setVerticalGroup(
            mainChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        rightChartPanel.setBackground(new java.awt.Color(51, 220, 246));
        rightChartPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout rightChartPanelLayout = new javax.swing.GroupLayout(rightChartPanel);
        rightChartPanel.setLayout(rightChartPanelLayout);
        rightChartPanelLayout.setHorizontalGroup(
            rightChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        rightChartPanelLayout.setVerticalGroup(
            rightChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(leftChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rightChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(studentButton)
                        .addGap(33, 33, 33)
                        .addComponent(subjectButton))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(teacherButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                        .addComponent(termCreateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(222, 222, 222)
                        .addComponent(gradeTeacherButton)
                        .addGap(63, 63, 63)
                        .addComponent(subjectTeacherButton)
                        .addGap(51, 51, 51)
                        .addComponent(registerAllButton)
                        .addGap(107, 107, 107))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                        .addComponent(registerStudentButton)
                        .addGap(53, 53, 53)
                        .addComponent(gradeAllButton)
                        .addGap(158, 158, 158))))
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addComponent(headingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(registerAllButton)
                                    .addComponent(subjectTeacherButton)
                                    .addComponent(gradeTeacherButton))
                                .addGap(34, 34, 34)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(registerStudentButton)
                                    .addComponent(gradeAllButton)))
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(subjectButton)
                                    .addComponent(studentButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(teacherButton))))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(termCreateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(leftChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mainChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(rightChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        dataMenu.setText("Data");

        studentMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addstudent16.png"))); // NOI18N
        studentMenuItem.setText("Students");
        studentMenuItem.setAutoscrolls(true);
        studentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentMenuItemActionPerformed(evt);
            }
        });
        dataMenu.add(studentMenuItem);

        subjectMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addsubject16.png"))); // NOI18N
        subjectMenuItem.setText("Subjects");
        subjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectMenuItemActionPerformed(evt);
            }
        });
        dataMenu.add(subjectMenuItem);

        teacherMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addteacher16.png"))); // NOI18N
        teacherMenuItem.setText("Teachers");
        teacherMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherMenuItemActionPerformed(evt);
            }
        });
        dataMenu.add(teacherMenuItem);

        jMenuBar1.add(dataMenu);

        registrationMenu.setText("Registration");

        gradeTeacherMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/gradeteach16.png"))); // NOI18N
        gradeTeacherMenuItem.setText("Grade Teacher");
        gradeTeacherMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeTeacherMenuItemActionPerformed(evt);
            }
        });
        registrationMenu.add(gradeTeacherMenuItem);

        subjectTeacherMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/subjectteach16.png"))); // NOI18N
        subjectTeacherMenuItem.setText("Subject Teacher");
        subjectTeacherMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectTeacherMenuItemActionPerformed(evt);
            }
        });
        registrationMenu.add(subjectTeacherMenuItem);

        registerMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/register16.png"))); // NOI18N
        registerMenu.setText("Register");

        registerAllMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/registergrade16.png"))); // NOI18N
        registerAllMenuItem.setText("All Grades");
        registerAllMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerAllMenuItemActionPerformed(evt);
            }
        });
        registerMenu.add(registerAllMenuItem);

        registerStudentMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/studentregister16.png"))); // NOI18N
        registerStudentMenuItem.setText("By Student");
        registerStudentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerStudentMenuItemActionPerformed(evt);
            }
        });
        registerMenu.add(registerStudentMenuItem);

        registrationMenu.add(registerMenu);

        jMenuBar1.add(registrationMenu);

        marksMenu.setText("Marks");

        byGradeMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/grade16.png"))); // NOI18N
        byGradeMenuItem.setText("By Grade");
        byGradeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byGradeMenuItemActionPerformed(evt);
            }
        });
        marksMenu.add(byGradeMenuItem);

        marksByStudentMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/studentregister16.png"))); // NOI18N
        marksByStudentMenuItem.setText("By Student");
        marksByStudentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marksByStudentMenuItemActionPerformed(evt);
            }
        });
        marksMenu.add(marksByStudentMenuItem);

        jMenuBar1.add(marksMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
private void createAllCharts(){
    GradesSummariesRepository gradeRepo = new GradesSummariesRepository();
    StudentSummariesRepository studRepo = new StudentSummariesRepository();
    SubjectSummariesRepository subRepo = new SubjectSummariesRepository();
    
    List<StudentSummaries> studSummaries = new ArrayList<>();
    List<GradesSummaries> gradSummaries = new ArrayList<>();
    List<SubjectSummaries> subSummaries = new ArrayList<>();
    Map<String,Double> gradeChartData = new HashMap<>();
    Map<String,Double> studentChartData = new HashMap<>();
    Map<String,Double> subjectChartData = new HashMap<>();
    
    if(currentTerm != terms.get(0).getTerm()){
        int term = terms.get(terms.size() - 1).getTerm();
        studSummaries = studRepo.getStudentSummariesByYearTerm(currentYear - 1, term);
        gradSummaries = gradeRepo.getGradesSummariesByYearTerm(currentYear - 1, term);
        subSummaries = subRepo.getSubjectSummariesByYearTerm(currentYear - 1, term);
    }else{
        studSummaries = studRepo.getStudentSummariesByYearTerm(currentYear, currentTerm - 1);
        gradSummaries = gradeRepo.getGradesSummariesByYearTerm(currentYear, currentTerm - 1);
        subSummaries = subRepo.getSubjectSummariesByYearTerm(currentYear, currentTerm - 1);
    }
    
    for(GradesSummaries gradeSummary : gradSummaries){
        gradeChartData.put(gradeSummary.toString().trim(), gradeSummary.getAverage());
    }
    java.awt.EventQueue.invokeLater(() -> {
            createChart("Average Performance By Grade",gradeChartData,mainChartPanel);
    });
    
    for(StudentSummaries studSummary : studSummaries){
        if(studSummary.getRanked() == 1){
            studentChartData.put(studSummary.toString().trim(), studSummary.getAverage());
        }
    }
    java.awt.EventQueue.invokeLater(() -> {
            createChart("Average Performance By Student",studentChartData,leftChartPanel);
    });
    
    for(SubjectSummaries subSummary : subSummaries){
        if(subSummary.getRanked() == 1){
            subjectChartData.put(subSummary.toString().trim(), subSummary.getAverage());
        }
    }
    java.awt.EventQueue.invokeLater(() -> {
            createChart("Average Performance By Subject",subjectChartData,rightChartPanel);
    });
}

private void createChart(String title, Map<String,Double> chartData, JPanel panel){
    JFXPanel fxPanel = new JFXPanel();
    panel.setLayout(new BorderLayout());
    PieChart.Data values[] = new PieChart.Data[chartData.size()];
    int counter = 0;
    for(String label : chartData.keySet()){
        values[counter++] = new PieChart.Data(label,chartData.get(label));
    }
    
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
            PieChart pieChart = new PieChart();
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList(values);
        
            pieChart.setData(data);
            pieChart.setTitle("Average Performance By Grade");
            pieChart.setLegendSide(Side.BOTTOM);
            pieChart.setLabelsVisible(true);
            
            //Creating and setting chart's background color fill
            BackgroundFill backgroundFill = new BackgroundFill(
                    Color.valueOf("33DCF6"), CornerRadii.EMPTY, Insets.EMPTY
            );            
            Background background = new Background(backgroundFill);
            pieChart.setBackground(background);
            
            //Creating and inserting percentage captions for chart slices
            Label caption = new Label("");
            caption.setTextFill(Color.DARKBLUE);
            caption.setStyle("-fx-font: bold 12pt \"Segoe UI\";");
            
            DoubleBinding total = Bindings.createDoubleBinding(() -> 
                    data.stream().collect(Collectors.summingDouble(PieChart.Data::getPieValue)), data
            );
            
            for(PieChart.Data current : pieChart.getData()){
                current.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, 
                    e -> {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String percentage = String.format("%.1f%%", 100*current.getPieValue()/total.get());
                        caption.setText(percentage);
                    });
            }
            
            Group root = new Group();
            root.getChildren().addAll(pieChart,caption);
            
            Scene scene = new Scene(root);
            
            fxPanel.setScene(scene);
            
            panel.add(fxPanel, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    });    
}    
private void txtTYearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentYear = Integer.parseInt(txtTYear.getSelectedItem().toString());
        System.out.println("Year Changed....Loading Data!!!");
        
    }  
    
    private void txtTermActionPerformed(java.awt.event.ActionEvent evt) {                                        
        TermSystem term = (TermSystem) txtTerm.getSelectedItem();
        currentTerm = term.getTerm();
        System.out.println("Term Changed....Loading Data!!!");
        
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        repository.closeSession();
    }//GEN-LAST:event_formWindowClosing

    private void studentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentMenuItemActionPerformed
        String [] data = new String[]{};
        StudentForm.main(data);
    }//GEN-LAST:event_studentMenuItemActionPerformed

    private void subjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectMenuItemActionPerformed
        String [] data = new String[]{};
        SubjectForm.main(data);
    }//GEN-LAST:event_subjectMenuItemActionPerformed

    private void teacherMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherMenuItemActionPerformed
        String [] data = new String[]{};
        TeacherForm.main(data);
    }//GEN-LAST:event_teacherMenuItemActionPerformed

    private void gradeTeacherMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeTeacherMenuItemActionPerformed
        String [] data = new String[]{"TYear",currentYear+"","Term",currentTerm+""};
        GradeTeacherForm.main(data);
    }//GEN-LAST:event_gradeTeacherMenuItemActionPerformed

    private void subjectTeacherMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectTeacherMenuItemActionPerformed
        String [] data = new String[]{"TYear",currentYear+"","Term",currentTerm+""};
        SubjectTeacherForm.main(data);
    }//GEN-LAST:event_subjectTeacherMenuItemActionPerformed

    private void registerAllMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerAllMenuItemActionPerformed
        String [] data = new String[]{"TYear",currentYear+"","Term",currentTerm+""};
        RegistrationForm.main(data);
    }//GEN-LAST:event_registerAllMenuItemActionPerformed

    private void registerStudentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerStudentMenuItemActionPerformed
        String [] data = new String[]{"TYear",currentYear+"","Term",currentTerm+""};
        StudentRegistrationForm.main(data);
    }//GEN-LAST:event_registerStudentMenuItemActionPerformed

    private void byGradeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byGradeMenuItemActionPerformed
        String [] data = new String[]{"TYear",currentYear+"","Term",currentTerm+""};
        MarksCaptureForm.main(data);
    }//GEN-LAST:event_byGradeMenuItemActionPerformed

    private void marksByStudentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marksByStudentMenuItemActionPerformed
        registerStudentMenuItemActionPerformed(evt);
    }//GEN-LAST:event_marksByStudentMenuItemActionPerformed

    private void studentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentButtonActionPerformed
        studentMenuItemActionPerformed(evt);
    }//GEN-LAST:event_studentButtonActionPerformed

    private void subjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectButtonActionPerformed
        subjectMenuItemActionPerformed(evt);
    }//GEN-LAST:event_subjectButtonActionPerformed

    private void registerAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerAllButtonActionPerformed
        registerAllMenuItemActionPerformed(evt);
    }//GEN-LAST:event_registerAllButtonActionPerformed

    private void teacherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherButtonActionPerformed
       teacherMenuItemActionPerformed(evt);
    }//GEN-LAST:event_teacherButtonActionPerformed

    private void gradeTeacherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeTeacherButtonActionPerformed
        gradeTeacherMenuItemActionPerformed(evt);
    }//GEN-LAST:event_gradeTeacherButtonActionPerformed

    private void registerStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerStudentButtonActionPerformed
        registerStudentMenuItemActionPerformed(evt);
    }//GEN-LAST:event_registerStudentButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void gradeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeAllButtonActionPerformed
        byGradeMenuItemActionPerformed(evt);
    }//GEN-LAST:event_gradeAllButtonActionPerformed

    private void termCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termCreateButtonActionPerformed
        String [] data = new String[]{"TYear",currentYear+"","Term",currentTerm+""};
        RegistrationRepository regRepository = new RegistrationRepository();
        GradeTeacherRepository gtRepository = new GradeTeacherRepository();
        SubjectTeacherRepository subteachRepository = new SubjectTeacherRepository();
        StudentRepository studRepository = new StudentRepository();
        List<Registration> regTemp = new ArrayList<>();
        List<GradeTeacher> gtTemp = new ArrayList<>();
        List<SubjectTeacher> stTemp = new ArrayList<>();
        regTemp = regRepository.getRegistrationsByYearTerm(currentYear, currentTerm);
        gtTemp = gtRepository.getGradeTeachersByYearTerm(currentYear, currentTerm);
        stTemp = subteachRepository.getSubjectTeachersByYearTerm(currentYear, currentTerm);
        
        //Check if registration has been completed or even partially completed
        if(regTemp.isEmpty() && gtTemp.isEmpty() && stTemp.isEmpty()){
            if(dialogMessage("This is a very long process! Complete all sub-processes!! Collect all registration data and be ready!!\n"
                    + "Do you want to continue??", "Warning", JOptionPane.YES_NO_OPTION)){
                SubjectRepository subRepository = new SubjectRepository();
                TeacherRepository teachRepository = new TeacherRepository();
                List<Subject> subTemp = new ArrayList<>();
                List<Student> studTemp = new ArrayList<>();
                List<Teacher> teachTemp = new ArrayList<>();
                studTemp = studRepository.getAllActiveStudents();
                subTemp = subRepository.getAllSubjects();
                teachTemp = teachRepository.getAllTeachers();
                
                //Pre-Registration data such as Students, Teachers and Subjects should be captured
                if(studTemp.isEmpty() && subTemp.isEmpty() && teachTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have students before registration! Capture Student Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    StudentForm.main(data);
                    dialogMessage("PRE-REG 2: You need to have teachers before registration! Capture Teacher Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   TeacherForm.main(data);
                   dialogMessage("PRE-REG 3: You need to have subjectss before registration! Capture Subject Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   SubjectForm.main(data);
                }else if(studTemp.isEmpty() && subTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have students before registration! Capture Student Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    StudentForm.main(data);
                   dialogMessage("PRE-REG 2: You need to have subjectss before registration! Capture Subject Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   SubjectForm.main(data);
                }else if(subTemp.isEmpty() && teachTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have teachers before registration! Capture Teacher Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   TeacherForm.main(data);
                   dialogMessage("PRE-REG 2: You need to have subjects before registration! Capture Subject Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   SubjectForm.main(data); 
                }else if(studTemp.isEmpty() && teachTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have students before registration! Capture Student Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    StudentForm.main(data);
                    dialogMessage("PRE-REG 2: You need to have teachers before registration! Capture Teacher Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   TeacherForm.main(data);
                }else if(studTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have students before registration! Capture Student Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    StudentForm.main(data);
                }else if(subTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have subjects before registration! Capture Subject Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   SubjectForm.main(data); 
                }else if(teachTemp.isEmpty()){
                    dialogMessage("PRE-REG 1: You need to have teachers before registration! Capture Teacher Details!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   TeacherForm.main(data);
                }
                
                //Registration process step by step
                if(currentTerm != terms.get(0).getTerm()){
                    if(!studTemp.isEmpty()){
                        studRepository.graduateExitStudentsNewYear();
                        studRepository.changeStudentGradeNewYear();
                    }
                   dialogMessage("STEP 1: Change Only the status (rstatus to T) of all the transfering students!\n"
                           + "NOTE: Final year exit students have been updated already.\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   StudentForm.main(data);
                   dialogMessage("STEP 2: Capture the details of all new students, including those in entry Grade/Form!\n"
                           + "NOTE: If any of the Exit students are continuing, change their status (to A) and grade!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   StudentForm.main(data);
                   dialogMessage("STEP 3: Edit the Grade Teacher records for this term as neeeded!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   GradeTeacherForm.main(data);
                   dialogMessage("STEP 4: Edit the Subject Teacher records for this term as neeeded!\n"
                           + "NOTE: If you don't select a Teacher for a Subject, that record won't be saved!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   SubjectTeacherForm.main(data);
                   dialogMessage("STEP 5: This is the Final Step of Registering students for indivitual subjects!\n"
                           + "NOTE: This process will open other forms to complete specialized registrations!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   RegistrationForm.main(data);
               }else{
                   dialogMessage("STEP 1: Change Only the status (rstatus to T) of all the transfering students!\n"
                           + "NOTE: Final year exit students have been updated already.\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   StudentForm.main(data);
                   dialogMessage("STEP 2: Capture the details of all new students, including those in entry Grade/Form!\n"
                           + "NOTE: If any of the Exit students are continuing, change their status (to A) and grade!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   StudentForm.main(data);
                   dialogMessage("STEP 3: Edit the Grade Teacher records for this term as neeeded!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   GradeTeacherForm.main(data);
                   dialogMessage("STEP 4: Edit the Subject Teacher records for this term as neeeded!\n"
                           + "NOTE: If you don't select a Teacher for a Subject, that record won't be saved!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   SubjectTeacherForm.main(data);
                   dialogMessage("STEP 5: This is the Final Step of Registering students for indivitual subjects!\n"
                           + "NOTE: This process will open other forms to complete specialized registrations!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   RegistrationForm.main(data);
               }
           }else{
               dialogMessage("This is the recommended way of creating a term!!\nComplete the process when you are ready!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            if(regTemp.isEmpty() || gtTemp.isEmpty() || stTemp.isEmpty()){
                dialogMessage("Registration is partially completed!! This may result in inconsistent data!!\n"
                        + "It is recommended tha you use the Create Term Registration Button for a new term always!!\n"
                        + "However, we will guide you through completing this registration!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                if(gtTemp.isEmpty()){
                   dialogMessage("STEP 1: Edit the Grade Teacher records for this term as neeeded!\n"
                           + "NOTE: Close the form when done!", "Information", JOptionPane.INFORMATION_MESSAGE);
                   GradeTeacherForm.main(data); 
                }
                if(stTemp.isEmpty()){
                    dialogMessage("STEP 1: Edit the Subject Teacher records for this term as neeeded!\n"
                           + "NOTE: If you don't select a Teacher for a Subject, that record won't be saved!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    SubjectTeacherForm.main(data);
                }
                if(regTemp.isEmpty()){
                    dialogMessage("STEP 1: This is the Final Step of Registering students for indivitual subjects!\n"
                           + "NOTE: This process will open other forms to complete specialized registrations!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    RegistrationForm.main(data);
                }
            }else{
               dialogMessage("Term Registrations have been completed!! You don't need to redo this process!!", "Information", JOptionPane.INFORMATION_MESSAGE); 
            }
        }
    }//GEN-LAST:event_termCreateButtonActionPerformed

    private void subjectTeacherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectTeacherButtonActionPerformed
        subjectTeacherMenuItemActionPerformed(evt);
    }//GEN-LAST:event_subjectTeacherButtonActionPerformed
    
    private boolean dialogMessage(String message, String title, int messageType){
        int response = JOptionPane.OK_OPTION;
        if(messageType == JOptionPane.YES_NO_OPTION || messageType == JOptionPane.OK_CANCEL_OPTION){
            response = JOptionPane.showConfirmDialog(this, message, title, messageType);
        }else{
            JOptionPane.showMessageDialog(this, message, title, messageType);
        }
        return (response == JOptionPane.OK_OPTION);
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JMenuItem byGradeMenuItem;
    private javax.swing.JMenu dataMenu;
    private javax.swing.JButton gradeAllButton;
    private javax.swing.JButton gradeTeacherButton;
    private javax.swing.JMenuItem gradeTeacherMenuItem;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headingPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel leftChartPanel;
    private javax.swing.JPanel mainChartPanel;
    private javax.swing.JMenuItem marksByStudentMenuItem;
    private javax.swing.JMenu marksMenu;
    private javax.swing.JButton registerAllButton;
    private javax.swing.JMenuItem registerAllMenuItem;
    private javax.swing.JMenu registerMenu;
    private javax.swing.JButton registerStudentButton;
    private javax.swing.JMenuItem registerStudentMenuItem;
    private javax.swing.JMenu registrationMenu;
    private javax.swing.JPanel rightChartPanel;
    private javax.swing.JButton settingsButton;
    private javax.swing.JButton studentButton;
    private javax.swing.JMenuItem studentMenuItem;
    private javax.swing.JButton subjectButton;
    private javax.swing.JMenuItem subjectMenuItem;
    private javax.swing.JButton subjectTeacherButton;
    private javax.swing.JMenuItem subjectTeacherMenuItem;
    private javax.swing.JButton teacherButton;
    private javax.swing.JMenuItem teacherMenuItem;
    private javax.swing.JButton termCreateButton;
    private javax.swing.JComboBox<String> txtTYear;
    private javax.swing.JComboBox<String> txtTYear2;
    private javax.swing.JComboBox<String> txtTerm;
    private javax.swing.JComboBox<String> txtTerm2;
    // End of variables declaration//GEN-END:variables

}

