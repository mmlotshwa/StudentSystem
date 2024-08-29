/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem.gui;
    
import com.mandla.studentsystem.repositories.TeacherRepository;
import com.mandla.studentsystem.entities.Teacher;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;

/**
 *
 * @author mlots
 */
public class TeacherForm extends javax.swing.JFrame {
    private List<Teacher> teachers;
    private List<Teacher> allTeachers;
    private final TeacherRepository repository;
    private int currentRec = - 1;
    private int numRecords = -1;
    private enum Status {VIEW, EDIT, NEW};
    private Status viewStatus;
    private List<String> departments;
    private boolean updated;
    
    /**
     * Creates new form TeacherForm
     */
    public TeacherForm() {
        initComponents();
        repository = new TeacherRepository();
        allTeachers = new ArrayList<>();
        
        departments = new ArrayList<>();
        
        departments = repository.getAllDepartments();
        departments.addFirst("All");
        DefaultComboBoxModel departmentNames = new DefaultComboBoxModel(departments.toArray());
        deptComboBox.setModel(departmentNames);
        loadData();
    }
    
     private void loadListModel() {
        DefaultListModel<String> teacherListModel = new DefaultListModel<>();
        for(Teacher teacher : teachers){
            teacherListModel.addElement(teacher.toString());
        }
        teacherList.setModel(teacherListModel);
    }
     
    private void buttonStatus(){    
        if(viewStatus == Status.NEW || viewStatus == Status.EDIT){
            headingPanelFormat();
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            headingLabel.setText(((viewStatus == Status.NEW)?"New Teacher":"Edit Teacher"));
            dataFieldStatus(true);
            
        }else{
            headingPanelFormat();
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            headingLabel.setText("View Teacher");
            dataFieldStatus(false);
            if(currentRec == -1){
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        }
    }
    
    private void dataFieldStatus(boolean status){
        txtTeacherID.setEnabled(status);
        txtFirstName.setEnabled(status);
        txtSurname.setEnabled(status);
        txtTitle.setEnabled(status);
        txtGender.setEnabled(status);
        txtDepartment.setEnabled(status);
        txtEmail.setEnabled(status);
    }
    
    private void navButtonStatus(boolean status){
        firstButton.setEnabled(status);
        previousButton.setEnabled(status);
        nextButton.setEnabled(status);
        lastButton.setEnabled(status);
    }
    
    private void navigationButtons(){
        if(currentRec == -1){
            navButtonStatus(false);
        }else{
            navButtonStatus(true);
        }
        
        if(currentRec == 0){
            firstButton.setEnabled(false);
            previousButton.setEnabled(false);
        }
        if(currentRec == numRecords - 1){
            lastButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }
    private void loadData(){
        teachers = new ArrayList<>();
        try{
            allTeachers = repository.getAllTeachers();

            if(!allTeachers.isEmpty()){
                teachers = allTeachers;
               loadListModel();
               numRecords = teachers.size();
               currentRec=0;
               updateDisplay(teachers.get(currentRec));
               viewStatus = Status.VIEW;
               updated = true;
            }else{
                dialogMessage("You currently have no teachers registered! Capture Teacher Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
                viewStatus = Status.NEW;
            }
            buttonStatus();
            navigationButtons();
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDisplay(Teacher teacher){
        txtTeacherID.setText(Integer.toString(teacher.getTeacherid()));
        txtFirstName.setText(teacher.getFirstName());
        txtSurname.setText(teacher.getSurname());
        txtTitle.setText(teacher.getTitle());
        txtGender.setText(teacher.getGender());
        txtDepartment.setText(teacher.getDepartment());
        txtEmail.setText(teacher.getEmail());
        recordLabel.setText(Integer.toString(currentRec + 1) + " of " + Integer.toString(numRecords));
        navigationButtons();
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
        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        bodyPanel = new javax.swing.JPanel();
        txtFirstName = new javax.swing.JTextField();
        txtTitle = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSurname = new javax.swing.JTextField();
        txtGender = new javax.swing.JTextField();
        txtTeacherID = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        deptComboBox = new javax.swing.JComboBox<>();
        studentsScrollPane = new javax.swing.JScrollPane();
        teacherList = new javax.swing.JList<>();
        headingPanel = new javax.swing.JPanel();
        headingLabel = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        firstButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        lastButton = new javax.swing.JButton();
        recordLabel = new javax.swing.JLabel();
        newButton = new javax.swing.JButton();

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.setText("jTextField1");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("StudentID:");

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(java.awt.SystemColor.control);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        headerPanel.setBackground(new java.awt.Color(51, 51, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Teacher Data Form");

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        bodyPanel.setBackground(new java.awt.Color(51, 220, 246));

        txtFirstName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFirstName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFirstNameFocusLost(evt);
            }
        });

        txtTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTitleFocusLost(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Gender:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Title:");

        txtSurname.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSurname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSurnameFocusLost(evt);
            }
        });

        txtGender.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGender.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGenderFocusLost(evt);
            }
        });

