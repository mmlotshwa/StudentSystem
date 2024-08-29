/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mandla.studentsystem.gui;

import com.mandla.studentsystem.repositories.RegistrationRepository;
import com.mandla.studentsystem.repositories.SubjectSchedulesRepository;
import com.mandla.studentsystem.repositories.TermSystemRepository;
import com.mandla.studentsystem.repositories.SpecializationChoiceRepository;
import com.mandla.studentsystem.repositories.StudentRepository;
import com.mandla.studentsystem.repositories.GradeNameRepository;
import com.mandla.studentsystem.entities.Subject;
import com.mandla.studentsystem.entities.SpecializationChoice;
import com.mandla.studentsystem.entities.SubjectSchedules;
import com.mandla.studentsystem.entities.Student;
import com.mandla.studentsystem.entities.GradeName;
import com.mandla.studentsystem.entities.Registration;
import com.mandla.studentsystem.entities.TermSystem;
import com.mandla.studentsystem.utils.TableFormat;
import com.mandla.studentsystem.utils.DataTableModel;
import com.mandla.studentsystem.utils.ComboBoxCellEditor;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.hibernate.HibernateException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mlots
 */
public class SpecializationChoiceForm extends javax.swing.JFrame {
    private List<SpecializationChoice> specializationChoices;
    private List<SpecializationChoice> AllSpecializationChoices;
    private final SpecializationChoiceRepository repository;
    private int currentRec = - 1;
    private int numRecords = -1;
    private enum Status {VIEW, EDIT, NEW};
    private Status viewStatus;
    private List<GradeName> grades;
    private ArrayList<Field> colNames;
    private ArrayList<Method> colMethods;
    private ArrayList<Method> colSetMethods;
    private ArrayList<Field> scheduleColNames;
    private ArrayList<Method> scheduleColMethods;
    private DataTableModel<SpecializationChoice> dataModel;
    private TableRowSorter<TableModel> sorter;
    private JTable resultTable;
    private int currentYear;
    private int currentTerm;
    private List<TermSystem> terms;
    private boolean updated; //Flag for all records status; true-if records read from database, false-if records created for new term
    private Map<String,Integer> subjects;
    private List<SubjectSchedules> subjectData;
    private Map<Integer,Student> studentSearch;
    private Map<Integer,Subject> subjectSearch;
    
    
    /**
     * Creates new form SpecializationChoiceForm
     */
    public SpecializationChoiceForm() {
        initComponents();
        updated = false;
        repository = new SpecializationChoiceRepository();
        AllSpecializationChoices = new ArrayList<>();
        loadComboBoxes();
        createSearchMaps();
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
    
    private void createSearchMaps(){
        try{
            StudentRepository studentRepo = new StudentRepository();
            List<Student> students = new ArrayList<>();
            subjectData = new ArrayList<>();
            subjects = new HashMap<>();
            subjectSearch = new HashMap<>();
            studentSearch = new HashMap<>();
            
            SubjectSchedulesRepository subjectRepository = new SubjectSchedulesRepository();
            subjectData = subjectRepository.getSpecializationSchedules();

            subjectData.sort(Comparator.comparing(SubjectSchedules::getSubjectid)
                    .thenComparing(SubjectSchedules::getSubject));
            
            
            for(SubjectSchedules subject : subjectData){
                subjects.put(subject.toString().trim(),subject.getSubjectid());
                subjectSearch.put(subject.getSubjectid(), subject.getSubject());
            }
            
            
            students = studentRepo.getAllActiveStudents();
            
            for(Student student : students){
                studentSearch.put(student.getStudentid(), student);
            }
            
            scheduleColNames = new ArrayList<>();
            scheduleColMethods = new ArrayList<>();
            
            scheduleColNames = subjectRepository.getColumnNames();
            for(Field colName : scheduleColNames){
                try {
                    scheduleColMethods.add(SubjectSchedules.class.getDeclaredMethod("get" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1)));
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(SpecializationChoiceForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }catch(HibernateException he){
            dialogMessage("Contact Administrator:" + he, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        
    private void loadListModel() {
        DefaultListModel<String> specializationChoiceListModel = new DefaultListModel<>();
        for(SpecializationChoice specializationChoice : specializationChoices){
            specializationChoiceListModel.addElement(specializationChoice.toString());
        }
        specializationChoiceList.setModel(specializationChoiceListModel);
    }
    
    private void activateListeners(){
        txtTerm.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTermActionPerformed(evt);
        });
        
        txtTYear.addActionListener((java.awt.event.ActionEvent evt) -> {
            txtTYearActionPerformed(evt);
        });
    }
    
    private void loadTableModel(){
        dataModel = new DataTableModel<>(specializationChoices,colNames,colMethods,colSetMethods,subjects,subjects);
        resultTable = new JTable(dataModel);
        sorter = new TableRowSorter<>(dataModel);
        resultTable.setRowSorter(sorter);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tableReFormat();
        
        tableScrollPane.setViewportView(resultTable); 
        tableScrollPane.repaint();
        numRecords = resultTable.getRowCount();
        resultTable.getModel().addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) {
                tableDataChanged(e);
            }
            
        });
    }
     
    private void buttonStatus(){    
        if(viewStatus == Status.NEW || viewStatus == Status.EDIT){
            headingPanelFormat();
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            headingLabel.setText(((viewStatus == Status.NEW)?"New Specialization Choices":"Edit Specialization Choices"));
            dataFieldStatus(true);
            
        }else{
            headingPanelFormat();
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            headingLabel.setText("View Specialization Choices");
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
        specializationChoices = new ArrayList<>();
        try{
            viewStatus = Status.EDIT;  
            if(currentTerm == terms.get(0).getTerm() && AllSpecializationChoices.isEmpty())
                repository.insertNewYearSpecializationChoices(currentYear - 1, terms.get(terms.size() - 1).getTerm());
            else if(AllSpecializationChoices.isEmpty())
                repository.insertNewTermSpecializationChoices(currentYear, currentTerm - 1);
            
            AllSpecializationChoices = repository.getAllSpecializationChoices();
            specializationChoices = AllSpecializationChoices;
            if(!specializationChoices.isEmpty()){
               currentRec=0;
               
               updated = true;
            }else{
               dialogMessage("There are no students registered for Special Choices!!\n"
                       + "This step will be ended!!", "Information", JOptionPane.INFORMATION_MESSAGE);
               this.dispose();
            }
            colNames = new ArrayList<>();
            colMethods = new ArrayList<>();
            colSetMethods = new ArrayList<>();
            colNames = repository.getColumnNames();
            for(Field colName : colNames){
                try {
                    colMethods.add(SpecializationChoice.class.getDeclaredMethod("get" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1)));
                    colSetMethods.add(SpecializationChoice.class.getDeclaredMethod("set" + Character.toString(colName.getName().charAt(0)).toUpperCase() + colName.getName().substring(1),colName.getType()));
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(SpecializationChoiceForm.class.getName()).log(Level.SEVERE, null, ex);
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
        if(!specializationChoices.isEmpty()){
            resultTable.setRowSelectionInterval(currentRec, currentRec);
            navigationButtons();
        }
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
        specializationChoiceList = new javax.swing.JList<>();
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

        specializationChoiceList.setBackground(new java.awt.Color(51, 153, 255));
        specializationChoiceList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        specializationChoiceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                specializationChoiceListValueChanged(evt);
            }
        });
        studentsScrollPane.setViewportView(specializationChoiceList);

        headingPanel.setBackground(new java.awt.Color(51, 153, 255));

        headingLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        headingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        headingLabel.setText("View Specialization Choices");

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
                .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 831, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
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
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saveButton))
                    .addGroup(headingPanelLayout.createSequentialGroup()
                        .addGroup(headingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancelButton)
                            .addComponent(editButton))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(headingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        txtTYear.setPreferredSize(new java.awt.Dimension(72, 26));

        txtTerm.setPreferredSize(new java.awt.Dimension(72, 26));

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(studentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addComponent(gradeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addGap(0, 1, Short.MAX_VALUE)
                                .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(bodyPanelLayout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTYear, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(194, 194, 194)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(headingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addContainerGap(8, Short.MAX_VALUE)
                        .addComponent(gradeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(headingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
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
                        .addComponent(studentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 672, Short.MAX_VALUE)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                        .addGap(31, 31, 31))))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(headerPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
//        viewStatus = Status.NEW;
//        SpecializationChoice specializationChoice = new SpecializationChoice("Select?", 0, 0, currentYear, currentTerm);
//        specializationChoice.setTeacher(new Teacher("Teacher Name", "Select"));
//        specializationChoice.setSubject(new Subject(0,"Select Subject"));
//        
//        dataModel.getModelData().add(specializationChoice);
//        dataModel.fireTableDataChanged();
//        numRecords++;
//        buttonStatus();
//        navigationButtons();
//        navButtonStatus(false);
    }//GEN-LAST:event_newButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        
        if(dialogMessage("Are you sure you want to discard the changes??\n", "Warning", JOptionPane.YES_NO_OPTION)){
            loadData();
        }else{
            dialogMessage("Click the Save Button to save the changes!!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        RegistrationRepository regRepository = new RegistrationRepository();
        List<Registration> registrations = new ArrayList<>();
        for(SpecializationChoice specializationChoice : specializationChoices){
            int StudentID = specializationChoice.getStudentid();
            int SubjectID = 0;
            if (specializationChoice.getSubject1() != null && specializationChoice.getSubject1() != 0){
                SubjectID = specializationChoice.getSubject1();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }
            if (specializationChoice.getSubject2() != null && specializationChoice.getSubject2() != 0){
                SubjectID = specializationChoice.getSubject2();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }
            if (specializationChoice.getSubject3() != null && specializationChoice.getSubject3() != 0){
                SubjectID = specializationChoice.getSubject3();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }
            if (specializationChoice.getSubject4() != null && specializationChoice.getSubject4() != 0){
                SubjectID = specializationChoice.getSubject4();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }
            if (specializationChoice.getSubject5() != null && specializationChoice.getSubject5() != 0){
                SubjectID = specializationChoice.getSubject5();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }
            if (specializationChoice.getSubject6() != null && specializationChoice.getSubject6() != 0){
                SubjectID = specializationChoice.getSubject6();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }
            if (specializationChoice.getSubject7() != null && specializationChoice.getSubject7() != 0){
                SubjectID = specializationChoice.getSubject7();
                Registration registration = new Registration(specializationChoice.getGrade(),SubjectID,StudentID,
                            0,0,null,currentYear,currentTerm);
                registration.setSubject(subjectSearch.get(SubjectID));
                registration.setStudent(studentSearch.get(StudentID));
                registrations.add(registration);
            }    
        }
        
        if(!registrations.isEmpty()){
            regRepository.addBatchRegistration(registrations);
            dialogMessage("Save Complete! " + registrations.size() + " records were saved!!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }else{
            dialogMessage("You have no data to save!!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_saveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
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
                SpecializationChoice specializationChoice = dataModel.getModelData().get(rows[i]);
                if(dialogMessage("Are Sure you want to delete the Record: \nStudentID: " + specializationChoice.getStudentid()
                        + "Student: " + specializationChoice.getStudent().toString().trim() + " Grade: " + specializationChoice.getGrade()
                        , "Warning", JOptionPane.YES_NO_OPTION)){
                    dataModel.getModelData().remove(rows[i]);
                    dataModel.fireTableRowsDeleted(rows[i], rows[i]);
                    
                    repository.deleteSpecializationChoice(specializationChoice);
                }
            }
        }else if(resultTable.getSelectedRowCount() == 1){
            int row = resultTable.getSelectedRow();
            SpecializationChoice specializationChoice = dataModel.getModelData().get(row);
            if(dialogMessage("Are Sure you want to delete the Record: \nStudentID: " + specializationChoice.getStudentid()
                    + "Student: " + specializationChoice.getStudent().toString().trim() + " Grade: " + specializationChoice.getGrade() 
                    , "Warning", JOptionPane.YES_NO_OPTION)){
                dataModel.getModelData().remove(row);
                dataModel.fireTableRowsDeleted(row, row);
                
                repository.deleteSpecializationChoice(specializationChoice);
            }
        }else{
            dialogMessage("Select the record(s) you want to delete first!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
            
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
        repository.deleteAllSpecializationChoices();
        repository.closeSession();
    }//GEN-LAST:event_formWindowClosing

    private void specializationChoiceListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_specializationChoiceListValueChanged
        if(!specializationChoices.isEmpty()){
            if(!specializationChoiceList.isSelectionEmpty()){
                currentRec = specializationChoiceList.getSelectedIndex();
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }else{
                currentRec = 0;
                resultTable.setRowSelectionInterval(currentRec, currentRec);
            }
        }
    }//GEN-LAST:event_specializationChoiceListValueChanged

    private void gradeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeComboBoxActionPerformed
        String grade = gradeComboBox.getSelectedItem().toString().trim();
        
        if(grade.equalsIgnoreCase("All")){
            loadData();
        }else{
            specializationChoices = new ArrayList<>();
            for(SpecializationChoice specializationChoice : AllSpecializationChoices){
                if(specializationChoice.getGrade().equalsIgnoreCase(grade)){
                    specializationChoices.add(specializationChoice);
                }
            }
        }
        if(!specializationChoices.isEmpty()){
            numRecords = specializationChoices.size();
            loadListModel();
            loadTableModel();
            currentRec = 0;
            updateDisplay();
        }else{
            dialogMessage("You don't have specializationChoices in " + grade + ". Capture SpecializationChoice Records!", "Message", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        }
    }//GEN-LAST:event_gradeComboBoxActionPerformed
    
    private void tableDataChanged(TableModelEvent e){
        int row = e.getFirstRow();
        int column = e.getColumn();
        int SubjectID = 0;
        boolean same = false;
        switch(column){
            case 3 ->{
                SubjectID = specializationChoices.get(row).getSubject1();
                if (specializationChoices.get(row).getSubject2() != null && SubjectID == specializationChoices.get(row).getSubject2())
                    same = true;
                else if (specializationChoices.get(row).getSubject3()!= null && SubjectID == specializationChoices.get(row).getSubject3())
                    same = true;
                else if (specializationChoices.get(row).getSubject4() != null && SubjectID == specializationChoices.get(row).getSubject4())
                    same = true;
                else if (specializationChoices.get(row).getSubject5()!= null && SubjectID == specializationChoices.get(row).getSubject5())
                    same = true;
                else if (specializationChoices.get(row).getSubject6() != null && SubjectID == specializationChoices.get(row).getSubject6())
                    same = true;
                else if (specializationChoices.get(row).getSubject7() != null && SubjectID == specializationChoices.get(row).getSubject7())
                    same = true;
            }
            case 4 ->{
                SubjectID = specializationChoices.get(row).getSubject2();
                if (specializationChoices.get(row).getSubject1() != null && SubjectID == specializationChoices.get(row).getSubject1())
                    same = true;
                else if (specializationChoices.get(row).getSubject3() != null && SubjectID == specializationChoices.get(row).getSubject3())
                    same = true;
                else if (specializationChoices.get(row).getSubject4() != null && SubjectID == specializationChoices.get(row).getSubject4())
                    same = true;
                else if (specializationChoices.get(row).getSubject5() != null && SubjectID == specializationChoices.get(row).getSubject5())
                    same = true;
                else if (specializationChoices.get(row).getSubject6() != null && SubjectID == specializationChoices.get(row).getSubject6())
                    same = true;
                else if (specializationChoices.get(row).getSubject7() !=  null && SubjectID == specializationChoices.get(row).getSubject7())
                    same = true;
            }
            case 5 ->{
                SubjectID = specializationChoices.get(row).getSubject3();
                if (specializationChoices.get(row).getSubject1() != null && SubjectID == specializationChoices.get(row).getSubject1())
                    same = true;
                else if (specializationChoices.get(row).getSubject2() != null && SubjectID == specializationChoices.get(row).getSubject2())
                    same = true;
                else if (specializationChoices.get(row).getSubject4() != null && SubjectID == specializationChoices.get(row).getSubject4())
                    same = true;
                else if (specializationChoices.get(row).getSubject5() != null && SubjectID == specializationChoices.get(row).getSubject5())
                    same = true;
                else if (specializationChoices.get(row).getSubject6() != null && SubjectID == specializationChoices.get(row).getSubject6())
                    same = true;
                else if (specializationChoices.get(row).getSubject7() !=  null && SubjectID == specializationChoices.get(row).getSubject7())
                    same = true;
            }
            case 6 ->{
                SubjectID = specializationChoices.get(row).getSubject4();
                if (specializationChoices.get(row).getSubject1() != null && SubjectID == specializationChoices.get(row).getSubject1())
                    same = true;
                else if (specializationChoices.get(row).getSubject2() != null && SubjectID == specializationChoices.get(row).getSubject2())
                    same = true;
                else if (specializationChoices.get(row).getSubject3() != null && SubjectID == specializationChoices.get(row).getSubject3())
                    same = true;
                else if (specializationChoices.get(row).getSubject5() != null && SubjectID == specializationChoices.get(row).getSubject5())
                    same = true;
                else if (specializationChoices.get(row).getSubject6() != null && SubjectID == specializationChoices.get(row).getSubject6())
                    same = true;
                else if (specializationChoices.get(row).getSubject7() !=  null && SubjectID == specializationChoices.get(row).getSubject7())
                    same = true;
            }
            case 7 ->{
                SubjectID = specializationChoices.get(row).getSubject5();
                if (specializationChoices.get(row).getSubject1() != null && SubjectID == specializationChoices.get(row).getSubject1())
                    same = true;
                else if (specializationChoices.get(row).getSubject2() != null && SubjectID == specializationChoices.get(row).getSubject2())
                    same = true;
                else if (specializationChoices.get(row).getSubject3() != null && SubjectID == specializationChoices.get(row).getSubject3())
                    same = true;
                else if (specializationChoices.get(row).getSubject4() != null && SubjectID == specializationChoices.get(row).getSubject4())
                    same = true;
                else if (specializationChoices.get(row).getSubject6() != null && SubjectID == specializationChoices.get(row).getSubject6())
                    same = true;
                else if (specializationChoices.get(row).getSubject7() !=  null && SubjectID == specializationChoices.get(row).getSubject7())
                    same = true;
            }
            case 8 ->{
                SubjectID = specializationChoices.get(row).getSubject6();
                if (specializationChoices.get(row).getSubject1() != null && SubjectID == specializationChoices.get(row).getSubject1())
                    same = true;
                else if (specializationChoices.get(row).getSubject2() != null && SubjectID == specializationChoices.get(row).getSubject2())
                    same = true;
                else if (specializationChoices.get(row).getSubject3() != null && SubjectID == specializationChoices.get(row).getSubject3())
                    same = true;
                else if (specializationChoices.get(row).getSubject4() != null && SubjectID == specializationChoices.get(row).getSubject4())
                    same = true;
                else if (specializationChoices.get(row).getSubject5() != null && SubjectID == specializationChoices.get(row).getSubject5())
                    same = true;
                else if (specializationChoices.get(row).getSubject7() !=  null && SubjectID == specializationChoices.get(row).getSubject7())
                    same = true;
            }
            case 9 ->{
                SubjectID = specializationChoices.get(row).getSubject7();
                if (specializationChoices.get(row).getSubject1() != null && SubjectID == specializationChoices.get(row).getSubject1())
                    same = true;
                else if (specializationChoices.get(row).getSubject2() != null && SubjectID == specializationChoices.get(row).getSubject2())
                    same = true;
                else if (specializationChoices.get(row).getSubject3() != null && SubjectID == specializationChoices.get(row).getSubject3())
                    same = true;
                else if (specializationChoices.get(row).getSubject4() != null && SubjectID == specializationChoices.get(row).getSubject4())
                    same = true;
                else if (specializationChoices.get(row).getSubject5() != null && SubjectID == specializationChoices.get(row).getSubject5())
                    same = true;
                else if (specializationChoices.get(row).getSubject6() !=  null && SubjectID == specializationChoices.get(row).getSubject6())
                    same = true;
            }
        }
        if(same){
            dialogMessage("The subject " + SubjectID + " has been duplicated!\nReplace/Remove this one!!", "Message", JOptionPane.INFORMATION_MESSAGE);
            resultTable.setValueAt(0, row, column);
        }
        
        updated=false;
    }
    private void teacherComboActionPerformed(ActionEvent evt) {
//        if(teacherCombo.getSelectedItem() instanceof Teacher){
//            Teacher teacher = new Teacher();
//            teacher = (Teacher) teacherCombo.getSelectedItem();
//            resultTable.getModel().setValueAt(teacher, resultTable.getSelectedRow(), resultTable.getSelectedColumn() + 1);
//            if(updated){
//                pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getSubjectteacherid(),
//                    dataModel.getModelData().get(resultTable.getSelectedRow()));
//                pending = true;
//            }
//            System.out.println("Selected Teacher: " + teacher);
//            System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
//            System.out.println("Updated Subject Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
//        }
    }
    
    private void subjectComboActionPerformed(ActionEvent evt) {
//        if(subjectCombo.getSelectedItem() instanceof Subject){
//            Subject subject = new Subject();
//            subject = (Subject) subjectCombo.getSelectedItem();
//            resultTable.getModel().setValueAt(subject, resultTable.getSelectedRow(), resultTable.getSelectedColumn() + 1);
//            if(updated){
//                pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getSubjectteacherid(),
//                    dataModel.getModelData().get(resultTable.getSelectedRow()));
//                pending = true;
//            }
//            System.out.println("Selected Subject: " + subject);
//            System.out.println("Edited Row: " + resultTable.getSelectedRow() + "\tColumn Edited: " + resultTable.getSelectedColumn());
//            System.out.println("Updated Subject Teacher Record: " + dataModel.getModelData().get(resultTable.getSelectedRow()));
//        }
    }
    
    private void gradeComboActionPerformed(ActionEvent evt) {
//        if(updated){
//            pendingRecords.put(dataModel.getModelData().get(resultTable.getSelectedRow()).getSubjectteacherid(),
//                dataModel.getModelData().get(resultTable.getSelectedRow()));
//            pending = true;
//        }
    }
    
    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        specializationChoices = new ArrayList<>();
        String searchKey = txtSearch.getText().trim().toLowerCase();
        if(!searchKey.isEmpty()){
            for(SpecializationChoice specializationChoice : AllSpecializationChoices){
                if(specializationChoice.getGrade().toLowerCase().startsWith(searchKey) 
                        || specializationChoice.getStudent().getFirstname().toLowerCase().startsWith(searchKey)
                        || specializationChoice.getStudent().getSurname().toLowerCase().startsWith(searchKey))
                    specializationChoices.add(specializationChoice);
            }
        }else{
            loadData();
        }
        if(!specializationChoices.isEmpty()){
            numRecords = specializationChoices.size();
            loadListModel();
            loadTableModel();
            currentRec = 0;
            updateDisplay();
        }else{
            loadData();
        }
    }//GEN-LAST:event_txtSearchKeyTyped
    
    private void tableReFormat(){
        ComboBoxCellEditor comboCellEditor = new ComboBoxCellEditor(resultTable,subjectData,scheduleColMethods,0);
        resultTable.getColumn(colNames.get(3).getName()).setCellEditor(comboCellEditor);
        resultTable.getColumn(colNames.get(4).getName()).setCellEditor(comboCellEditor);
        resultTable.getColumn(colNames.get(5).getName()).setCellEditor(comboCellEditor);
        resultTable.getColumn(colNames.get(6).getName()).setCellEditor(comboCellEditor);
        resultTable.getColumn(colNames.get(7).getName()).setCellEditor(comboCellEditor);
        resultTable.getColumn(colNames.get(8).getName()).setCellEditor(comboCellEditor);
        resultTable.getColumn(colNames.get(9).getName()).setCellEditor(comboCellEditor);
        Map<String,Integer> columns = new HashMap<>();
        columns.put(colNames.get(3).getName(), 200);
        columns.put(colNames.get(4).getName(), 200);
        columns.put(colNames.get(5).getName(), 200);
        columns.put(colNames.get(6).getName(), 200);
        columns.put(colNames.get(7).getName(), 200);
        columns.put(colNames.get(8).getName(), 200);
        columns.put(colNames.get(9).getName(), 200);

        TableFormat.resizeColumnWidthStatics(resultTable, columns);
    }
        
    private void txtTYearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentYear = Integer.parseInt(txtTYear.getSelectedItem().toString());
        System.out.println("Year Changed....Loading Data!!!");
        if(!AllSpecializationChoices.isEmpty())
            repository.deleteAllSpecializationChoices();
        updated=false;
        loadData();
    }  
    
    private void txtTermActionPerformed(java.awt.event.ActionEvent evt) {                                        
        TermSystem term = (TermSystem) txtTerm.getSelectedItem();
        currentTerm = term.getTerm();
        System.out.println("Term Changed....Loading Data!!!");
        specializationChoices.clear();
        System.out.println("Subject Teachers: " + specializationChoices + "\tSubject Teachers Empty?: " + specializationChoices.isEmpty());
        if(!AllSpecializationChoices.isEmpty())
            repository.deleteAllSpecializationChoices();
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
            java.util.logging.Logger.getLogger(SpecializationChoiceForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SpecializationChoiceForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SpecializationChoiceForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SpecializationChoiceForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new SpecializationChoiceForm().setVisible(true);
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
    private javax.swing.JList<String> specializationChoiceList;
    private javax.swing.JScrollPane studentsScrollPane;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JComboBox<String> txtTYear;
    private javax.swing.JComboBox<String> txtTerm;
    // End of variables declaration//GEN-END:variables

}

