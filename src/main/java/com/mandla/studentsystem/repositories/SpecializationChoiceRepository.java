/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.SpecializationChoice;
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
public class SpecializationChoiceRepository {
    private static SessionFactory factory;
    private static Session session;

    public SpecializationChoiceRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new SpecializationChoice());
        session = factory.openSession();
    }
    
    
    
    public void addSpecializationChoice(SpecializationChoice specializationChoice) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(specializationChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save SpecializationChoice: " + he);
        }
    }
    
    public SpecializationChoice updateSpecializationChoice(SpecializationChoice specializationChoice) throws HibernateException{
        Transaction trans = null;
        SpecializationChoice mergedSpecializationChoice = new SpecializationChoice();
        
        try{
          trans = session.beginTransaction();
          session.evict(specializationChoice);
          mergedSpecializationChoice = (SpecializationChoice) session.merge(specializationChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SpecializationChoice Update: " + he);
        }
        return mergedSpecializationChoice;
    }
    
    public void deleteSpecializationChoice(SpecializationChoice specializationChoice) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(specializationChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SpecializationChoice Delete: " + he);
        }
    }
    
    public List<SpecializationChoice> getAllSpecializationChoices() throws HibernateException{
        List<SpecializationChoice> specializationChoices = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            specializationChoices = session.createQuery("FROM SpecializationChoice AS LC ORDER BY LC.grade",SpecializationChoice.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SpecializationChoice Read: " + he);
        }
        return specializationChoices;
    }
    
    public List<SpecializationChoice> getSpecializationChoicesByGrade(String newgrade) throws HibernateException{
        List<SpecializationChoice> specializationChoices = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM SpecializationChoice AS LC WHERE LC.grade = :grade "
                    + "ORDER BY S.grade",SpecializationChoice.class);
            query.setParameter("grade",newgrade);
            
            specializationChoices = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - SpecializationChoice Read by Grade: " + he);
        }
        return specializationChoices;
    }
    
    public void insertNewYearSpecializationChoices(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            String insertHQL = "INSERT INTO specializationchoice (studentid, grade) SELECT s.studentid, s.grade FROM student AS s" 
                    + " WHERE ((s.rstatus = \"A\" AND LEFT(s.grade,1) != 'A' AND LEFT(s.grade,1) != '1' AND LEFT(s.grade,1) = '2')" 
                    + " OR (s.studentid NOT IN (SELECT DISTINCT r.studentid FROM registration AS r WHERE (tyear = :tyear AND term = :term)))" 
                    + "	AND (s.rstatus = \"A\" AND LEFT(s.grade,1) != 'A' AND LEFT(s.grade,1) != '1'))";
            MutationQuery query = session.createNativeMutationQuery(insertHQL);
            query.setParameter("tyear", tyear);
            query.setParameter("term", term);
            int numCreatedRecords = query.executeUpdate();
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Insert: " + he);
        }
    }
    
    public void insertNewTermSpecializationChoices(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            String insertHQL = "INSERT INTO specializationchoice (studentid, grade) SELECT s.studentid, s.grade FROM student AS s" 
                    + " WHERE ((s.rstatus = \"A\" AND LEFT(s.grade,1) != 'A' AND LEFT(s.grade,1) != '1')" 
                    + " AND s.studentid NOT IN (SELECT DISTINCT r.studentid FROM registration AS r" 
                    + " WHERE tyear = :tyear AND term = :term))";
            MutationQuery query = session.createNativeMutationQuery(insertHQL);
            query.setParameter("tyear", tyear);
            query.setParameter("term", term);
            int numCreatedRecords = query.executeUpdate();
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Insert: " + he);
        }
    }
    
    public void deleteAllSpecializationChoices() throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
           String insertHQL = "DELETE FROM specializationchoice";
          int numCreatedRecords = session.createNativeMutationQuery(insertHQL).executeUpdate();
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Delete: " + he);
        }
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = SpecializationChoice.class.getDeclaredFields();
       
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
