/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem.gui;

import com.mandla.studentsystem.repositories.TermSystemRepository;
import com.mandla.studentsystem.repositories.TeacherRepository;
import com.mandla.studentsystem.repositories.GradeNameRepository;
import com.mandla.studentsystem.repositories.GradeTeacherRepository;
import com.mandla.studentsystem.entities.GradeTeacher;
import com.mandla.studentsystem.entities.GradeName;
import com.mandla.studentsystem.entities.Teacher;
import com.mandla.studentsystem.entities.TermSystem;
import com.mandla.studentsystem.utils.TableFormat;
import com.mandla.studentsystem.utils.DataTableModel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.time.DayOfWeek;
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
import javax.swing.JButton;
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
public class GradeTeacherForm extends javax.swing.JFrame {
    private List<GradeTeacher> gradeTeachers;
    private List<GradeTeacher> allGradeTeachers;
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
    private final Map<Integer,GradeTeacher> pendingRecords; //To store updated or added records while updated is true and pending false
    
    
    /**
     * Creates new form GradeTeacherForm
     */
    public GradeTeacherForm() {
        initComponents();
        updated = pending = false;
        pendingRecords = new HashMap<>();
        repository = new GradeTeacherRepository();
        allGradeTeachers = new ArrayList<>();
        createTeacherSearchMap();
        loadComboBoxes();
        createAndLoadDates();
        loadData(); 
        buttonStatus();
        navigationButtons();
        activateListeners();
    }
    
    private void createAndLoadDates(){
        DatePickerSettings settings1 = new DatePickerSettings();
        DatePickerSettings settings2 = new DatePickerSettings();
        DatePickerSettings settings3 = new DatePickerSettings();
        DatePickerSettings settings4 = new DatePickerSettings();
        termBegins.setSettings(settings1);
        termEnds.setSettings(settings2);
        nextTermBegins.setSettings(settings3);
        nextTermEnds.setSettings(settings4);
        
        settings1.setHighlightPolicy(new MyHighlightPolicy());
        settings2.setHighlightPolicy(new MyHighlightPolicy());
        settings3.setHighlightPolicy(new MyHighlightPolicy());
        settings4.setHighlightPolicy(new MyHighlightPolicy());
        
        setDatePickerButtonIcon(termBegins.getComponentToggleCalendarButton());
        setDatePickerButtonIcon(termEnds.getComponentToggleCalendarButton());
        setDatePickerButtonIcon(nextTermBegins.getComponentToggleCalendarButton());
        setDatePickerButtonIcon(nextTermEnds.getComponentToggleCalendarButton());
    }
    
