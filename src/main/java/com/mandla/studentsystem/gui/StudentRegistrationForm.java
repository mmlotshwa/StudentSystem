/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem.gui;

import com.mandla.studentsystem.repositories.RegistrationRepository;
import com.mandla.studentsystem.repositories.TermSystemRepository;
import com.mandla.studentsystem.repositories.SubjectRepository;
import com.mandla.studentsystem.repositories.StudentRepository;
import com.mandla.studentsystem.repositories.GradeNameRepository;
import com.mandla.studentsystem.repositories.SubjectTeacherRepository;
import com.mandla.studentsystem.repositories.CommentListRepository;
import com.mandla.studentsystem.entities.Subject;
import com.mandla.studentsystem.entities.CommentList;
import com.mandla.studentsystem.entities.Student;
import com.mandla.studentsystem.entities.GradeName;
import com.mandla.studentsystem.entities.Registration;
import com.mandla.studentsystem.entities.SubjectTeacher;
import com.mandla.studentsystem.entities.TermSystem;
import com.mandla.studentsystem.utils.TableFormat;
import com.mandla.studentsystem.utils.SpinnerCellEditor;
import com.mandla.studentsystem.utils.EventCellDataChange;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.HibernateException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mlots
 */
public class StudentRegistrationForm extends javax.swing.JFrame {
    private List<Student> students;
    private List<Registration> registrations;
    private List<Student> allStudents;
    private List<Registration> currentStudentRegistrations;
    private Map<Integer,List<Registration>> studRegistrations;
    private final StudentRepository studRepository;
    private final RegistrationRepository repository;
    private int currentRec = - 1;
    private int numRecords = -1;
    private enum Status {VIEW, EDIT, NEW};
    private Status viewStatus;
    private List<GradeName> grades;
    private ArrayList<Field> colNames;
    private ArrayList<Method> colMethods;
    private ArrayList<Method> colSetMethods;
    private DataTableModel<Registration> dataModel;
    private TableRowSorter<TableModel> sorter;
    private JTable resultTable;
    private List<SubjectTeacher> subjectTeacherData;
    private List<Subject> subjectData;
    private List<CommentList> commentsData;
    private int currentYear;
    private int currentTerm;
    private List<TermSystem> terms;
    private Map<String,Integer> subjectTeachers;
    private Map<Integer,Subject> subjects;
    private JComboBox subjectCombo; //For allowing the user to select the teacher on the table
    private JComboBox commentCombo; //For allowing the user to change the subject on the table
    private boolean updated; //Flag for all records status; true-if records read from database, false-if records created for new term
    private boolean pending; //Flag to show records set for update
    private final Map<Integer,Registration> pendingRecords; //To store updated or added records while updated is true and pending false
    
    
    /**
     * Creates new form RegistrationForm
     */
    public StudentRegistrationForm() {
        initComponents();
        pending = false;
        pendingRecords = new HashMap<>();
        repository = new RegistrationRepository();
        studRepository = new StudentRepository();
        subjectTeacherData = new ArrayList<>();
        subjectData = new ArrayList<>();
        allStudents = new ArrayList<>();
        
        loadComboBoxes();
        loadData(); 
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
        
        CommentListRepository commentRepository = new CommentListRepository();
        commentsData = commentRepository.getAllTeacherComments();
        
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
        
        //Load Subjects to a search map, to find student selected title
        SubjectRepository subjectRepository = new SubjectRepository();
        subjectData = subjectRepository.getAllSubjects();

        subjectData.sort(Comparator.comparing(Subject::getDepartment).thenComparing(Subject::getTitle));
        subjects = new HashMap<>();
        for(Subject subject : subjectData){
            subjects.put(subject.getSubjectid(),subject);
        }
    }
    
    private void loadListModel() {
        DefaultListModel<String> studentListModel = new DefaultListModel<>();
        for(Student student : students){
            studentListModel.addElement(student.getSurname() + ", " + student.getFirstname());
        }
        studentList.setModel(studentListModel);
    }
    
