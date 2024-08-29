/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.LanguageChoice;
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
public class LanguageChoiceRepository {
    private static SessionFactory factory;
    private static Session session;

    public LanguageChoiceRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new LanguageChoice());
        session = factory.openSession();
    }
    
    
    
    public void addLanguageChoice(LanguageChoice languageChoice) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(languageChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save LanguageChoice: " + he);
        }
    }
    
    public LanguageChoice updateLanguageChoice(LanguageChoice languageChoice) throws HibernateException{
        Transaction trans = null;
        LanguageChoice mergedLanguageChoice = new LanguageChoice();
        
        try{
          trans = session.beginTransaction();
          session.evict(languageChoice);
          mergedLanguageChoice = (LanguageChoice) session.merge(languageChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Update: " + he);
        }
        return mergedLanguageChoice;
    }
    
    public void deleteLanguageChoice(LanguageChoice languageChoice) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(languageChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Delete: " + he);
        }
    }
    
    public List<LanguageChoice> getAllLanguageChoices() throws HibernateException{
        List<LanguageChoice> languageChoices = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            languageChoices = session.createQuery("FROM LanguageChoice AS LC ORDER BY LC.grade",LanguageChoice.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Read: " + he);
        }
        return languageChoices;
    }
    
    public List<LanguageChoice> getLanguageChoicesByGrade(String newgrade) throws HibernateException{
        List<LanguageChoice> languageChoices = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM LanguageChoice AS LC WHERE LC.grade = :grade "
                    + "ORDER BY S.grade",LanguageChoice.class);
            query.setParameter("grade",newgrade);
            
            languageChoices = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Read by Grade: " + he);
        }
        return languageChoices;
    }
    
    public void insertNewYearLanguageChoices(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            String insertHQL = "INSERT INTO languagechoice(studentid,grade) SELECT s.studentid, s.grade FROM student s WHERE "
                   + "((LEFT(s.grade,1) = '1' AND s.rstatus = \"A\") OR (s.studentid NOT IN (SELECT DISTINCT r.studentid FROM "
                   + "registration r WHERE (r.tyear = :tyear AND r.term = :term)) AND s.rstatus = \"A\" AND LEFT(s.grade,1) != 'A'))";
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
    
    public void insertNewTermLanguageChoices(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            String insertHQL = "INSERT INTO languagechoice(studentid,grade) SELECT s.studentid, s.grade FROM student s WHERE "
                   + "((s.studentid NOT IN (SELECT DISTINCT r.studentid FROM registration r WHERE"
                   + " (r.tyear = :tyear AND r.term = :term)) AND s.rstatus = \"A\" AND LEFT(s.grade,1) != 'A'))";
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
    
    public void deleteAllLanguageChoices() throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
           String insertHQL = "DELETE FROM languagechoice";
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
        
        Field [] fields = LanguageChoice.class.getDeclaredFields();
       
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