    private void setDatePickerButtonIcon(JButton button){
        button.setText("");
        button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/date.png"))); 
        button.setBorder(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/datehover.png")));
    }
    
    private void loadComboBoxes(){
        try {
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
            for (int i = currentYear - 10; i <= currentYear + 10; i++) {
                yearModel.addElement((Object) i);
            }
            txtTYear.setModel(yearModel);
            
            txtTYear.setSelectedItem((Object) currentYear);
            TermSystem thisTerm = terms.get(0);
            
            for (int i = terms.size() - 1; i >= 0; i--) {
                if (currentDate.getMonthValue() >= terms.get(i).getStartMonth()) {
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
    
    private void loadListModel() {
        if (!gradeTeachers.isEmpty()) {
            DefaultListModel<String> gradeTeacherListModel = new DefaultListModel<>();
            for (GradeTeacher gradeTeacher : gradeTeachers) {
                gradeTeacherListModel.addElement(gradeTeacher.toString());
            }
            gradeTeacherList.setModel(gradeTeacherListModel);
        } else {
            dialogMessage("Load ListModel: Failure at Start-Up! \nCapture this Term's Grade Teachers first!", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    private void activateListeners(){
        termBegins.addDateChangeListener((DateChangeEvent event) -> {
           termBeginsDateChanged(event);
        });

        termEnds.addDateChangeListener((DateChangeEvent event) -> {
            termEndsDateChanged(event);
        });

        nextTermBegins.addDateChangeListener((DateChangeEvent event) -> {
            nextTermBeginsDateChanged(event);
        });

        nextTermEnds.addDateChangeListener((DateChangeEvent event) -> {
            nextTermEndsDateChanged(event);
        });
        
        txtTerm.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTermActionPerformed(evt);
        });
        
        txtTYear.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTYearActionPerformed(evt);
        });
    }
    
    private void createTeacherSearchMap(){
        try{
            teacherData = new ArrayList<>();
            TeacherRepository teachRepository = new TeacherRepository();
            teacherData = teachRepository.getAllTeachers();

            teacherData.sort(Comparator.comparing(Teacher::getSurname).thenComparing(Teacher::getFirstName));
            teachers = new HashMap<>();
            for(Teacher teacher : teacherData){
                teachers.put(teacher.getSurname() + ", " + teacher.getFirstName(), teacher.getTeacherid());
            }
        }catch(HibernateException he){
            dialogMessage("Search Maps: Failure at startup! This operation cannot proceed!\n" + he.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTableModel(){
        dataModel = new DataTableModel<>(gradeTeachers,colNames,colMethods,colSetMethods,teachers);
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
            headingLabel.setText(((viewStatus == Status.NEW)?"New Grade Teacher":"Edit Grade Teacher"));
            dataFieldStatus(true);
            
        }else{
            headingPanelFormat();
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            headingLabel.setText("View Grade Teacher");
            dataFieldStatus(false);
            if(currentRec == -1){
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        }
    }
    
    private void dataFieldStatus(boolean status){
        txtTYear.setEnabled(status);
        termBegins.setEnabled(status);
        termEnds.setEnabled(status);
        txtTerm.setEnabled(status);
        nextTermBegins.setEnabled(status);
        nextTermEnds.setEnabled(status);
        txtNumDays.setEnabled(status);
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
        try{
            gradeTeachers = new ArrayList<>();
            
            allGradeTeachers = repository.getGradeTeachersByYearTerm(currentYear, currentTerm);

            if(!allGradeTeachers.isEmpty()){
               gradeTeachers = allGradeTeachers;
               currentRec=0;
               updateDisplay(gradeTeachers.get(currentRec));
               viewStatus = Status.VIEW;
               updated = true;
               pending = false;
            }else{
                if(dialogMessage("No Term Subject Teacher Records Found! Do you want to create them?", "Warning", JOptionPane.YES_NO_OPTION)){
                    viewStatus = Status.NEW;
                    if(currentTerm != terms.get(0).getTerm()){
                        
                        repository.insertNewTermGradeTeachers(currentYear, currentTerm);
                        allGradeTeachers = repository.getGradeTeachersByYearTerm(currentYear, currentTerm);
                        updated=true;
                        pending=false;
                    }else{
                        for(GradeName grade : grades){
                            GradeTeacher teacher = new GradeTeacher(0, currentYear, currentTerm, 
                                grade.getGrade(), null, null, 60, null, null);
                            teacher.setTeacher(new Teacher("Teacher","Select"));
                            allGradeTeachers.add(teacher);
                        }
                        updated=false;
                        pending=false;
                    }
                    if(!allGradeTeachers.isEmpty()){
                        gradeTeachers = allGradeTeachers;
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
                updateDisplay(gradeTeachers.get(currentRec));
            }
            colNames = new ArrayList<>();
            colMethods = new ArrayList<>();
            colSetMethods = new ArrayList<>();
            colNames = repository.getColumnNames();
            for(Field colName : colNames){
                try {
                    colMethods.add(GradeTeacher.class.getDeclaredMethod("get" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1)));
                    colSetMethods.add(GradeTeacher.class.getDeclaredMethod("set" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1),colName.getType()));
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(GradeTeacherForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            loadListModel();
            loadTableModel();
        }catch(Exception he){
            dialogMessage("Loading Data: System failed to load data!!\n This operation cannot proceed!!\n"
                    + he.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
        
    }
    
    private void updateDisplay(GradeTeacher gradeTeacher){
        termBegins.setDate(gradeTeacher.getTermbegins()==null?null:gradeTeacher.getTermbegins().toLocalDate());
        termEnds.setDate(gradeTeacher.getTermends()==null?null:gradeTeacher.getTermends().toLocalDate());
        nextTermBegins.setDate(gradeTeacher.getNexttermbegins()==null?null:gradeTeacher.getNexttermbegins().toLocalDate());
        nextTermEnds.setDate(gradeTeacher.getNexttermends()==null?null:gradeTeacher.getNexttermends().toLocalDate());
        if(gradeTeacher.getNumofdays() != 0) 
            txtNumDays.setValue(gradeTeacher.getNumofdays());
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
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        gradeComboBox = new javax.swing.JComboBox<>();
        studentsScrollPane = new javax.swing.JScrollPane();
        gradeTeacherList = new javax.swing.JList<>();
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
        termBegins = new com.github.lgooddatepicker.components.DatePicker();
        termEnds = new com.github.lgooddatepicker.components.DatePicker();
        nextTermBegins = new com.github.lgooddatepicker.components.DatePicker();
        nextTermEnds = new com.github.lgooddatepicker.components.DatePicker();
        txtNumDays = new javax.swing.JSpinner();
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
        jLabel1.setText(" Grade Teacher Data Form");
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

        jLabel7.setText("Term End Date:");
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Term:");
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setText("Next Term Ends:");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setText("Next Term Begin:");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Year:");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setText("Term Start Date:");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        gradeComboBox.setBackground(new java.awt.Color(51, 153, 255));
        gradeComboBox.setToolTipText("Filter by Grade!");
        gradeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeComboBoxActionPerformed(evt);
            }
        });

        gradeTeacherList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        gradeTeacherList.setBackground(new java.awt.Color(51, 153, 255));
        gradeTeacherList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                gradeTeacherListValueChanged(evt);
            }
        });
        studentsScrollPane.setViewportView(gradeTeacherList);

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        headingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        headingLabel.setText("View GradeTeacher");
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

        termBegins.setBackground(new java.awt.Color(51, 220, 246));

        termEnds.setBackground(new java.awt.Color(51, 220, 246));

        nextTermBegins.setBackground(new java.awt.Color(51, 220, 246));

        nextTermEnds.setBackground(new java.awt.Color(51, 220, 246));

        txtNumDays.setModel(new javax.swing.SpinnerNumberModel(60, null, null, 1));
        txtNumDays.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtNumDaysStateChanged(evt);
            }
        });

        jLabel9.setText("Number of Days:");
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gradeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bodyPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(headingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(termBegins, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                                    .addComponent(nextTermBegins, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(bodyPanelLayout.createSequentialGroup()
                                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(29, 29, 29))
                                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(nextTermEnds, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addGroup(bodyPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(termEnds, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(74, 74, 74))
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 20, Short.MAX_VALUE))))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(376, 376, 376))))
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
                        .addGap(7, 7, 7)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(termBegins, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(termEnds, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(nextTermEnds, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nextTermBegins, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumDays, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(studentsScrollPane)))
                .addContainerGap())
        );

        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/gradeteach.png"))); // NOI18N
        newButton.setBackground(new java.awt.Color(51, 51, 255));
        newButton.setBorder(null);
        newButton.setBorderPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mandla/studentsystem/buttons/gradeteachover.png"))); // NOI18N
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
                        .addComponent(bodyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        viewStatus = Status.NEW;
        GradeTeacher gradeTeacher = new GradeTeacher(0, Integer.parseInt(txtTYear.getSelectedItem().toString()), 
                Integer.parseInt(txtTerm.getSelectedItem().toString()), "select?", termBegins.getText().trim().isEmpty()?null:Date.valueOf(termBegins.getDate()), 
                termEnds.getText().isEmpty()?null:Date.valueOf(termEnds.getDate()), Integer.parseInt(txtNumDays.getValue().toString()), 
                nextTermBegins.getText().isEmpty()?null:Date.valueOf(nextTermBegins.getDate()),
                nextTermEnds.getText().isEmpty()?null:Date.valueOf(nextTermEnds.getDate()));
        gradeTeacher.setTeacher(new Teacher("Teacher Name", "Select"));
        
        dataModel.getModelData().add(gradeTeacher);
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
            if(!gradeTeacherList.isEnabled())
                gradeTeacherList.setEnabled(true);
        }
        navigationButtons();
    }
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        System.out.println("Updated Status: " + updated + "\tPending Status: " + pending);
        try{
            if(!updated){
                for(GradeTeacher gradeTeacher : gradeTeachers){
                    repository.addGradeTeacher(gradeTeacher);
                }
                updated = true;
            }else if(pending & !(pendingRecords.isEmpty())){
                if(pendingRecords.containsKey(0)){
                   pendingRecords.remove(0);
                }
                for(int gradeteacherid : pendingRecords.keySet()){
                    repository.updateGradeTeacher(pendingRecords.get(gradeteacherid));
                }
                pending = false;
            }
            dialogMessage("All the records have been updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }catch(HibernateException he){
            dialogMessage("Save Grade Teacher: Failed to save the record(s)!\n" + he, "Error", JOptionPane.ERROR_MESSAGE);
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
 
        try {
            if (resultTable.getSelectedRowCount() > 1) {
                int[] rows = resultTable.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    GradeTeacher gradeTeacher = dataModel.getModelData().get(rows[i]);
                    if (dialogMessage("Are Sure you want to delete the Record: \nTeacherID: " + gradeTeacher.getTeacherid()
                            + "Grade Teacher: " + gradeTeacher.getTeacher().toString().trim() + " Grade: " + gradeTeacher.getGrade()
                            + " Year: " + gradeTeacher.getTyear() + " Term: " + gradeTeacher.getTerm(), "Warning", JOptionPane.YES_NO_OPTION)) {
                        dataModel.getModelData().remove(rows[i]);
                        dataModel.fireTableRowsDeleted(rows[i], rows[i]);
                        if (updated) {
                            repository.deleteGradeTeacher(gradeTeacher);
                        }
                    }
                }
            } else if (resultTable.getSelectedRowCount() == 1) {
                int row = resultTable.getSelectedRow();
                GradeTeacher gradeTeacher = dataModel.getModelData().get(row);
                if (dialogMessage("Are Sure you want to delete the Record: \nTeacherID: " + gradeTeacher.getTeacherid()
                        + "Grade Teacher: " + gradeTeacher.getTeacher().toString().trim() + " Grade: " + gradeTeacher.getGrade()
                        + " Year: " + gradeTeacher.getTyear() + " Term: " + gradeTeacher.getTerm(), "Warning", JOptionPane.YES_NO_OPTION)) {
                    dataModel.getModelData().remove(row);
                    dataModel.fireTableRowsDeleted(row, row);
                    if (updated) {
                        repository.deleteGradeTeacher(gradeTeacher);
                    }
                }
            } else {
                dialogMessage("Select the record(s) you want to delete first!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HibernateException he) {
            dialogMessage("Delete Grade Teacher: Failed to delete the record(s)!\n" + he.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
            
    }//GEN-LAST:event_deleteButtonActionPerformed

private void moveRecordSelection(int moveTo){
    resultTable.setRowSelectionInterval(moveTo, moveTo);
    navigationButtons();
}
    
    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        moveRecordSelection(--currentRec);
    }//GEN-LAST:event_previousButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        moveRecordSelection(currentRec = 0);
    }//GEN-LAST:event_firstButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        moveRecordSelection(++currentRec);
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        moveRecordSelection(currentRec = resultTable.getRowCount() - 1);
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

    private void gradeTeacherListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_gradeTeacherListValueChanged
        if(!gradeTeachers.isEmpty()){
            if(!gradeTeacherList.isSelectionEmpty()){
                currentRec = gradeTeacherList.getSelectedIndex();
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }else{
                currentRec = 0;
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }
        }
    }//GEN-LAST:event_gradeTeacherListValueChanged

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
        
        if(grade.equalsIgnoreCase("All")){
            gradeTeachers = new ArrayList<>(allGradeTeachers);
        }else{
            gradeTeachers = new ArrayList<>();
            for(GradeTeacher gradeTeacher : allGradeTeachers){
                if(gradeTeacher.getGrade().trim().equalsIgnoreCase(grade))
                    gradeTeachers.add(gradeTeacher);
            }
            if(gradeTeachers.isEmpty()){
                dialogMessage("You don't have gradeTeachers in " + grade + ". Capture GradeTeacher Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
                viewStatus = Status.NEW;
                gradeTeachers = new ArrayList<>(allGradeTeachers);
            }
        }
        loadListModel();
        loadTableModel();
        currentRec = 0;
        updateDisplay(gradeTeachers.get(currentRec));
    }
    
    private void teacherComboActionPerformed(ActionEvent evt) {
        if(teacherCombo.getSelectedItem() instanceof Teacher){
            Teacher teacher = new Teacher();
            teacher = (Teacher) teacherCombo.getSelectedItem();
            resultTable.getModel().setValueAt(teacher, resultTable.getSelectedRow(), resultTable.getSelectedColumn() + 1);
            if(updated){
                pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getTeacherid(),
                    dataModel.getModelData().get(resultTable.getSelectedRow()));
                pending = true;
            }
            System.out.println("Selected Teacher: " + teacher);
            System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
            System.out.println("Updated Grade Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
        }
    }
    
    private void gradeComboActionPerformed(ActionEvent evt) {
        if(updated){
            pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getTeacherid(),
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
        
        gradeTeachers = new ArrayList<>();
        if(!searchKey.isEmpty()){
            for(GradeTeacher gradeTeacher : allGradeTeachers){
                if(gradeTeacher.getGrade().toLowerCase().startsWith(searchKey) || gradeTeacher.getTeacher().toString().toLowerCase().startsWith(searchKey))
                    gradeTeachers.add(gradeTeacher);
            }
        }else{
            for(GradeTeacher gradeTeacher : allGradeTeachers){
                gradeTeachers.add(gradeTeacher);
            }
        }
        loadListModel();
        loadTableModel();
        if(!gradeTeachers.isEmpty()){
            currentRec = 0;
            updateDisplay(gradeTeachers.get(currentRec));
        }
    }
    
    private void tableReFormat(){
        TableFormat.addComboBoxToCell(resultTable, colNames.get(1).getName(), teacherCombo);
        TableFormat.addComboBoxToCell(resultTable, colNames.get(0).getName(), gradeCombo);
         Map<String,Integer> columns = new HashMap<>();
        columns.put(colNames.get(1).getName(), 130);
        TableFormat.resizeColumnWidthStatics(resultTable, columns);
    }
    
    private void termBeginsDateChanged(DateChangeEvent event) {
        Date termStart = termBegins.getDate()==null?null:Date.valueOf(termBegins.getDate());
        for(GradeTeacher teacher : gradeTeachers){
            teacher.setTermbegins(termStart);
            if(updated){
                pendingRecords.put(teacher.getGradeteacherid(),teacher);
                pending = true;
            }
        }
        dataModel.setModelData(gradeTeachers);
        System.out.println("TermBegins Date Changed....Reloading Table Data Model!!!");
        tableReFormat();
    }
    
    private void termEndsDateChanged(DateChangeEvent event) {
        Date termStops = termEnds.getDate()==null?null:Date.valueOf(termEnds.getDate());
        for(GradeTeacher teacher : gradeTeachers){
            teacher.setTermends(termStops);
            if(updated){
                pendingRecords.put(teacher.getGradeteacherid(),teacher);
                pending = true;
            }
        }
        dataModel.setModelData(gradeTeachers);
        System.out.println("TermEnds Date Changed....Reloading Table Data Model!!!");
        tableReFormat();
    }
    
    private void nextTermBeginsDateChanged(DateChangeEvent event) {
        Date nextTermStart = nextTermBegins.getDate()==null?null:Date.valueOf(nextTermBegins.getDate());
        for(GradeTeacher teacher : gradeTeachers){
            teacher.setNexttermbegins(nextTermStart);
            if(updated){
                pendingRecords.put(teacher.getGradeteacherid(),teacher);
                pending = true;
            }
        }
        dataModel.setModelData(gradeTeachers);
        System.out.println("NextTermBegins Date Changed....Reloading Table Data Model!!!");
        tableReFormat();
    }
    
    private void nextTermEndsDateChanged(DateChangeEvent event) {
        Date nextTermStops = nextTermEnds.getDate()==null?null:Date.valueOf(nextTermEnds.getDate());
        for(GradeTeacher teacher : gradeTeachers){
            teacher.setNexttermends(nextTermStops);
            if(updated){
                pendingRecords.put(teacher.getGradeteacherid(),teacher);
                pending = true;
            }
        }
        System.out.println("NextTermEnds Date Changed....Reloading Table Data Model!!!");
        dataModel.setModelData(gradeTeachers);
        tableReFormat();
    }
    
    private void txtNumDaysStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtNumDaysStateChanged
        int numDays = (Integer) txtNumDays.getValue();
        for(GradeTeacher teacher : gradeTeachers){
            teacher.setNumofdays(numDays);
            if(updated){
                pendingRecords.put(teacher.getGradeteacherid(),teacher);
                pending = true;
            }
        }
        dataModel.setModelData(gradeTeachers);
        System.out.println("Number of Days Changed....Reloading Table Data Model!!!");
        tableReFormat();
    }//GEN-LAST:event_txtNumDaysStateChanged
    
    private void txtTYearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentYear = Integer.parseInt(txtTYear.getSelectedItem().toString());
        System.out.println("Year Changed....Loading Data!!!");
        updated=pending=false;
        loadData();
    }  
    
