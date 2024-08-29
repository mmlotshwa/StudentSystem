/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.utils;

import com.mandla.studentsystem.MainForm;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author mlots
 */
public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    
    private static SessionFactory createSessionFactory(Object object){
        try{
           Configuration configuration = new Configuration();
           configuration.configure(MainForm.class.getResource("hibernate.cfg.xml"));
           return configuration.addAnnotatedClass(object.getClass()).buildSessionFactory(); 
        }catch(HibernateException he){
            System.err.println("Initial SessionFactory creation failed: " + he);
            throw new ExceptionInInitializerError(he);
        }
    }
    
    public static SessionFactory getSessionFactory(Object object){
        sessionFactory = createSessionFactory(object);
        return sessionFactory;
    }
}
