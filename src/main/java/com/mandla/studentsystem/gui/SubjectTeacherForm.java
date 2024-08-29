/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem.gui;

import com.mandla.studentsystem.repositories.SubjectSchedulesRepository;
import com.mandla.studentsystem.repositories.TermSystemRepository;
import com.mandla.studentsystem.repositories.TeacherRepository;
import com.mandla.studentsystem.repositories.SubjectRepository;
import com.mandla.studentsystem.repositories.GradeNameRepository;
import com.mandla.studentsystem.repositories.SubjectTeacherRepository;
import com.mandla.studentsystem.entities.Subject;
import com.mandla.studentsystem.entities.SubjectSchedules;
import com.mandla.studentsystem.entities.GradeName;
import com.mandla.studentsystem.entities.Teacher;
import com.mandla.studentsystem.entities.SubjectTeacher;
import com.mandla.studentsystem.entities.TermSystem;
import com.mandla.studentsystem.utils.TableFormat;
import com.mandla.studentsystem.utils.DataTableModel;
import java.awt.event.ActionEvent;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.hibernate.HibernateException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mlots
 */
public class SubjectTeacherForm extends javax.swing.JFrame {
    private List<SubjectTeacher> subjectTeachers;
    private List<SubjectTeacher> allSubjectTeachers;
    private final SubjectTeacherRepository repository;
    private int currentRec = - 1;
    private int numRecords = -1;
    private enum Status {VIEW, EDIT, NEW};
    private Status viewStatus;
    private List<GradeName> grades;
    private ArrayList<Field> colNames;
    private ArrayList<Method> colMethods;
    private ArrayList<Method> colSetMethods;
    private DataTableModel<SubjectTeacher> dataModel;
    private TableRowSorter<TableModel> sorter;
    private JTable resultTable;
    private List<Teacher> teacherData;
    private List<Subject> subjectData;
    private int currentYear;
    private int currentTerm;
    private List<TermSystem> terms;
    private Map<String,Integer> teachers;
    private Map<String,Integer> subjects;
    private Map<Integer,Subject> subjectInfo;
    JComboBox teacherCombo; //For allowing the user to select the teacher on the table
    JComboBox gradeCombo; //For allowing the user to change the grade on the table
    JComboBox subjectCombo; //For allowing the user to change the subject on the table
    private boolean updated; //Flag for all records status; true-if records read from database, false-if records created for new term
    private boolean pending; //Flag to show records set for update
    private final Map<Integer,SubjectTeacher> pendingRecords; //To store updated or added records while updated is true and pending false
    
    
    /**
     * Creates new form SubjectTeacherForm
     */
    public SubjectTeacherForm() {
        initComponents();
        updated = pending = false;
        pendingRecords = new HashMap<>();
        repository = new SubjectTeacherRepository();
        allSubjectTeachers = new ArrayList<>();
        createTeacherSearchMap();
        loadComboBoxes();
        loadData(); 
        buttonStatus();
        navigationButtons();
        activateListeners();
    }
    
    private void loadComboBoxes(){
        LocalDate currentDate = LocalDate.now();
        
        GradeNameRepository gradeRepository = new GradeNameRepository();
        grades = new ArrayList<>();
        
        grades = gradeRepository.getAllGrades();
        grades.addFirst(new GradeName("All"));
        DefaultComboBoxModel gradeNames = new DefaultComboBoxModel(grades.toArray());
        gradeComboBox.setModel(gradeNames);
        
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
        
    }
    
    private void loadListModel() {
        DefaultListModel<String> subjectTeacherListModel = new DefaultListModel<>();
        for(SubjectTeacher subjectTeacher : subjectTeachers){
            subjectTeacherListModel.addElement(subjectTeacher.toString());
        }
        subjectTeacherList.setModel(subjectTeacherListModel);
    }
    
