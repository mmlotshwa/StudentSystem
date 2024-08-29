/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.SubjectSummaries;
import com.mandla.studentsystem.utils.HibernateSessionFactoryUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author mlots
 */
public class SubjectSummariesRepository {
    private static SessionFactory factory;
    private static Session session;

    public SubjectSummariesRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new SubjectSummaries());
        session = factory.openSession();
    }
    
    public List<SubjectSummaries> getAllSubjectSummaries() throws HibernateException{
        List<SubjectSummaries> summaries = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            summaries = session.createQuery("FROM SubjectSummaries AS S ORDER BY S.grade, S.subjectid",SubjectSummaries.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Schedules Read: " + he);
        }
        return summaries;
    }
    
    public List<SubjectSummaries> getSubjectSummariesByGrade(String grade) throws HibernateException{
        List<SubjectSummaries> summaries = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SubjectSummaries AS S WHERE S.grade = :grade "
                    + "ORDER BY S.grade, S.subjectid",SubjectSummaries.class);
            query.setParameter("grade",grade);
            summaries = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Dept Read: " + he);
        }
        
        return summaries;
    }
    
    public List<SubjectSummaries> getSubjectSummariesByYearTerm(int tyear, int term) throws HibernateException{
        List<SubjectSummaries> summaries = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SubjectSummaries AS S WHERE S.tyear = :tyear "
                    + "AND S.term = :term ORDER BY S.grade, S.subjectid",SubjectSummaries.class);
            query.setParameter("tyear",tyear);
            query.setParameter("term",term);
            summaries = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Dept Read: " + he);
        }
        
        return summaries;
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = SubjectSummaries.class.getDeclaredFields();
        Method [] methods = SubjectSummaries.class.getDeclaredMethods();
        
       for(Method method : methods){
           System.out.println(method.getName());
       }
        
        for(Field field : fields){
            if(field != null){
                columnNames.add(field);
            }
        }
        System.out.println(columnNames);
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
