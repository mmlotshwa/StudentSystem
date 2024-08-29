/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.Student;
import com.mandla.studentsystem.utils.HibernateSessionFactoryUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

/**
 *
 * @author mlots
 */
public class StudentRepository {
    private static SessionFactory factory;
    private static Session session;

    public StudentRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new Student());
        session = factory.openSession();
    }
    
    
    
    public void addStudent(Student student) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(student);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save Student: " + he);
        }
    }
    
    public Student updateStudent(Student student) throws HibernateException{
        Transaction trans = null;
        Student mergedStudent = new Student();
        
        try{
          trans = session.beginTransaction();
          session.evict(student);
          mergedStudent = (Student) session.merge(student);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Update: " + he);
        }
        return mergedStudent;
    }
    
    public void deleteStudent(Student student) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(student);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Delete: " + he);
        }
    }
    
    public List<Student> getAllStudents() throws HibernateException{
        List<Student> students = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            students = session.createQuery("FROM Student AS S ORDER BY S.grade, S.surname, S.firstname",Student.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Read: " + he);
        }
        return students;
    }
    
    public List<Student> getAllActiveStudents() throws HibernateException{
        List<Student> students = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Student AS S WHERE S.rstatus = :rstatus "
                    + "ORDER BY S.grade, S.surname, S.firstname",Student.class);
            query.setParameter("rstatus","A");
            students = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Read: " + he);
        }
        return students;
    }
    
    public List<Student> getStudentsByGrade(String newgrade) throws HibernateException{
        List<Student> students = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Student AS S WHERE S.grade = :grade "
                    + "ORDER BY S.grade, S.surname, S.firstname",Student.class);
            query.setParameter("grade",newgrade);
            
            students = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Read by Grade: " + he);
        }
        return students;
    }
    
    public List<Student> getActiveStudentsByGrade(String newgrade) throws HibernateException{
        List<Student> students = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Student AS S WHERE S.grade = :grade AND S.rstatus = :rstatus"
                    + " ORDER BY S.grade, S.surname, S.firstname",Student.class);
            query.setParameter("grade",newgrade);
            query.setParameter("rstatus","A");
            students = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Read by Grade: " + he);
        }
        return students;
    }
    
    public List<Student> getToRegisterStudents(int tyear, int oldTerm) throws HibernateException{
        List<Student> students = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Student s WHERE ((s.studentid NOT IN (SELECT r.studentid FROM Registration r" 
                    + " WHERE tyear = :tyear AND term = :oldTerm)) AND LEFT(s.grade,1) != 'A' AND s.rstatus = \"A\")",Student.class);
            query.setParameter("tyear",tyear);
            query.setParameter("oldTerm",oldTerm);
            students = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Student Not In Previous Term: " + he);
        }
        return students;
    }
    
    public void changeStudentGradeNewYear(){
        Transaction trans = null;
        int numRecords = 0;
        String updateHQL = "";
        try{
            trans = session.beginTransaction();
            updateHQL = "UPDATE Student SET grade = REPLACE(grade,'AS','Advanced'"
                    + " WHERE (rstatus = \"A\" AND LEFT(grade,2) = 'AS')";
            numRecords = session.createNativeMutationQuery(updateHQL).executeUpdate();
            
            updateHQL = "UPDATE Student SET grade = REPLACE(grade,'3','4'"
                    + " WHERE (rstatus = \"A\" AND LEFT(grade,1) = '3')";
            numRecords = session.createNativeMutationQuery(updateHQL).executeUpdate();
            
            updateHQL = "UPDATE Student SET grade = REPLACE(grade,'2','3'"
                    + " WHERE (rstatus = \"A\" AND LEFT(grade,1) = '2')";
            numRecords = session.createNativeMutationQuery(updateHQL).executeUpdate();
            
            updateHQL = "UPDATE Student SET grade = REPLACE(grade,'1','2'"
                    + " WHERE (rstatus = \"A\" AND LEFT(grade,1) = '1')";
            numRecords = session.createNativeMutationQuery(updateHQL).executeUpdate();
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Delete: " + he);
        }
    }
    
    public void graduateExitStudentsNewYear(){
        Transaction trans = null;
        int numRecords = 0;
        String updateHQL = "";
        try{
            trans = session.beginTransaction();
            updateHQL = "UPDATE Student SET rstatus = 'G'"
                    + " WHERE (rstatus = \"A\" AND LEFT(grade,1) = '4')";
            numRecords = session.createNativeMutationQuery(updateHQL).executeUpdate();
            
            updateHQL = "UPDATE Student SET grade = 'G'"
                    + " WHERE (rstatus = \"A\" AND grade LIKE 'Advanced%')";
            numRecords = session.createNativeMutationQuery(updateHQL).executeUpdate();
            
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Delete: " + he);
        }
    }
    
    public void closeSession(){
        if(session.isOpen()){
            session.clear();
            session.close();
        }
    } 
    
    public void closeFactory(){
        if (factory.isOpen()){
            factory.close();
        }
    }
}