        txtTeacherID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Dept:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Surname:");

        txtDepartment.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDepartment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepartmentFocusLost(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("TeacherID:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("First Name:");

        deptComboBox.setBackground(new java.awt.Color(51, 153, 255));
        deptComboBox.setToolTipText("Filter by Grade!");
        deptComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptComboBoxActionPerformed(evt);
            }
        });

        teacherList.setBackground(new java.awt.Color(51, 153, 255));
        teacherList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        teacherList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                teacherListValueChanged(evt);
            }
        });
        studentsScrollPane.setViewportView(teacherList);

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        headingLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        headingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        headingLabel.setText("View Teacher");

        deleteButton.setBackground(new java.awt.Color(51, 153, 255));
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/delete.png"))); // NOI18N
        deleteButton.setBorder(null);
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/deledisabled.png"))); // NOI18N
        deleteButton.setMaximumSize(new java.awt.Dimension(48, 48));
        deleteButton.setMinimumSize(new java.awt.Dimension(48, 48));
        deleteButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/deletehover.png"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/edit.png"))); // NOI18N
        editButton.setBorder(null);
        editButton.setBorderPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/editdisabled.png"))); // NOI18N
        editButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/edithover.png"))); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/save.png"))); // NOI18N
        saveButton.setBorder(null);
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/savedisabled.png"))); // NOI18N
        saveButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/savehover.png"))); // NOI18N
        saveButton.setVisible(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/cancel.png"))); // NOI18N
        cancelButton.setBorder(null);
        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/candeldsiabled.png"))); // NOI18N
        cancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/candelhover.png"))); // NOI18N
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headingPanelLayout = new javax.swing.GroupLayout(headingPanel);
        headingPanel.setLayout(headingPanelLayout);
        headingPanelLayout.setHorizontalGroup(
            headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton)
                .addGap(42, 42, 42)
                .addComponent(editButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        headingPanelLayout.setVerticalGroup(
            headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(headingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saveButton))
                    .addGroup(headingPanelLayout.createSequentialGroup()
                        .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancelButton)
                            .addComponent(editButton))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 153, 242));

        firstButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/first.png"))); // NOI18N
        firstButton.setBorder(null);
        firstButton.setBorderPainted(false);
        firstButton.setContentAreaFilled(false);
        firstButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/firstdisabled.png"))); // NOI18N
        firstButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/firsthover.png"))); // NOI18N
        firstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstButtonActionPerformed(evt);
            }
        });

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/previous.png"))); // NOI18N
        previousButton.setBorder(null);
        previousButton.setBorderPainted(false);
        previousButton.setContentAreaFilled(false);
        previousButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/previousdisabled.png"))); // NOI18N
        previousButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/previoushover.png"))); // NOI18N
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/next.png"))); // NOI18N
        nextButton.setBorder(null);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/nextdisabled.png"))); // NOI18N
        nextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/nexthover.png"))); // NOI18N
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        lastButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/last.png"))); // NOI18N
        lastButton.setBorder(null);
        lastButton.setBorderPainted(false);
        lastButton.setContentAreaFilled(false);
        lastButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/lastdisabled.png"))); // NOI18N
        lastButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/lasthover.png"))); // NOI18N
        lastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastButtonActionPerformed(evt);
            }
        });

        recordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        recordLabel.setText("recordLabel");
        recordLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(firstButton)
                .addGap(50, 50, 50)
                .addComponent(previousButton)
                .addGap(74, 74, 74)
                .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(71, 71, 71)
                .addComponent(nextButton)
                .addGap(50, 50, 50)
                .addComponent(lastButton)
                .addGap(66, 66, 66))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(previousButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(studentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(deptComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(0, 56, Short.MAX_VALUE)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(bodyPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(bodyPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtTeacherID))
                                        .addGroup(bodyPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bodyPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(bodyPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(bodyPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(6, 6, 6))
                    .addComponent(headingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(deptComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(studentsScrollPane))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addComponent(headingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTeacherID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        newButton.setBackground(new java.awt.Color(51, 51, 255));
        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addteacher.png"))); // NOI18N
        newButton.setBorder(null);
        newButton.setBorderPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addteacherhover.png"))); // NOI18N
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                        .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch)
                            .addGroup(headerPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bodyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        viewStatus = Status.NEW;
        txtTeacherID.setText("");
        txtFirstName.setText("");
        txtSurname.setText("");
        txtTitle.setText("");
        if(deptComboBox.getSelectedItem().toString().trim().equalsIgnoreCase("All"))
            txtDepartment.setText("");
        else
            txtDepartment.setText(deptComboBox.getSelectedItem().toString().trim());
        txtEmail.setText("");
        txtGender.setText("");
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
        txtTeacherID.setEnabled(false);
    }//GEN-LAST:event_newButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if(!updated){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                viewStatus = Status.VIEW;
                buttonStatus();
                navigationButtons();
                System.out.println("Current Record: " + teachers.get(currentRec));
                updateDisplay(teachers.get(currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            viewStatus = Status.VIEW;
            buttonStatus();
            navigationButtons();
            System.out.println("Current Record: " + teachers.get(currentRec));
            updateDisplay(teachers.get(currentRec));
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try{
            if(viewStatus == Status.EDIT){
                teachers.add(currentRec, repository.updateTeacher(teachers.get(currentRec)));
                viewStatus = Status.VIEW;
            }else{
                Teacher teacher = new Teacher(txtTitle.getText().trim(), txtFirstName.getText().trim(), txtSurname.getText().trim(), txtGender.getText().trim(), 
                        txtEmail.getText().trim(), txtDepartment.getText().trim());
                repository.addTeacher(teacher);
                if(!teachers.contains(teacher)){
                    teachers.add(teacher);
                    allTeachers.add(teacher);
                }
            }
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
            teachers = new ArrayList<>(allTeachers);
        }
        loadListModel();
        numRecords = teachers.size();
        currentRec=teachers.size() - 1;
        updateDisplay(teachers.get(currentRec));
        viewStatus = Status.VIEW;
        updated = true;
    }//GEN-LAST:event_saveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        viewStatus = Status.EDIT;
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
        txtTeacherID.setEnabled(false);
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        Teacher teacher = teachers.get(currentRec);
        repository.deleteTeacher(teacher);
        if(teachers.contains(teacher)){
            teachers.remove(teacher);
            allTeachers.remove(teacher);
        }
        updateDisplay(teachers.get(--currentRec));
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        if(!updated){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                updateDisplay(teachers.get(--currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            updateDisplay(teachers.get(--currentRec));
        }
    }//GEN-LAST:event_previousButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        if(!updated){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                currentRec = 0;
                updateDisplay(teachers.get(currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            currentRec = 0;
            updateDisplay(teachers.get(currentRec));
        }
    }//GEN-LAST:event_firstButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if(!updated){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                updateDisplay(teachers.get(++currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            updateDisplay(teachers.get(++currentRec));
        }
        
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        if(!updated){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                currentRec = numRecords - 1;
                updateDisplay(teachers.get(currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            currentRec = numRecords - 1;
            updateDisplay(teachers.get(currentRec));
        }
    }//GEN-LAST:event_lastButtonActionPerformed

    private void txtDepartmentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepartmentFocusLost
        teachers.get(currentRec).setDepartment(txtDepartment.getText().trim());
        updated = false;
    }//GEN-LAST:event_txtDepartmentFocusLost

    private void txtGenderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGenderFocusLost
        teachers.get(currentRec).setGender(txtGender.getText().trim().toUpperCase());
        updated = false;
    }//GEN-LAST:event_txtGenderFocusLost

    private void txtTitleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTitleFocusLost
        teachers.get(currentRec).setTitle(txtTitle.getText().trim());
        updated = false;
    }//GEN-LAST:event_txtTitleFocusLost

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        teachers.get(currentRec).setEmail(txtEmail.getText().trim());
        updated = false;
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtSurnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSurnameFocusLost
        teachers.get(currentRec).setSurname(txtSurname.getText());
        updated = false;
    }//GEN-LAST:event_txtSurnameFocusLost

    private void txtFirstNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFirstNameFocusLost
       teachers.get(currentRec).setFirstName(txtFirstName.getText());
       updated = false;
    }//GEN-LAST:event_txtFirstNameFocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(!updated){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                repository.closeSession();
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            repository.closeSession();
        }
        
    }//GEN-LAST:event_formWindowClosing

    private void teacherListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_teacherListValueChanged
        if(!teachers.isEmpty()){
            if(!teacherList.isSelectionEmpty()){
                currentRec = teacherList.getSelectedIndex();
                updateDisplay(teachers.get(currentRec));
            }else{
                currentRec = 0;
                updateDisplay(teachers.get(currentRec));
            }
        }
    }//GEN-LAST:event_teacherListValueChanged

    private void deptComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptComboBoxActionPerformed
        String dept = deptComboBox.getSelectedItem().toString().trim();
        
        if (!teacherList.isEnabled()) 
            teacherList.setEnabled(true);
        if(dept.equalsIgnoreCase("All")){
            loadData();
        }else{
            try{
            teachers = repository.getTeachersByDept(dept);
            
            if(!teachers.isEmpty()){
                numRecords = teachers.size();
                loadListModel();
                currentRec = 0;
                updateDisplay(teachers.get(currentRec));
            }else{
                dialogMessage("You don't have teachers in " + dept + ". Capture Teacher Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
                viewStatus = Status.NEW;
                loadData();
                teacherList.setEnabled(false);
                newButtonActionPerformed(new java.awt.event.ActionEvent(this,1,"New"));
            }
            }catch(HibernateException he){
                dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
                loadData();
            }
        }
    }//GEN-LAST:event_deptComboBoxActionPerformed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        List<Teacher> newteachers = new ArrayList<>();
        String searchKey = txtSearch.getText().trim().toLowerCase();
        if(!searchKey.isEmpty()){
            for(Teacher teacher : teachers){
                if(teacher.getSurname().toLowerCase().startsWith(searchKey))
                    newteachers.add(teacher);
            }
            if(!newteachers.isEmpty()){
                teachers = newteachers;
                numRecords = teachers.size();
                loadListModel();
                currentRec = 0;
                updateDisplay(teachers.get(currentRec));
            }
        }else{
            loadData();
        }
    }//GEN-LAST:event_txtSearchKeyTyped
    
    private boolean dialogMessage(String message, String title, int messageType){
        int response = JOptionPane.OK_OPTION;
        if(messageType == JOptionPane.YES_NO_OPTION || messageType == JOptionPane.OK_CANCEL_OPTION){
            response = JOptionPane.showConfirmDialog(this, message, title, messageType);
        }else{
            JOptionPane.showMessageDialog(this, message, title, messageType);
        }
        return (response == JOptionPane.OK_OPTION);
    }
    
    private void headingPanelFormat(){
        headingPanel.removeAll();
        if(viewStatus == Status.VIEW){
            javax.swing.GroupLayout headingPanelLayout = new javax.swing.GroupLayout(headingPanel);
            headingPanel.setLayout(headingPanelLayout);
            headingPanelLayout.setHorizontalGroup(
                headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(saveButton)
                    .addGap(42, 42, 42)
                    .addComponent(editButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            headingPanelLayout.setVerticalGroup(
                headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(headingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveButton))
                        .addGroup(headingPanelLayout.createSequentialGroup()
                            .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cancelButton)
                                .addComponent(editButton))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
        }else{
            javax.swing.GroupLayout headingPanelLayout = new javax.swing.GroupLayout(headingPanel);
            headingPanel.setLayout(headingPanelLayout);
            headingPanelLayout.setHorizontalGroup(
                headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(deleteButton)
                    .addGap(42, 42, 42)
                    .addComponent(cancelButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            headingPanelLayout.setVerticalGroup(
                headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(headingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveButton))
                        .addGroup(headingPanelLayout.createSequentialGroup()
                            .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cancelButton)
                                .addComponent(editButton))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
        }
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
            java.util.logging.Logger.getLogger(TeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TeacherForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JComboBox<String> deptComboBox;
    private javax.swing.JButton editButton;
    private javax.swing.JButton firstButton;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JPanel headingPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton lastButton;
    private javax.swing.JButton newButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane studentsScrollPane;
    private javax.swing.JList<String> teacherList;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtGender;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSurname;
    private javax.swing.JTextField txtTeacherID;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables
}
