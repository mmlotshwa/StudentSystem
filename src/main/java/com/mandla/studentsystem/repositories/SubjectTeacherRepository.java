/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.SubjectTeacher;
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
public class SubjectTeacherRepository {
    private static SessionFactory factory;
    private static Session session;

    public SubjectTeacherRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new SubjectTeacher());
        session = factory.openSession();
    }

    public void addSubjectTeacher(SubjectTeacher subjectTeacher) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(subjectTeacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save SubjectTeacher: " + he);
        }
    }
    
    public void addBatchSubjectTeacher(List<SubjectTeacher> subjectTeachers) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          for(SubjectTeacher subjectTeacher : subjectTeachers){
            session.persist(subjectTeacher);
          }
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save SubjectTeacher: " + he);
        }
    }
    
    public SubjectTeacher updateSubjectTeacher(SubjectTeacher subjectTeacher) throws HibernateException{
        Transaction trans = null;
        SubjectTeacher mergedSubjectTeacher = new SubjectTeacher();
        
        try{
          trans = session.beginTransaction();
          session.evict(subjectTeacher);
          mergedSubjectTeacher = (SubjectTeacher) session.merge(subjectTeacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Update: " + he);
        }
        return mergedSubjectTeacher;
    }
    
    public void deleteSubjectTeacher(SubjectTeacher subjectTeacher) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(subjectTeacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Delete: " + he);
        }
    }
    
    public List<SubjectTeacher> getAllSubjectTeachers() throws HibernateException{
        List<SubjectTeacher> subjectTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            subjectTeachers = session.createQuery("FROM SubjectTeacher AS GT ORDER BY GT.tyear, GT.term, GT.grade",SubjectTeacher.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Read: " + he);
        }
        return subjectTeachers;
    }
    
    public List<SubjectTeacher> getSubjectTeachersByYearTerm(int year, int term) throws HibernateException{
        List<SubjectTeacher> subjectTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SubjectTeacher AS GT WHERE GT.tyear = :tyear And GT.term = :term "
                    + "ORDER BY GT.tyear, GT.term, GT.grade",SubjectTeacher.class);
            query.setParameter("tyear",year);
            query.setParameter("term",term);
            subjectTeachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Read by Term & Year: " + he);
        }
        return subjectTeachers;
    }
    
    public List<SubjectTeacher> getSubjectTeachersByYear(int year) throws HibernateException{
        List<SubjectTeacher> subjectTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SubjectTeacher AS GT WHERE GT.tyear = :tyear "
                    + "ORDER BY GT.tyear, GT.term, GT.grade",SubjectTeacher.class);
            query.setParameter("tyear",year);
            subjectTeachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Read by Subject: " + he);
        }
        return subjectTeachers;
    }
    
    public List<SubjectTeacher> getSubjectTeachersByGradeYearTerm(String grade, int year, int term) throws HibernateException{
        List<SubjectTeacher> subjectTeachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SubjectTeacher AS ST WHERE ST.grade = :grade AND ST.tyear = :tyear"
                    + " AND ST.term = :term ORDER BY ST.teacherid, ST.subjectid",SubjectTeacher.class);
            query.setParameter("grade",grade);
            query.setParameter("tyear",year);
            query.setParameter("term",term);
            subjectTeachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SubjectTeacher Read by Subject: " + he);
        }
        return subjectTeachers;
    }
    
    public void insertNewTermSubjectTeachers(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        int numCreatedRecords = 0;
        int oldTerm = term - 1;
        
        try{
            trans = session.beginTransaction();
            //Carrying over all the Form/Grade 2 and above as they are.....
            String insertHQL = "INSERT INTO subjectteacher(teacherid, subjectid, tyear, term, grade)" 
                    + " SELECT s.teacherid, s.subjectid, :tyear AS tyear, :term AS term, s.grade" 
                    + " FROM subjectteacher s WHERE (s.tyear = :currentYear AND s.term = :oldTerm)";
                    
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
        
        Field [] fields = SubjectTeacher.class.getDeclaredFields();
       
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
