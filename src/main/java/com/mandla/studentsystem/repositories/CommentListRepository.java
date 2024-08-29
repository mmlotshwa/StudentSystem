/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.CommentList;
import com.mandla.studentsystem.utils.HibernateSessionFactoryUtil;
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
public class CommentListRepository {
   private static SessionFactory factory;
    private static Session session;

    public CommentListRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new CommentList());
        session = factory.openSession();
    }
    
    
    
    public void addComment(CommentList comment){
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(comment);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to save Comment Record: " + he);
        }
    }
    
    public CommentList updateComment(CommentList comment){
        Transaction trans = null;
        CommentList mergedComment = new CommentList();
        
        try{
          trans = session.beginTransaction();
          session.evict(comment);
          mergedComment = (CommentList) session.merge(comment);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to save Comment Record: " + he);
        }
        return mergedComment;
    }
    
    public void deleteComment(CommentList comment){
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(comment);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            System.out.println("Failed to delete Comment Record: " + he);
        }
    }
    
    public List<CommentList> getAllTeacherComments(){
        List<CommentList> comments = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM CommentList AS CL WHERE CL.type = :type "
                    + "ORDER BY CL.comment",CommentList.class);
            query.setParameter("type","Teacher");
            comments = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            System.out.println("Failed to retrieve Comments Records: " + he);
        }
        return comments;
    }
    
    public List<CommentList> getAllPrincipalComments(){
        List<CommentList> comments = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM CommentList AS CL WHERE CL.type = :type "
                    + "ORDER BY CL.comment",CommentList.class);
            query.setParameter("type","Principal");
            comments = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            System.out.println("Failed to retrieve Comments Records: " + he);
        }
        return comments;
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