    private void txtTermActionPerformed(java.awt.event.ActionEvent evt) {                                        
        TermSystem term = (TermSystem) txtTerm.getSelectedItem();
        currentTerm = term.getTerm();
        System.out.println("Term Changed....Loading Data!!!");
        System.out.println("Grade Teachers: " + gradeTeachers + "\tGrade Teachers Empty?: " + gradeTeachers.isEmpty());
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
            java.util.logging.Logger.getLogger(GradeTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GradeTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GradeTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GradeTeacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GradeTeacherForm().setVisible(true);
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
    private javax.swing.JList<String> gradeTeacherList;
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
    private com.github.lgooddatepicker.components.DatePicker nextTermBegins;
    private com.github.lgooddatepicker.components.DatePicker nextTermEnds;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane studentsScrollPane;
    private javax.swing.JScrollPane tableScrollPane;
    private com.github.lgooddatepicker.components.DatePicker termBegins;
    private com.github.lgooddatepicker.components.DatePicker termEnds;
    private javax.swing.JSpinner txtNumDays;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JComboBox<String> txtTYear;
    private javax.swing.JComboBox<String> txtTerm;
    // End of variables declaration//GEN-END:variables

    
    
    /**
   * SampleDateVetoPolicy, A veto policy is a way to disallow certain dates from being selected in
   * calendar. A vetoed date cannot be selected by using the keyboard or the mouse.
   */
  private static class MyDateVetoPolicy implements DateVetoPolicy {

    /**
     * isDateAllowed, Return true if a date should be allowed, or false if a date should be vetoed.
     */
    @Override
    public boolean isDateAllowed(LocalDate date) {
      // Disallow days 7 to 11.
      if ((date.getDayOfMonth() >= 7) && (date.getDayOfMonth() <= 11)) {
        return false;
      }
      // Disallow odd numbered saturdays.
      if ((date.getDayOfWeek() == DayOfWeek.SATURDAY) && ((date.getDayOfMonth() % 2) == 1)) {
        return false;
      }
      // Allow all other days.
      return true;
    }
  }
  
   /**
   * SampleHighlightPolicy, A highlight policy is a way to visually highlight certain dates in the
   * calendar. These may be holidays, or weekends, or other significant dates.
   */
  private static class MyHighlightPolicy implements DateHighlightPolicy {

    /**
     * getHighlightInformationOrNull, Implement this function to indicate if a date should be
     * highlighted, and what highlighting details should be used for the highlighted date.
     *
     * <p>If a date should be highlighted, then return an instance of HighlightInformation. If the
     * date should not be highlighted, then return null.
     *
     * <p>You may (optionally) fill out the fields in the HighlightInformation class to give any
     * particular highlighted day a unique foreground color, background color, or tooltip text. If
     * the color fields are null, then the default highlighting colors will be used. If the tooltip
     * field is null (or empty), then no tooltip will be displayed.
     *
     * <p>Dates that are passed to this function will never be null.
     */
    @Override
    public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
//      // Highlight a chosen date, with a tooltip and a red background color.
//      if (date.getDayOfMonth() == 25) {
//        return new HighlightInformation(Color.red, null, "It's the 25th!");
//      }
      // Highlight all Saturdays with a unique background and foreground color.
      if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
        return new HighlightInformation(Color.BLUE, Color.WHITE, "It's Saturday!");
      }
      // Highlight all Sundays with default colors and a tooltip.
      if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
        return new HighlightInformation(Color.BLUE, Color.WHITE, "It's Sunday!");
      }
      // All other days should not be highlighted.
      return null;
    }
  }
}

