/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.TermSystem;
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
public class TermSystemRepository {
    private static SessionFactory factory;
    private static Session session;

    public TermSystemRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new TermSystem());
        session = factory.openSession();
    }
    
    public void addTerm(TermSystem term){
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(term);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to save Term Record: " + he);
        }
    }
    
    public TermSystem updateTerm(TermSystem term){
        Transaction trans = null;
        TermSystem mergedTerm = new TermSystem();
        
        try{
          trans = session.beginTransaction();
          session.evict(term);
          mergedTerm = (TermSystem) session.merge(term);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to save Term Record: " + he);
        }
        return mergedTerm;
    }
    
    public void deleteTerm(TermSystem term){
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(term);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to delete Term Record: " + he);
        }
    }
    
    public List<TermSystem> getAllTerms(){
        List<TermSystem> terms = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            terms = session.createQuery("FROM TermSystem AS G ORDER BY G.term",TermSystem.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            System.out.println("Failed to retrieve Term Records: " + he);
        }
        return terms;
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
