/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.Registration;
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
public class RegistrationRepository {
  private static SessionFactory factory;
    private static Session session;

    public RegistrationRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new Registration());
        session = factory.openSession();
    }

    public void addRegistration(Registration registration) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(registration);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save Registration: " + he);
        }
    }
    
    public void addBatchRegistration(List<Registration> registrations) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          for(Registration registration : registrations){
            session.persist(registration);
          }
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save Registration: " + he);
        }
    }
    
    public Registration updateRegistration(Registration registration) throws HibernateException{
        Transaction trans = null;
        Registration mergedRegistration = new Registration();
        
        try{
          trans = session.beginTransaction();
          session.evict(registration);
          mergedRegistration = (Registration) session.merge(registration);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Registration Update: " + he);
        }
        return mergedRegistration;
    }
    
    public void deleteRegistration(Registration registration) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(registration);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Registration Delete: " + he);
        }
    }
    
    public List<Registration> getAllRegistrations() throws HibernateException{
        List<Registration> registrations = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            registrations = session.createQuery("FROM Registration AS GT ORDER BY GT.tyear, GT.term, GT.grade",Registration.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Registration Read: " + he);
        }
        return registrations;
    }
    
    public List<Registration> getRegistrationsByYearTerm(int year, int term) throws HibernateException{
        List<Registration> registrations = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Registration AS GT WHERE GT.tyear = :tyear And GT.term = :term "
                    + "ORDER BY GT.tyear, GT.term, GT.grade",Registration.class);
            query.setParameter("tyear",year);
            query.setParameter("term",term);
            registrations = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Registration Read by Term & Year: " + he);
        }
        return registrations;
    }
    
    public List<Registration> getRegistrationsByYear(int year) throws HibernateException{
        List<Registration> registrations = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Registration AS S WHERE S.tyear = :tyear "
                    + "ORDER BY S.tyear, S.term, S.grade",Registration.class);
            query.setParameter("tyear",year);
            registrations = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Registration Read by Subject: " + he);
        }
        return registrations;
    }
    
    public List<Registration> getRegistrationsByGrade(String grade) throws HibernateException{
        List<Registration> registrations = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Registration AS S WHERE S.grade = :grade "
                    + "ORDER BY S.tyear, S.term, S.grade",Registration.class);
            query.setParameter("grade",grade);
            registrations = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Registration Read by Subject: " + he);
        }
        return registrations;
    }
    
    /*
    **  Carries over registrations from the previous term to the current term within the same year.
    **  Students choose their specialization at Form/Grade 2, they have all subjects at Form/Grade 1.
    **  But Form/Grade 2 up to 4 they only do their chosen specialization subjects (any subjects).
    **  At Form/Grade 1 each term they try out a different practical subject of the 3 subjects.
    **  When new students join in a term within the year, they have to be registered to subjects:
    **      At Form/Grade 1 is all subjects but they choose the Second Language (French or Setswana).
    **      At Form/Grade 2 and above they choose their specials, practical and Secon Language.
    */
    public void insertNewTermRegistrationAll(int tyear, int term) throws HibernateException{
        Transaction trans = null;
        int numCreatedRecords = 0;
        String insertHQL = "";
        try{
            trans = session.beginTransaction();
            //Carrying over all the Form/Grade 2 and above as they are.....
            insertHQL = "INSERT INTO registration (studentid, tyear, term, grade, subjectid)" 
                    + " SELECT r.studentid, :tyear AS tyear, :term AS term, s.grade, r.subjectid" 
                    + " FROM registration r INNER JOIN student s ON r.studentid = s.studentid" 
                    + " WHERE (s.rstatus = \"A\" AND r.tyear = :currentYear AND r.term = :oldTerm AND LEFT(s.grade,1) != '1')";
            numCreatedRecords = doInsertTermRegistrations(insertHQL, tyear, term);
            
            //Carrying over the Form/Grade 1 registrations but excluding the pratical so they can choose...
            insertHQL = "INSERT INTO registration (studentid, tyear, term, grade, subjectid) SELECT r.studentid, :tyear AS tyear," 
                    + " :term AS term, s.grade, r.subjectid FROM registration r INNER JOIN student s ON r.studentid = s.studentid" 
                    + " WHERE (s.rstatus = \"A\" AND r.tyear = :currentYear AND r.term = :oldTerm AND LEFT(s.grade,1) = '1' AND r.subjectid != 445 AND"
                    + " r.subjectid != 600 AND r.subjectid != 648)";
            numCreatedRecords = doInsertTermRegistrations(insertHQL, tyear, term);
            
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - LanguageChoice Insert: " + he);
        }
    }
    
    private int doInsertTermRegistrations(String insertHQL, int tyear, int term) throws HibernateException{
        int oldTerm = term - 1;
        int numCreatedRecords = 0;
        try{
        
            MutationQuery query = session.createNativeMutationQuery(insertHQL);
            query.setParameter("tyear", tyear);
            query.setParameter("term", term);
            query.setParameter("currentYear", tyear);
            query.setParameter("oldTerm", oldTerm);
            numCreatedRecords = query.executeUpdate();
        
        }catch(HibernateException he){
            throw new HibernateException("Hibernate - LanguageChoice Insert: " + he);
        }
        return numCreatedRecords;
    }
    
    private int doInsertNewYearRegistrations(String insertHQL, int tyear, int term, int oldTerm) throws HibernateException{
        int oldYear = tyear - 1;
        int numCreatedRecords = 0;
        try{
            MutationQuery query = session.createNativeMutationQuery(insertHQL);
            query.setParameter("tyear", tyear);
            query.setParameter("term", term);
            query.setParameter("oldYear", oldYear);
            query.setParameter("oldTerm", oldTerm);
            numCreatedRecords = query.executeUpdate();
        }catch(HibernateException he){
            throw new HibernateException("Hibernate - Auto Registration Insert: " + he);
        }
        return numCreatedRecords;
    }
    
    public void insertNewYearTermRegistrations(int tyear, int term, int oldTerm) throws HibernateException{
        Transaction trans = null;
        int numCreatedRecords = 0;
        String insertHQL = "";
        try{
            trans = session.beginTransaction();
            //Carry over registration records from the previous for Form/Grade 3 upwards...
            insertHQL = "INSERT INTO registration (studentid, tyear, term, grade, subjectid)" 
                    + " SELECT r.studentid, :tyear AS tyear, :term AS term, s.grade, r.subjectid" 
                    + " FROM registration r INNER JOIN student s ON r.studentid = s.studentid" 
                    + " WHERE (s.rstatus = \"A\" AND r.tyear = :oldYear AND r.term = :oldTerm AND LEFT(s.grade,1) != '2' AND LEFT(s.grade,1) != '1')";
            numCreatedRecords = doInsertNewYearRegistrations(insertHQL,tyear,term,oldTerm);
            
            trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Auto Registration Insert: " + he);
        }
    }
    
    public ArrayList<Field> getColumnNames(){
        ArrayList<Field> columnNames = new ArrayList<>();
        
        Field [] fields = Registration.class.getDeclaredFields();
       
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
