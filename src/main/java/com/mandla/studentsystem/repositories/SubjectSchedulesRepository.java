/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.SubjectSchedules;
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
public class SubjectSchedulesRepository {
   private static SessionFactory factory;
    private static Session session;

    public SubjectSchedulesRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new SubjectSchedules());
        session = factory.openSession();
    }
    
    public List<SubjectSchedules> getAllSchedules() throws HibernateException{
        List<SubjectSchedules> schedules = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            schedules = session.createQuery("FROM SubjectSchedules AS S ORDER BY S.grade, S.subjectid",SubjectSchedules.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Schedules Read: " + he);
        }
        return schedules;
    }
    
    public List<SubjectSchedules> getSchedulesByGrade(String grade) throws HibernateException{
        List<SubjectSchedules> schedules = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SubjectSchedules AS S WHERE S.grade = :grade "
                    + "ORDER BY S.grade, S.subjectid",SubjectSchedules.class);
            query.setParameter("grade",grade);
            schedules = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Dept Read: " + he);
        }
        
        return schedules;
    }
    
    public List<SubjectSchedules> getSpecializationSchedules() throws HibernateException{
        List<SubjectSchedules> schedules = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            schedules = session.createQuery("FROM SubjectSchedules  AS SS WHERE SS.optiontype != 'General' "
                    + "AND SS.optiontype != 'Language' AND SS.subjectid != 1123 "
                    + "AND SS.optionlevel != 1 ORDER BY SS.grade, SS.subjectid",SubjectSchedules.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Dept Read: " + he);
        }
        
        return schedules;
    }
    
    public List<SubjectSchedules> getTermSpecializationSchedules() throws HibernateException{
        List<SubjectSchedules> schedules = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            schedules = session.createQuery("FROM SubjectSchedules  WHERE ((optiontype = \"General\" AND (LEFT(grade,1) = '2'"
                    + " OR LEFT(grade,1) = '3' OR LEFT(grade,1) = '4')) OR (LEFT(grade,1) = '1' AND optiontype != \"Practical\"" 
                    + " AND optiontype != \"Language\")) ORDER BY grade, subjectid",SubjectSchedules.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Dept Read: " + he);
        }
        
        return schedules;
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = SubjectSchedules.class.getDeclaredFields();
        Method [] methods = SubjectSchedules.class.getDeclaredMethods();
        
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