    private void activateListeners(){
        txtTerm.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTermActionPerformed(evt);
        });
        
        txtTYear.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTYearActionPerformed(evt);
        });
    }
    
    private void createSubjectTeacherSearchMap(String grade){
        try{
            SubjectTeacherRepository teachRepository = new SubjectTeacherRepository();
            subjectTeacherData = teachRepository.getSubjectTeachersByGradeYearTerm(grade, currentYear, currentTerm);

            subjectTeacherData.sort(
                    Comparator.comparing(SubjectTeacher::getTeacherid)
                    .thenComparing(SubjectTeacher::getSubjectid)
            );
            subjectTeachers = new HashMap<>();
            for(SubjectTeacher subjectTeacher : subjectTeacherData){
                subjectTeachers.put(subjectTeacher.toString().trim(),subjectTeacher.getSubjectid());
            }
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTableModel(){
        dataModel = new DataTableModel<>(currentStudentRegistrations,colNames,colMethods,colSetMethods,subjectTeachers);
        resultTable = new JTable(dataModel);
        sorter = new TableRowSorter<>(dataModel);
        resultTable.setRowSorter(sorter);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DefaultComboBoxModel subjectModel = new DefaultComboBoxModel(subjectTeacherData.toArray());
        
        subjectCombo = new JComboBox(subjectModel);
        
        subjectCombo.addActionListener(evt -> {
            this.subjectComboActionPerformed(evt);
        });
        
        String [] comments = new String[commentsData.size()];
        for(int i = 0; i<commentsData.size(); i++){
            comments[i] = commentsData.get(i).getComment();
        }
            
        DefaultComboBoxModel commentsModel = new DefaultComboBoxModel(comments);
        
        commentCombo = new JComboBox(commentsModel);
        
        commentCombo.addActionListener((ActionEvent e) -> {
            commentComboActionPerformed(e);
        });
        
        tableReFormat();
        
        tableScrollPane.setViewportView(resultTable); 
        tableScrollPane.repaint();
    }
     
    private void buttonStatus(){    
        if(viewStatus == Status.NEW || viewStatus == Status.EDIT){
            headingPanelFormat();
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            headingLabel.setText(((viewStatus == Status.NEW)?"New Student Registration":"Edit Student Registration"));
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
        subjectCombo.setEnabled(status);
        commentCombo.setEnabled(status);
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
        students = new ArrayList<>();
        registrations = new ArrayList<>();
        currentStudentRegistrations = new ArrayList<>();
        studRegistrations = new HashMap<>();
        
        try{
            allStudents = studRepository.getAllActiveStudents();

            if(!allStudents.isEmpty()){
               students = allStudents;
               loadListModel();
               numRecords = students.size();
               currentRec=0;
               viewStatus = Status.VIEW;
               registrations = repository.getRegistrationsByYearTerm(currentYear, currentTerm);
               pending = false;
               if(registrations.isEmpty()){
                   dialogMessage("This term registrations have not been created! You cannot edit student registrations!!", "Message", JOptionPane.INFORMATION_MESSAGE);
                   this.dispose();
                   this.setVisible(false);
                }
                colNames = new ArrayList<>();
                colMethods = new ArrayList<>();
                colSetMethods = new ArrayList<>();
                colNames = repository.getColumnNames();
                for(Field colName : colNames){
                    try {
                        colMethods.add(Registration.class.getDeclaredMethod("get" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1)));
                        colSetMethods.add(Registration.class.getDeclaredMethod("set" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1),colName.getType()));
                    } catch (NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(StudentRegistrationForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                registrations.sort(Comparator.comparing(Registration::getStudentid));
                createRegistrationsMap();
                updateDisplay(students.get(currentRec));
            }else{
                dialogMessage("You currently have no students registered! Capture Student Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                this.setVisible(false);
            }
            buttonStatus();
            navigationButtons();
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void createRegistrationsMap(){
        int currentID, previousID;
        List<Registration> tempRegs = new ArrayList<>();
        
        previousID=registrations.get(0).getStudentid();
        
        for(int i=0; i<registrations.size(); i++){
            currentID = registrations.get(i).getStudentid();
            if(currentID == previousID){
                tempRegs.add(registrations.get(i));
                if(i == registrations.size() - 1)
                    currentID = 0;
            }
            
            if(currentID != previousID){
                studRegistrations.put(previousID, tempRegs);
                tempRegs = new ArrayList<>();
            }
            previousID = currentID;
        }
    }
    
    private void updateDisplay(Student student){
        txtStudentID.setText(Integer.toString(student.getStudentid()));
        txtFirstName.setText(student.getFirstname());
        txtSurname.setText(student.getSurname());
        txtGender.setText(student.getGender());
        txtGrade.setText(student.getGrade());
        String grade = student.getGrade();
        int StudentID = student.getStudentid();
        createSubjectTeacherSearchMap(grade);
        
        currentStudentRegistrations = new ArrayList<>();
        if(studRegistrations.containsKey(StudentID)){
            currentStudentRegistrations.addAll(studRegistrations.get(StudentID)); 
        }else{
            dialogMessage("Student: " + StudentID + " Not Registerd! Add subjects for the student!!", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        
        loadTableModel();
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
        studentList = new javax.swing.JList<>();
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
        txtFirstName = new javax.swing.JTextField();
        txtSurname = new javax.swing.JTextField();
        txtStudentID = new javax.swing.JTextField();
        txtGender = new javax.swing.JTextField();
        txtGrade = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
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
        jLabel1.setText("Student Registrations Data Form");
        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearch.setPreferredSize(new java.awt.Dimension(64, 36));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        bodyPanel.setBackground(new java.awt.Color(51, 220, 246));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Term:");
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Year:");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        gradeComboBox.setBackground(new java.awt.Color(51, 153, 255));
        gradeComboBox.setToolTipText("Filter by Grade!");
        gradeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeComboBoxActionPerformed(evt);
            }
        });

        studentList.setBackground(new java.awt.Color(51, 153, 255));
        studentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        studentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                studentListValueChanged(evt);
            }
        });
        studentsScrollPane.setViewportView(studentList);

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        headingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        headingLabel.setText("View Student Registration");
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

        txtFirstName.setEnabled(false);

        txtSurname.setEnabled(false);

        txtStudentID.setEnabled(false);

        txtGender.setEnabled(false);

        txtGrade.setEnabled(false);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("StudentID:");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Names:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Gender:");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Surname:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Grade:");

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
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                                    .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap())
                                .addComponent(headingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(196, 196, 196)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                                        .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(196, 196, 196)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(21, 21, 21)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(361, 361, 361)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(194, 194, 194)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))))
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(gradeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(headingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel6)
                                .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/studentregister.png"))); // NOI18N
        newButton.setBackground(new java.awt.Color(51, 51, 255));
        newButton.setBorder(null);
        newButton.setBorderPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/studentregisterhover.png"))); // NOI18N
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(headerPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        Registration registration = new Registration(txtGrade.getText().trim(),0,
                students.get(currentRec).getStudentid(),0,0,"",currentYear,currentTerm);
        registration.setSubject(new Subject(0,"Select Subject"));
        registration.setStudent(students.get(currentRec));
        
        dataModel.getModelData().add(registration);
        dataModel.fireTableDataChanged();
        
        buttonStatus();
        navigationButtons();
        navButtonStatus(false);
    }//GEN-LAST:event_newButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if(pending){
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
        updateDisplay(students.get(currentRec));
    }
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try{
            if(pending & !(pendingRecords.isEmpty())){
                if(pendingRecords.containsKey(0)){
                   pendingRecords.remove(0);
                }
                for(int registrationid : pendingRecords.keySet()){
                    repository.updateRegistration(pendingRecords.get(registrationid));
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
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if(resultTable.getSelectedRowCount() > 1){
            int [] rows = resultTable.getSelectedRows();
            for(int i = 0; i < rows.length; i++){
                Registration registration = dataModel.getModelData().get(rows[i]);
                if(dialogMessage("Are Sure you want to delete the Record: \nSubjectID: " + registration.getSubjectid()
                        + "Subject Title: " + registration.getSubject().toString().trim() + " Grade: " + registration.getGrade() +
                        " Year: " + registration.getTyear() + " Term: " + registration.getTerm(), "Warning", JOptionPane.YES_NO_OPTION)){
                    dataModel.getModelData().remove(rows[i]);
                    dataModel.fireTableRowsDeleted(rows[i], rows[i]);
                    if(!pending){
                       repository.deleteRegistration(registration);
                    }else{
                        if(pendingRecords.containsKey(registration.getRegistrationid())){
                            pendingRecords.remove(registration.getRegistrationid());
                        }
                    }
                }
            }
        }else if(resultTable.getSelectedRowCount() == 1){
            int row = resultTable.getSelectedRow();
            Registration registration = dataModel.getModelData().get(row);
            if(dialogMessage("Are Sure you want to delete the Record: \nSubjectID: " + registration.getSubjectid()
                    + "Subject Title: " + registration.getSubject().toString().trim() + " Grade: " + registration.getGrade() +
                    " Year: " + registration.getTyear() + " Term: " + registration.getTerm(), "Warning", JOptionPane.YES_NO_OPTION)){
                dataModel.getModelData().remove(row);
                dataModel.fireTableRowsDeleted(row, row);
                if(!pending){
                   repository.deleteRegistration(registration);
                }else{
                    if(pendingRecords.containsKey(registration.getRegistrationid())){
                        pendingRecords.remove(registration.getRegistrationid());
                    }
                }
            }
        }else{
            dialogMessage("Select the record(s) you want to delete first!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
            
    }//GEN-LAST:event_deleteButtonActionPerformed
    
    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        if(pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                updateDisplay(students.get(--currentRec));
            }else{
                dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            updateDisplay(students.get(--currentRec));
        }
        
    }//GEN-LAST:event_previousButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        if(pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                currentRec = 0;
                updateDisplay(students.get(currentRec));
            }else{
                dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            currentRec = 0;
            updateDisplay(students.get(currentRec));
        }
        
    }//GEN-LAST:event_firstButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if(pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                updateDisplay(students.get(++currentRec));
            }else{
                dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            updateDisplay(students.get(++currentRec));
        }
        
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        if(pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                currentRec = students.size() - 1;
                updateDisplay(students.get(currentRec));
            }else{
                dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            currentRec = students.size() - 1;
            updateDisplay(students.get(currentRec));
        }
    }//GEN-LAST:event_lastButtonActionPerformed

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

    private void studentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_studentListValueChanged
        if(pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                if(!students.isEmpty()){
                    if(!studentList.isSelectionEmpty()){
                        currentRec = studentList.getSelectedIndex();
                    }else{
                        currentRec = 0;
                    }
                    updateDisplay(students.get(currentRec));
                }
            }else{
                dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            if(!students.isEmpty()){
                if(!studentList.isSelectionEmpty()){
                    currentRec = studentList.getSelectedIndex();
                }else{
                    currentRec = 0;
                }
                updateDisplay(students.get(currentRec));
            }
        }
    }//GEN-LAST:event_studentListValueChanged

    private void gradeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeComboBoxActionPerformed
        if(pending){
            if(dialogMessage("Do you want to discard all the changes??", "Warning", JOptionPane.YES_NO_OPTION)){
                gradeComboAction();
            }else{
                dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            gradeComboAction();
        }
    }//GEN-LAST:event_gradeComboBoxActionPerformed
    
    private void gradeComboAction(){
        String grade = gradeComboBox.getSelectedItem().toString().trim();
        if(grade.equalsIgnoreCase("All")){
            students = new ArrayList<>(allStudents);
            loadListModel();
            numRecords = students.size();
            currentRec=0;
            updateDisplay(students.get(currentRec));
        }else{   
            try{
            students = new ArrayList<>();
            for (Student student : allStudents){
                if(student.getGrade().equalsIgnoreCase(grade)){
                    students.add(student);
                }
            }
            if(!students.isEmpty()){
                numRecords = students.size();
                currentRec = 0;
                loadListModel();
                updateDisplay(students.get(currentRec));
            }else{
                dialogMessage("You don't have students in " + grade + ". Capture Student Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
                students = new ArrayList<>(allStudents);
                loadListModel();
                numRecords = students.size();
                currentRec=0;
                updateDisplay(students.get(currentRec));
            }
            }catch(HibernateException he){
                dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
                students = new ArrayList<>(allStudents);
                loadListModel();
                numRecords = students.size();
                currentRec=0;
                updateDisplay(students.get(currentRec));
            }
        }
    }
    
    private void subjectComboActionPerformed(ActionEvent evt) {
        if(subjectCombo.getSelectedItem() instanceof SubjectTeacher){
            SubjectTeacher subjectTeacher = new SubjectTeacher();
            subjectTeacher = (SubjectTeacher) subjectCombo.getSelectedItem();
            Subject subject = subjects.get(subjectTeacher.getSubjectid());
            resultTable.getModel().setValueAt(subject, resultTable.getSelectedRow(), resultTable.getSelectedColumn() + 1);
            pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getRegistrationid(),
                dataModel.getModelData().get(resultTable.getSelectedRow()));
            pending = true;
            
            System.out.println("Selected Teacher: " + subjectTeacher);
            System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
            System.out.println("Updated Subject Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
        }
    }
    
    private void commentComboActionPerformed(ActionEvent evt) { 
        pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getRegistrationid(),
            dataModel.getModelData().get(resultTable.getSelectedRow()));
        pending = true;
        
        System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
        System.out.println("Updated Subject Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
        
    }
    
    private void txtMarkStateChanged() {
        Registration current = dataModel.getModelData().get(resultTable.getSelectedRow());
        int total = 0, counter=0;
        for(Registration registration : registrations){
            if(registration.getGrade().equalsIgnoreCase(current.getGrade())
                    && registration.getSubjectid() == current.getSubjectid()){
                total+=registration.getExammark();
                counter++;
            }
        }
        int average = 0;
        if(counter>=1){
            average = total / counter;
            //resultTable.getModel().setValueAt(average, resultTable.getSelectedRow(), resultTable.getSelectedColumn() - 1);
        }
        pendingRecords.put(current.getRegistrationid(),current);
        pending = true;
        System.out.println("Selected Row: " + resultTable.getSelectedRow() 
                + "\nChanged Record: " + dataModel.getModelData().get(resultTable.getSelectedRow())
                + "\nExam Mark: " + dataModel.getModelData().get(resultTable.getSelectedRow()).getExammark()
                + "\nAverage Mark: " + average + "\nTotal Marks: " + total + "\nNumber of Records: " + counter);
    }
    
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
            students = new ArrayList<>();
            for(Student student : students){
                if(student.getGrade().toLowerCase().startsWith(searchKey) 
                        || student.getSurname().startsWith(searchKey)
                        || student.getFirstname().startsWith(searchKey))
                    students.add(student);
            }
            if(!students.isEmpty()){
                numRecords = students.size();
                loadListModel();
                currentRec = 0;
                updateDisplay(students.get(currentRec));
            }
        }else{
            students = new ArrayList<>(allStudents);
            numRecords = students.size();
            loadListModel();
            currentRec = 0;
            updateDisplay(students.get(currentRec));
        }
    }
    private void tableReFormat(){
        TableFormat.addComboBoxToCell(resultTable, colNames.get(1).getName(), subjectCombo);
        TableFormat.addComboBoxToCell(resultTable, colNames.get(7).getName(), commentCombo);
        resultTable.getColumnModel().getColumn(5).setCellEditor(new SpinnerCellEditor(new EventCellDataChange(){
            @Override
            public void dataChanged() {
                txtMarkStateChanged();
            }
        }));
        Map<String,Integer> columns = new HashMap<>();
        columns.put(colNames.get(1).getName(), 280);
        columns.put(colNames.get(7).getName(), 255);
        TableFormat.resizeColumnWidthStatics(resultTable, columns);
        resultTable.setRowHeight(25);
    }
        
    private void txtTYearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentYear = Integer.parseInt(txtTYear.getSelectedItem().toString());
        System.out.println("Year Changed....Loading Data!!!");
        pending=false;
        loadData();
    }  
    
    private void txtTermActionPerformed(java.awt.event.ActionEvent evt) {                                        
        TermSystem term = (TermSystem) txtTerm.getSelectedItem();
        currentTerm = term.getTerm();
        System.out.println("Term Changed....Loading Data!!!");
        
        System.out.println("Subject Teachers: " + registrations + "\tSubject Teachers Empty?: " + registrations.isEmpty());
        updated=false;
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
            java.util.logging.Logger.getLogger(StudentRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new StudentRegistrationForm().setVisible(true);
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
    private javax.swing.JList<String> studentList;
    private javax.swing.JScrollPane studentsScrollPane;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtGender;
    private javax.swing.JTextField txtGrade;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStudentID;
    private javax.swing.JTextField txtSurname;
    private javax.swing.JComboBox<String> txtTYear;
    private javax.swing.JComboBox<String> txtTerm;
    // End of variables declaration//GEN-END:variables

}