    private void activateListeners(){
        txtTerm.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTermActionPerformed(evt);
        });
        
        txtTYear.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTYearActionPerformed(evt);
        });
    }
    
    private void createTeacherSearchMap(){
        teacherData = new ArrayList<>();
        subjectData = new ArrayList<>();
        try{
            TeacherRepository teachRepository = new TeacherRepository();
            teacherData = teachRepository.getAllTeachers();

            teacherData.sort(Comparator.comparing(Teacher::getSurname).thenComparing(Teacher::getFirstName));
            teachers = new HashMap<>();
            for(Teacher teacher : teacherData){
                teachers.put(teacher.getSurname() + ", " + teacher.getFirstName(), teacher.getTeacherid());
            }
            
            SubjectRepository subjectRepository = new SubjectRepository();
            subjectData = subjectRepository.getAllSubjects();

            subjectData.sort(Comparator.comparing(Subject::getDepartment).thenComparing(Subject::getTitle));
            subjects = new HashMap<>();
            subjectInfo = new HashMap<>();
            for(Subject subject : subjectData){
                subjects.put(subject.toString().trim(), subject.getSubjectid());
                subjectInfo.put(subject.getSubjectid(), subject);
            }
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTableModel(){
        dataModel = new DataTableModel<>(subjectTeachers,colNames,colMethods,colSetMethods,teachers,subjects);
        resultTable = new JTable(dataModel);
        sorter = new TableRowSorter<>(dataModel);
        resultTable.setRowSorter(sorter);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DefaultComboBoxModel teacherModel = new DefaultComboBoxModel(teacherData.toArray());
        
        teacherCombo = new JComboBox(teacherModel);
        
        teacherCombo.addActionListener(evt -> {
            this.teacherComboActionPerformed(evt);
        });
        
        List<GradeName> gradesTemp = new ArrayList<>(grades);
        gradesTemp.removeFirst();
        
        DefaultComboBoxModel gradesModel = new DefaultComboBoxModel(gradesTemp.toArray());
        
        gradeCombo = new JComboBox(gradesModel);
        
        gradeCombo.addActionListener((ActionEvent e) -> {
            gradeComboActionPerformed(e);
        });
        
        DefaultComboBoxModel subjectsModel = new DefaultComboBoxModel(subjectData.toArray());
        
        subjectCombo = new JComboBox(subjectsModel);
        
        subjectCombo.addActionListener((ActionEvent e) -> {
            subjectComboActionPerformed(e);
        });
        
        tableReFormat();
        
        tableScrollPane.setViewportView(resultTable); 
        tableScrollPane.repaint();
        numRecords = resultTable.getRowCount();
    }
     
    private void buttonStatus(){    
        if(viewStatus == Status.NEW || viewStatus == Status.EDIT){
            headingPanelFormat();
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            headingLabel.setText(((viewStatus == Status.NEW)?"New Subject Teacher":"Edit Subject Teacher"));
            dataFieldStatus(true);
            
        }else{
            headingPanelFormat();
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            headingLabel.setText("View Subject Teacher");
            dataFieldStatus(false);
            if(currentRec == -1){
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        }
    }
    
    private void dataFieldStatus(boolean status){
        txtTYear.setEnabled(status);
        txtTerm.setEnabled(status);
        teacherCombo.setEnabled(status);
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
        recordLabel.setText(Integer.toString(currentRec + 1) + " of " + Integer.toString(numRecords));
    }
    
    private void loadData(){
        subjectTeachers = new ArrayList<>();
        
        try{          
            allSubjectTeachers = repository.getSubjectTeachersByYearTerm(currentYear, currentTerm);

            if(!allSubjectTeachers.isEmpty()){
                subjectTeachers = allSubjectTeachers;
                currentRec=0;
                viewStatus = Status.VIEW;
                updated = true;
                pending = false;
            }else{
                if(dialogMessage("No Term Subject Teacher Records Found! Do you want to create them?", "Warning", JOptionPane.YES_NO_OPTION)){
                    if(currentTerm != terms.get(0).getTerm()){
                        repository.insertNewTermSubjectTeachers(currentYear, currentTerm);
                        allSubjectTeachers = repository.getSubjectTeachersByYearTerm(currentYear, currentTerm);
                        updated=true;
                        pending=false;
                        viewStatus = Status.VIEW;
                    }else{    
                        SubjectSchedulesRepository scheduleRepository = new SubjectSchedulesRepository();
                        List<SubjectSchedules> schedules = new ArrayList<>();
                        schedules = scheduleRepository.getAllSchedules();
                        System.out.println("Schedules: " + schedules.size());
                        for(SubjectSchedules schedule : schedules){
                            SubjectTeacher subjectTeacher = new SubjectTeacher(schedule.getGrade(), 0, schedule.getSubjectid(),currentYear, currentTerm);
                            subjectTeacher.setTeacher(new Teacher("Teacher","Select"));
                            subjectTeacher.setSubject(subjectInfo.get(schedule.getSubjectid()));
                            allSubjectTeachers.add(subjectTeacher);
                        }
                        updated=false;
                        pending=false;
                        viewStatus = Status.EDIT;
                    }
                    if(!allSubjectTeachers.isEmpty()){
                        subjectTeachers = allSubjectTeachers;
                        currentRec=0;
                    }else{
                        dialogMessage("Insufficient data to proceed with this process!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        this.setVisible(false);
                    }
                }else{
                    dialogMessage("Insufficient data to proceed with this process!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    this.setVisible(false);
                }
                
            }
            colNames = new ArrayList<>();
            colMethods = new ArrayList<>();
            colSetMethods = new ArrayList<>();
            colNames = repository.getColumnNames();
            for(Field colName : colNames){
                try {
                    colMethods.add(SubjectTeacher.class.getDeclaredMethod("get" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1)));
                    colSetMethods.add(SubjectTeacher.class.getDeclaredMethod("set" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1),colName.getType()));
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(SubjectTeacherForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            loadListModel();
            loadTableModel();
            updateDisplay();
            System.out.println("Year: " + currentYear + "\tTerm: " + currentTerm);
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void updateDisplay(){
        resultTable.setRowSelectionInterval(currentRec, currentRec);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        gradeComboBox = new javax.swing.JComboBox<>();
        studentsScrollPane = new javax.swing.JScrollPane();
        subjectTeacherList = new javax.swing.JList<>();
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
        txtTYear = new javax.swing.JComboBox<>();
        txtTerm = new javax.swing.JComboBox<>();
        tableScrollPane = new javax.swing.JScrollPane();
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
        jLabel1.setText("Subject Teacher Data Form");

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearch.setPreferredSize(new java.awt.Dimension(64, 36));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        bodyPanel.setBackground(new java.awt.Color(51, 220, 246));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Term:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Year:");

        gradeComboBox.setBackground(new java.awt.Color(51, 153, 255));
        gradeComboBox.setToolTipText("Filter by Grade!");
        gradeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeComboBoxActionPerformed(evt);
            }
        });

        subjectTeacherList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        subjectTeacherList.setBackground(new java.awt.Color(51, 153, 255));
        subjectTeacherList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                subjectTeacherListValueChanged(evt);
            }
        });
        studentsScrollPane.setViewportView(subjectTeacherList);

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        headingLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        headingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        headingLabel.setText("View Subject Teacher");

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
                .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
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

        txtTYear.setPreferredSize(new java.awt.Dimension(72, 26));

        txtTerm.setPreferredSize(new java.awt.Dimension(72, 26));

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addComponent(gradeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(194, 194, 194)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addComponent(headingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(gradeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(headingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tableScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(studentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)))
                .addContainerGap())
        );

        newButton.setBackground(new java.awt.Color(51, 51, 255));
        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/subjectteach.png"))); // NOI18N
        newButton.setBorder(null);
        newButton.setBorderPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/subjectteachover.png"))); // NOI18N
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                            .addGroup(headerPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bodyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        viewStatus = Status.NEW;
        SubjectTeacher subjectTeacher = new SubjectTeacher("Select?", 0, 0, currentYear, currentTerm);
        subjectTeacher.setTeacher(new Teacher("Teacher Name", "Select"));
        subjectTeacher.setSubject(new Subject(0,"Select Subject"));
        
        dataModel.getModelData().add(subjectTeacher);
        dataModel.fireTableDataChanged();
        numRecords++;
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
    }//GEN-LAST:event_newButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        System.out.println("Records Updated: " + updated + "\tPending Changes: " + pending);
        if(!updated || pending){
           if(dialogMessage("Your changes have not been saved!! Are you sure you want to discard the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              cancelSettings(); 
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            cancelSettings();
        }   
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void cancelSettings(){
        viewStatus = Status.VIEW;
        buttonStatus();
        
        if(resultTable.getRowCount()>=1){
            if(resultTable.getSelectedRow() == currentRec && currentRec != -1){
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }else{
                currentRec = 0;
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }
            if(!subjectTeacherList.isEnabled())
                subjectTeacherList.setEnabled(true);
        }
        navigationButtons();
    }
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        System.out.println("Updated Status: " + updated + "\tPending Status: " + pending);
        try{
            if(!updated){
                repository.addBatchSubjectTeacher(subjectTeachers);
                updated = true;
            }else if(pending & !(pendingRecords.isEmpty())){
                if(pendingRecords.containsKey(0)){
                   pendingRecords.remove(0);
                }
                for(int subjectteacherid : pendingRecords.keySet()){
                    repository.updateSubjectTeacher(pendingRecords.get(subjectteacherid));
                }
                pending = false;
            }
            dialogMessage("All the records have been updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        viewStatus = Status.EDIT;
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
        if(resultTable.getRowCount()>=1)
            resultTable.setRowSelectionInterval(currentRec, currentRec);       
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
 
        if(resultTable.getSelectedRowCount() > 1){
            int [] rows = resultTable.getSelectedRows();
            for(int i = 0; i < rows.length; i++){
                SubjectTeacher subjectTeacher = dataModel.getModelData().get(rows[i]);
                if(dialogMessage("Are Sure you want to delete the Record: \nTeacherID: " + subjectTeacher.getTeacherid()
                        + "Subject Teacher: " + subjectTeacher.getTeacher().toString().trim() + " Grade: " + subjectTeacher.getGrade() +
                        " Year: " + subjectTeacher.getTyear() + " Term: " + subjectTeacher.getTerm(), "Warning", JOptionPane.YES_NO_OPTION)){
                    dataModel.getModelData().remove(rows[i]);
                    dataModel.fireTableRowsDeleted(rows[i], rows[i]);
                    if(updated){
                       repository.deleteSubjectTeacher(subjectTeacher);
                    }
                }
            }
        }else if(resultTable.getSelectedRowCount() == 1){
            int row = resultTable.getSelectedRow();
            SubjectTeacher subjectTeacher = dataModel.getModelData().get(row);
            if(dialogMessage("Are Sure you want to delete the Record: \nTeacherID: " + subjectTeacher.getTeacherid()
                    + "Subject Teacher: " + subjectTeacher.getTeacher().toString().trim() + " Grade: " + subjectTeacher.getGrade() +
                    " Year: " + subjectTeacher.getTyear() + " Term: " + subjectTeacher.getTerm(), "Warning", JOptionPane.YES_NO_OPTION)){
                dataModel.getModelData().remove(row);
                dataModel.fireTableRowsDeleted(row, row);
                if(updated){
                   repository.deleteSubjectTeacher(subjectTeacher);
                }
            }
        }else{
            dialogMessage("Select the record(s) you want to delete first!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        numRecords = resultTable.getRowCount();
    }//GEN-LAST:event_deleteButtonActionPerformed

private void moveRecordSelection(){
    updateDisplay();
    navigationButtons();
}
    
    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        --currentRec;
        moveRecordSelection();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        currentRec = 0;
        moveRecordSelection();
    }//GEN-LAST:event_firstButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        ++currentRec;
        moveRecordSelection();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        currentRec = resultTable.getRowCount() - 1;
        moveRecordSelection();
    }//GEN-LAST:event_lastButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(!updated || pending){
        if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              repository.closeSession();
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            repository.closeSession();
        }
    }//GEN-LAST:event_formWindowClosing

    private void subjectTeacherListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_subjectTeacherListValueChanged
        if(!subjectTeachers.isEmpty()){
            if(!subjectTeacherList.isSelectionEmpty()){
                currentRec = subjectTeacherList.getSelectedIndex();
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }else{
                currentRec = 0;
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }
        }
    }//GEN-LAST:event_subjectTeacherListValueChanged

    private void gradeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeComboBoxActionPerformed
        if(!updated || pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
              gradeComboBoxAction();
           }else{
               dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
           }
        }else{
            gradeComboBoxAction();
        } 
    }//GEN-LAST:event_gradeComboBoxActionPerformed
    
    private void gradeComboBoxAction(){
        String grade = gradeComboBox.getSelectedItem().toString().trim();
        
        if (!subjectTeacherList.isEnabled()) 
            subjectTeacherList.setEnabled(true);
        if(grade.equalsIgnoreCase("All")){
            subjectTeachers.addAll(allSubjectTeachers);
            loadListModel();
            loadTableModel();
        }else{   
            subjectTeachers.clear();
            for(SubjectTeacher subjectTeacher : allSubjectTeachers){
                if(subjectTeacher.getGrade().equalsIgnoreCase(grade)){
                    subjectTeachers.add(subjectTeacher);
                }
            }
        }
        if(!subjectTeachers.isEmpty()){
            numRecords = subjectTeachers.size();
            loadListModel();
            loadTableModel();
            currentRec = 0;
            updateDisplay();
        }else{
            dialogMessage("You don't have subjectTeachers in " + grade + ". Capture SubjectTeacher Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
            viewStatus = Status.NEW;
            subjectTeachers = new ArrayList<>(allSubjectTeachers);
            loadListModel();
            loadTableModel();
            newButtonActionPerformed(new java.awt.event.ActionEvent(this,1,"New"));
        }
    }
    
    private void teacherComboActionPerformed(ActionEvent evt) {
        if(teacherCombo.getSelectedItem() instanceof Teacher){
            Teacher teacher = new Teacher();
            teacher = (Teacher) teacherCombo.getSelectedItem();
            resultTable.getModel().setValueAt(teacher, resultTable.getSelectedRow(), resultTable.getSelectedColumn() + 1);
            if(updated){
                pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getSubjectteacherid(),
                    dataModel.getModelData().get(resultTable.getSelectedRow()));
                pending = true;
            }
            System.out.println("Selected Teacher: " + teacher);
            System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
            System.out.println("Updated Subject Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
        }
    }
    
    private void subjectComboActionPerformed(ActionEvent evt) {
        if(subjectCombo.getSelectedItem() instanceof Subject){
            Subject subject = new Subject();
            subject = (Subject) subjectCombo.getSelectedItem();
            resultTable.getModel().setValueAt(subject, resultTable.getSelectedRow(), resultTable.getSelectedColumn() + 1);
            if(updated){
                pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getSubjectteacherid(),
                    dataModel.getModelData().get(resultTable.getSelectedRow()));
                pending = true;
            }
            System.out.println("Selected Subject: " + subject);
            System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
            System.out.println("Updated Subject Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
        }
    }
    
    private void gradeComboActionPerformed(ActionEvent evt) {
        if(updated){
            pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getSubjectteacherid(),
                dataModel.getModelData().get(resultTable.getSelectedRow()));
            pending = true;
        }
    }
    
    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        if(!updated || pending){
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
            subjectTeachers = new ArrayList<>();
            for(SubjectTeacher subjectTeacher : allSubjectTeachers){
                if(subjectTeacher.getGrade().toLowerCase().startsWith(searchKey) 
                        || subjectTeacher.getTeacher().toString().toLowerCase().startsWith(searchKey)
                        || subjectTeacher.getSubject().getTitle().toLowerCase().startsWith(searchKey))
                    subjectTeachers.add(subjectTeacher);
            }
        }else{
            subjectTeachers = new ArrayList<>(allSubjectTeachers);
        }
        if(!subjectTeachers.isEmpty()){
            numRecords = subjectTeachers.size();
            loadListModel();
            loadTableModel();
            currentRec = 0;
            updateDisplay();
        }else{
            subjectTeachers = new ArrayList<>(allSubjectTeachers);
            numRecords = subjectTeachers.size();
            loadListModel();
            loadTableModel();
            currentRec = 0;
            updateDisplay();
        }
    }
    
    private void tableReFormat(){
        TableFormat.addComboBoxToCell(resultTable, colNames.get(3).getName(), subjectCombo);
        TableFormat.addComboBoxToCell(resultTable, colNames.get(1).getName(), teacherCombo);
        TableFormat.addComboBoxToCell(resultTable, colNames.get(0).getName(), gradeCombo);
        Map<String,Integer> columns = new HashMap<>();
        columns.put(colNames.get(1).getName(), 130);
        columns.put(colNames.get(3).getName(), 255);
        TableFormat.resizeColumnWidthStatics(resultTable, columns);
    }
        
    private void txtTYearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentYear = Integer.parseInt(txtTYear.getSelectedItem().toString());
        System.out.println("Year Changed....Loading Data!!!");
        subjectTeachers.clear();
        updated=pending=false;
        loadData();
    }  
    
    private void txtTermActionPerformed(java.awt.event.ActionEvent evt) {                                        
        TermSystem term = (TermSystem) txtTerm.getSelectedItem();
        currentTerm = term.getTerm();
        System.out.println("Term Changed....Loading Data!!!");
        subjectTeachers.clear();
        System.out.println("Subject Teachers: " + subjectTeachers + "\tSubject Teachers Empty?: " + subjectTeachers.isEmpty());
        updated=pending=false;
        loadData();
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
                    .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            java.util.logging.Logger.getLogger(SubjectTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SubjectTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SubjectTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SubjectTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            public void run() {
                new SubjectTeacherForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton firstButton;
    private javax.swing.JComboBox<String> gradeComboBox;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JPanel headingPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton lastButton;
    private javax.swing.JButton newButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane studentsScrollPane;
    private javax.swing.JList<String> subjectTeacherList;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JComboBox<String> txtTYear;
    private javax.swing.JComboBox<String> txtTerm;
    // End of variables declaration//GEN-END:variables

}

