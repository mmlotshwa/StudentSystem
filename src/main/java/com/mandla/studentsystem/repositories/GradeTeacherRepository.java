/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.GradeTeacher;
import com.mandla.studentsystem.utils.HibernateSessionFactoryUtil;
import java.lang.reflect.Field;
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
public class GradeTeacherRepository {
    private static SessionFactory factory;
    private static Session session;

    public GradeTeacherRepository() throws HibernateException{
        try {
            factory = HibernateSessionFactoryUtil.getSessionFactory(new GradeTeacher());
            session = factory.openSession();
        } catch (HibernateException he) {
            throw new HibernateException(he);
        }
    }
    
    
    
    public void addGradeTeacher(GradeTeacher gradeTeacher) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(gradeTeacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save GradeTeacher: " + he);
        }
    }
    
    public GradeTeacher updateGradeTeacher(GradeTeacher gradeTeacher) throws HibernateException{
        Transaction trans = null;
        GradeTeacher mergedGradeTeacher = new GradeTeacher();
        
        try{
          trans = session.beginTransaction();
          session.evict(gradeTeacher);
          mergedGradeTeacher = (GradeTeacher) session.merge(gradeTeacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - GradeTeacher Update: " + he);
        }
        return mergedGradeTeacher;
    }
    
    public void deleteGradeTeacher(GradeTeacher gradeTeacher) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(gradeTeacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - GradeTeacher Delete: " + he);
        }
    }
    
    public List<GradeTeacher> getAllGradeTeachers() throws HibernateException{
        List<GradeTeacher> gradeTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            gradeTeachers = session.createQuery("FROM GradeTeacher AS GT ORDER BY GT.tyear, GT.term, GT.grade",GradeTeacher.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - GradeTeacher Read: " + he);
        }
        return gradeTeachers;
    }
    
    public List<GradeTeacher> getGradeTeachersByYearTerm(int year, int term) throws HibernateException{
        List<GradeTeacher> gradeTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM GradeTeacher AS GT WHERE GT.tyear = :tyear And GT.term = :term "
                    + "ORDER BY GT.tyear, GT.term, GT.grade",GradeTeacher.class);
            query.setParameter("tyear",year);
            query.setParameter("term",term);
            gradeTeachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - GradeTeacher Read by Term & Year: " + he);
        }
        return gradeTeachers;
    }
    
    public List<GradeTeacher> getGradeTeachersByYear(int year) throws HibernateException{
        List<GradeTeacher> gradeTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM GradeTeacher AS S WHERE S.tyear = :tyear "
                    + "ORDER BY S.tyear, S.term, S.grade",GradeTeacher.class);
            query.setParameter("tyear",year);
            gradeTeachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - GradeTeacher Read by Grade: " + he);
        }
        return gradeTeachers;
    }
    
    public List<GradeTeacher> getGradeTeachersByGrade(String grade) throws HibernateException{
        List<GradeTeacher> gradeTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM GradeTeacher AS S WHERE S.grade = :grade "
                    + "ORDER BY S.tyear, S.term, S.grade",GradeTeacher.class);
            query.setParameter("grade",grade);
            gradeTeachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - GradeTeacher Read by Grade: " + he);
        }
        return gradeTeachers;
    }
    
    public void insertNewTermGradeTeachers(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        int numCreatedRecords = 0;
        int oldTerm = term - 1;
        
        try{
            trans = session.beginTransaction();
            //Carrying over all the Form/Grade 2 and above as they are.....
            String insertHQL = "INSERT INTO gradeteacher (teacherid, tyear, term, grade, termbegins, termends)" 
                    + " SELECT g.teacherid, :tyear AS tyear, :term AS term, g.grade, g.nexttermbegins AS termbegins, g.nexttermends AS termends" 
                    + " FROM gradeteacher g WHERE (g.tyear = :currentYear AND g.term = :oldTerm)";
                    
            MutationQuery query = session.createNativeMutationQuery(insertHQL);
            query.setParameter("tyear", tyear);
            query.setParameter("term", term);
            query.setParameter("currentYear", tyear);
            query.setParameter("oldTerm", oldTerm);
            numCreatedRecords = query.executeUpdate();
            
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Insert: " + he);
        }
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = GradeTeacher.class.getDeclaredFields();
       
        for(Field field : fields){
            if(field != null){
                columnNames.add(field);
            }
        }
        return columnNames;
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
