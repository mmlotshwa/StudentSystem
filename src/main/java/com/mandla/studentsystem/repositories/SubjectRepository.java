/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.Subject;
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
public class SubjectRepository {
    private static SessionFactory factory;
    private static Session session;

    public SubjectRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new Subject());
        session = factory.openSession();
    }
    
    
    
    public void addSubject(Subject subject) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(subject);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save Subject: " + he);
        }
    }
    
    public Subject updateSubject(Subject subject) throws HibernateException{
        Transaction trans = null;
        Subject mergedSubject = new Subject();
        
        try{
          trans = session.beginTransaction();
          session.evict(subject);
          mergedSubject = (Subject) session.merge(subject);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Update: " + he);
        }
        return mergedSubject;
    }
    
    public void deleteSubject(Subject subject) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(subject);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Delete: " + he);
        }
    }
    
    public List<Subject> getAllSubjects() throws HibernateException{
        List<Subject> subjects = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            subjects = session.createQuery("FROM Subject AS S ORDER BY S.department, S.title",Subject.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Read: " + he);
        }
        return subjects;
    }
    
     public List<String> getAllDepartments() throws HibernateException{
        List<Object> departments = new ArrayList<>();
        List<String> depts = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            departments = session.createQuery("SELECT DISTINCT S.department FROM Subject AS S ORDER BY S.department",Object.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Dept Read: " + he);
        }
        for(Object department : departments)
            depts.add(department.toString());
        
        return depts;
    }
    
    public List<Subject> getSubjectsByDept(String newdept) throws HibernateException{
        List<Subject> subjects = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Subject AS S WHERE S.department = :department "
                    + "ORDER BY S.department, S.title",Subject.class);
            query.setParameter("department",newdept);
            subjects = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Subject Read by Grade: " + he);
        }
        return subjects;
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = Subject.class.getDeclaredFields();
        Method [] methods = Subject.class.getDeclaredMethods();
        
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
