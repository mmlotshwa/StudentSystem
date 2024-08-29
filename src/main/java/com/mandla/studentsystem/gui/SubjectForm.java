/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem.gui;
    
import com.mandla.studentsystem.repositories.SubjectRepository;
import com.mandla.studentsystem.entities.Subject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;

/**
 *
 * @author mlots
 */
public class SubjectForm extends javax.swing.JFrame {
    private List<Subject> subjects;
    private List<Subject> allSubjects;
    private final SubjectRepository repository;
    private int currentRec = - 1;
    private int numRecords = -1;
    private enum Status {VIEW, EDIT, NEW};
    private Status viewStatus;
    private final List<String> departments;
    private boolean pending;
    /**
     * Creates new form SubjectForm
     */
    public SubjectForm() {
        initComponents();
        pending=false;
        repository = new SubjectRepository();
        allSubjects = new ArrayList<>();
        departments = repository.getAllDepartments();
        departments.addFirst("All");
        DefaultComboBoxModel deptNames = new DefaultComboBoxModel(departments.toArray());
        deptComboBox.setModel(deptNames);
        loadData();
        ArrayList<Field> colNames = new ArrayList<>();
        colNames = repository.getColumnNames();
        
        for(Field colName : colNames){
            try {
                Method method = Subject.class.getDeclaredMethod("get" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1));
                try {
                    System.out.println(method.invoke(subjects.get(0)));
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(SubjectForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(SubjectForm.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        }
    }
    
     private void loadListModel() {
        DefaultListModel<String> subjectListModel = new DefaultListModel<>();
        for(Subject subject : subjects){
            subjectListModel.addElement(subject.toString());
        }
        subjectList.setModel(subjectListModel);
    }
     
    private void buttonStatus(){    
        if(viewStatus == Status.NEW || viewStatus == Status.EDIT){
            headingPanelFormat();
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            headingLabel.setText(((viewStatus == Status.NEW)?"New Subject":"Edit Subject"));
            dataFieldStatus(true);
            
        }else{
            headingPanelFormat();
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            headingLabel.setText("View Subject");
            dataFieldStatus(false);
            if(currentRec == -1){
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        }
    }
    
    private void dataFieldStatus(boolean status){
        txtSubjectID.setEnabled(status);
        txtTitle.setEnabled(status);
        txtDepartment.setEnabled(status);
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
        subjects = new ArrayList<>();
        try{
            allSubjects = repository.getAllSubjects();

            if(!allSubjects.isEmpty()){
                subjects = allSubjects;
               loadListModel();
               numRecords = subjects.size();
               currentRec=0;
               updateDisplay(subjects.get(currentRec));
               viewStatus = Status.VIEW;
            }else{
                dialogMessage("You currently have no subjects captured! Capture Subject Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
                viewStatus = Status.NEW;
            }
            buttonStatus();
            navigationButtons();
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDisplay(Subject subject){
        txtSubjectID.setText(Integer.toString(subject.getSubjectid()));
        txtTitle.setText(subject.getTitle());
        txtDepartment.setText(subject.getDepartment());
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
        txtTitle = new javax.swing.JTextField();
        txtDepartment = new javax.swing.JTextField();
        txtSubjectID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        deptComboBox = new javax.swing.JComboBox<>();
        studentsScrollPane = new javax.swing.JScrollPane();
        subjectList = new javax.swing.JList<>();
        headingPanel = new javax.swing.JPanel();
        headingLabel = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Subject Data Form");
        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        bodyPanel.setBackground(new java.awt.Color(51, 220, 246));

        txtTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTitleFocusLost(evt);
            }
        });

        txtDepartment.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDepartment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepartmentFocusLost(evt);
            }
        });

        txtSubjectID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSubjectID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubjectIDFocusLost(evt);
            }
        });

        jLabel5.setText("Department:");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setText("SubjectID:");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setText("Title:");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        deptComboBox.setBackground(new java.awt.Color(51, 153, 255));
        deptComboBox.setToolTipText("Filter by Grade!");
        deptComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptComboBoxActionPerformed(evt);
            }
        });

        subjectList.setBackground(new java.awt.Color(51, 153, 255));
        subjectList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        subjectList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                subjectListValueChanged(evt);
            }
        });
        studentsScrollPane.setViewportView(subjectList);

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        headingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        headingLabel.setText("View Subject");
        headingLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/delete.png"))); // NOI18N
        deleteButton.setBackground(new java.awt.Color(51, 153, 255));
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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)))
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                            .addComponent(txtDepartment)
                            .addComponent(txtSubjectID))
                        .addGap(37, 37, 37))
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
                            .addComponent(txtSubjectID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addsubject.png"))); // NOI18N
        newButton.setBackground(new java.awt.Color(51, 51, 255));
        newButton.setBorder(null);
        newButton.setBorderPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/addsubjecthover.png"))); // NOI18N
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                        .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        txtSubjectID.setText("");
        txtTitle.setText("");
        if(deptComboBox.getSelectedItem().toString().trim().equalsIgnoreCase("All"))
            txtDepartment.setText("");
        else
            txtDepartment.setText(deptComboBox.getSelectedItem().toString().trim());
        pending = true;
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
    }//GEN-LAST:event_newButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              viewStatus = Status.VIEW;
                buttonStatus();
                navigationButtons();
                updateDisplay(subjects.get(currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            viewStatus = Status.VIEW;
            buttonStatus();
            navigationButtons();
            updateDisplay(subjects.get(currentRec));
        }  
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try{
            if(viewStatus == Status.EDIT){
                subjects.add(currentRec, repository.updateSubject(subjects.get(currentRec)));
                viewStatus = Status.VIEW;
            }else{
                Subject subject = new Subject(Integer.parseInt(txtSubjectID.getText()),txtTitle.getText().trim(), txtDepartment.getText().trim());
                repository.addSubject(subject);
                allSubjects.add(subject);
                subjects.add(subject);
                numRecords = subjects.size();
            }
            pending = false;
        }catch(HibernateException he){
            dialogMessage("Database Savinf Error: " + he, "Error", JOptionPane.ERROR_MESSAGE);
            subjects = allSubjects;  
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        viewStatus = Status.EDIT;
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        Subject subject = subjects.get(currentRec);
        repository.deleteSubject(subject);
        if(subjects.contains(subject)){
            subjects.remove(subject);
            allSubjects.remove(subject);
        }
        numRecords = subjects.size();
        loadListModel();
        updateDisplay(subjects.get(--currentRec));
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              updateDisplay(subjects.get(--currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            updateDisplay(subjects.get(--currentRec));
        }    
    }//GEN-LAST:event_previousButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                currentRec = 0;
                updateDisplay(subjects.get(currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            currentRec = 0;
            updateDisplay(subjects.get(currentRec));
        }
    }//GEN-LAST:event_firstButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              updateDisplay(subjects.get(++currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            updateDisplay(subjects.get(++currentRec));
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                currentRec = numRecords - 1;
                updateDisplay(subjects.get(currentRec));
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            currentRec = numRecords - 1;
            updateDisplay(subjects.get(currentRec));
        }
        
    }//GEN-LAST:event_lastButtonActionPerformed

    private void txtDepartmentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepartmentFocusLost
        subjects.get(currentRec).setDepartment(txtDepartment.getText());
        pending=true;
    }//GEN-LAST:event_txtDepartmentFocusLost

    private void txtTitleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTitleFocusLost
       subjects.get(currentRec).setTitle(txtTitle.getText());
       pending=true;
    }//GEN-LAST:event_txtTitleFocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              repository.closeSession();
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            repository.closeSession();
        }
    }//GEN-LAST:event_formWindowClosing

    private void subjectListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_subjectListValueChanged
        if(pending){
            if (dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)) {
                if (!subjects.isEmpty()) {
                    if (!subjectList.isSelectionEmpty()) {
                        currentRec = subjectList.getSelectedIndex();
                        updateDisplay(subjects.get(currentRec));
                    } else {
                        currentRec = 0;
                        updateDisplay(subjects.get(currentRec));
                    }
                }
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else {
            if (!subjects.isEmpty()) {
                if (!subjectList.isSelectionEmpty()) {
                    currentRec = subjectList.getSelectedIndex();
                    updateDisplay(subjects.get(currentRec));
                } else {
                    currentRec = 0;
                    updateDisplay(subjects.get(currentRec));
                }
            }
        }
    }//GEN-LAST:event_subjectListValueChanged

    private void deptComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptComboBoxActionPerformed
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              deptComboAction();
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            deptComboAction();
        }
    }//GEN-LAST:event_deptComboBoxActionPerformed

    private void deptComboAction(){
        String dept = deptComboBox.getSelectedItem().toString().trim();
        
        if (!subjectList.isEnabled()) 
            subjectList.setEnabled(true);
        if(dept.equalsIgnoreCase("All")){
            subjects = new ArrayList<>(allSubjects);
        }else{
            subjects = new ArrayList<>();
            for(Subject subject : allSubjects){
                if(subject.getDepartment().equalsIgnoreCase(dept)){
                    subjects.add(subject);
                }
            }
        }
        
        if(!subjects.isEmpty()){
            numRecords = subjects.size();
            loadListModel();
            currentRec = 0;
            updateDisplay(subjects.get(currentRec));
        }else{
            dialogMessage("You don't have subjects in " + dept + ". Capture Subject Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
            viewStatus = Status.NEW;
            subjects = new ArrayList<>(allSubjects);
            numRecords = subjects.size();
            loadListModel();
            newButtonActionPerformed(new java.awt.event.ActionEvent(this,1,"New"));
        }
    }
    
    private void txtSubjectIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubjectIDFocusLost
        try{
            subjects.get(currentRec).setSubjectid(Integer.parseInt(txtSubjectID.getText()));
            pending=true;
        }catch(NumberFormatException nfe){
            dialogMessage("You did not type in an Integer SubjectID. Please Correct!", "Error", JOptionPane.ERROR_MESSAGE);
            txtSubjectID.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtSubjectIDFocusLost

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        if(pending){
           if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              searchKeyAction();
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            searchKeyAction();
        }
    }//GEN-LAST:event_txtSearchKeyTyped
    
    private void searchKeyAction(){
        String searchKey = txtSearch.getText().trim().toLowerCase();
        if(!searchKey.isEmpty()){
            subjects = new ArrayList<>();
            for(Subject subject : allSubjects){
                if(subject.getTitle().toLowerCase().startsWith(searchKey))
                    subjects.add(subject);
            }
            
        }else{
            subjects = new ArrayList<>(allSubjects);
        }
        if(!subjects.isEmpty()){
            numRecords = subjects.size();
            loadListModel();
            currentRec = 0;
            updateDisplay(subjects.get(currentRec));
        }
    }
    
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
            java.util.logging.Logger.getLogger(SubjectForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SubjectForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SubjectForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SubjectForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SubjectForm().setVisible(true);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton lastButton;
    private javax.swing.JButton newButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane studentsScrollPane;
    private javax.swing.JList<String> subjectList;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSubjectID;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables
}
