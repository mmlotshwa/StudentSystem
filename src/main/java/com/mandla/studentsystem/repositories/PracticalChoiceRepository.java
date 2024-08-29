/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.PracticalChoice;
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
public class PracticalChoiceRepository {
    private static SessionFactory factory;
    private static Session session;

    public PracticalChoiceRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new PracticalChoice());
        session = factory.openSession();
    }
    
    
    
    public void addPracticalChoice(PracticalChoice practicalChoice) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(practicalChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save PracticalChoice: " + he);
        }
    }
    
    public PracticalChoice updatePracticalChoice(PracticalChoice practicalChoice) throws HibernateException{
        Transaction trans = null;
        PracticalChoice mergedPracticalChoice = new PracticalChoice();
        
        try{
          trans = session.beginTransaction();
          session.evict(practicalChoice);
          mergedPracticalChoice = (PracticalChoice) session.merge(practicalChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - PracticalChoice Update: " + he);
        }
        return mergedPracticalChoice;
    }
    
    public void deletePracticalChoice(PracticalChoice practicalChoice) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(practicalChoice);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - PracticalChoice Delete: " + he);
        }
    }
    
    public void insertPracticalChoices(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            String insertHQL = "INSERT INTO practicalchoice(studentid,grade) SELECT s.studentid, s.grade FROM student s WHERE "
                   + "((LEFT(s.grade, 1) = '1' AND s.rstatus = \"A\") OR (s.studentid NOT IN (SELECT DISTINCT r.studentid FROM "
                   + "registration r WHERE (r.tyear = :tyear AND r.term = :term)) AND s.rstatus = \"A\" AND LEFT(s.grade,1) != 'A'))";
            MutationQuery query = session.createNativeMutationQuery(insertHQL);
            query.setParameter("tyear", tyear);
            query.setParameter("term", term);
            int numCreatedRecords = query.executeUpdate();
            trans.commit();
        }catch(HibernateException he ){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - PracticalChoice Delete: " + he);
        }
    }
    
    public void deleteAllPracticalChoices() throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
           String insertHQL = "DELETE FROM practicalchoice";
          int numCreatedRecords = session.createNativeMutationQuery(insertHQL).executeUpdate();
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - PracticalChoice Delete: " + he);
        }
    }
    
    public List<PracticalChoice> getAllPracticalChoices() throws HibernateException{
        List<PracticalChoice> practicalChoices = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            practicalChoices = session.createQuery("FROM PracticalChoice AS LC ORDER BY LC.grade",PracticalChoice.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - PracticalChoice Read: " + he);
        }
        return practicalChoices;
    }
    
    public List<PracticalChoice> getPracticalChoicesByGrade(String newgrade) throws HibernateException{
        List<PracticalChoice> practicalChoices = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM PracticalChoice AS LC WHERE LC.grade = :grade "
                    + "ORDER BY S.grade",PracticalChoice.class);
            query.setParameter("grade",newgrade);
            
            practicalChoices = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - PracticalChoice Read by Grade: " + he);
        }
        return practicalChoices;
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = PracticalChoice.class.getDeclaredFields();
       
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
