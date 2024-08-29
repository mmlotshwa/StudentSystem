/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.GradeName;
import com.mandla.studentsystem.utils.HibernateSessionFactoryUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author mlots
 */
public class GradeNameRepository {
    private static SessionFactory factory;
    private static Session session;

    public GradeNameRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new GradeName());
        session = factory.openSession();
    }
    
    
    
    public void addGrade(GradeName grade){
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(grade);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to save Grade Record: " + he);
        }
    }
    
    public GradeName updateGradeName(GradeName grade){
        Transaction trans = null;
        GradeName mergedGrade = new GradeName();
        
        try{
          trans = session.beginTransaction();
          session.evict(grade);
          mergedGrade = (GradeName) session.merge(grade);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to save Grade Record: " + he);
        }
        return mergedGrade;
    }
    
    public void deleteGrade(GradeName grade){
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(grade);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to delete Grade Record: " + he);
        }
    }
    
    public List<GradeName> getAllGrades(){
        List<GradeName> grades = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            grades = session.createQuery("FROM GradeName AS G ORDER BY G.grade",GradeName.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            System.out.println("Failed to retrieve Grade Records: " + he);
        }
        return grades;
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
