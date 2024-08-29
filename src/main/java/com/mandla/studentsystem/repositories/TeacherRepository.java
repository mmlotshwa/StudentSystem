/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.repositories;

import com.mandla.studentsystem.entities.Teacher;
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
public class TeacherRepository {
    private static SessionFactory factory;
    private static Session session;

    public TeacherRepository() {
        factory = HibernateSessionFactoryUtil.getSessionFactory(new Teacher());
        session = factory.openSession();
    }
    
    
    
    public void addTeacher(Teacher teacher) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.persist(teacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Save Teacher: " + he);
        }
    }
    
    public Teacher updateTeacher(Teacher teacher) throws HibernateException{
        Transaction trans = null;
        Teacher mergedTeacher = new Teacher();
        
        try{
          trans = session.beginTransaction();
          session.evict(teacher);
          mergedTeacher = (Teacher) session.merge(teacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Teacher Update: " + he);
        }
        return mergedTeacher;
    }
    
    public void deleteTeacher(Teacher teacher) throws HibernateException{
        Transaction trans = null;
        
        try{
          trans = session.beginTransaction();
          session.remove(teacher);
          trans.commit();
        }catch(HibernateException he){
            if (trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Teacher Delete: " + he);
        }
    }
    
    public List<Teacher> getAllTeachers() throws HibernateException{
        List<Teacher> teachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            teachers = session.createQuery("FROM Teacher AS S ORDER BY S.department, S.title",Teacher.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Teacher Read: " + he);
        }
        return teachers;
    }
    
     public List<String> getAllDepartments() throws HibernateException{
        List<Object> departments = new ArrayList<>();
        List<String> depts = new ArrayList<>();
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            departments = session.createQuery("SELECT DISTINCT T.department FROM Teacher AS T WHERE T.department IS NOT NULL "
                    + "ORDER BY T.department",Object.class).list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Teacher Dept Read: " + he);
        }
        for(Object department : departments)
            depts.add(department.toString());
        
        return depts;
    }
    
    public List<Teacher> getTeachersByDept(String newdept) throws HibernateException{
        List<Teacher> teachers = new ArrayList<>();
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Teacher AS S WHERE S.department = :department "
                    + "ORDER BY S.department, S.title",Teacher.class);
            query.setParameter("department",newdept);
            teachers = query.list();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Teacher Read by Grade: " + he);
        }
        return teachers;
    }
    
    public Teacher getTeachersByID(int teacherid) throws HibernateException{
        Teacher teacher;
        
        Transaction trans = null;
        
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("FROM Teacher AS S WHERE S.teacherid = :teacherid ",Teacher.class);
            query.setParameter("teacherid",teacherid);
            teacher = (Teacher) query.getSingleResult();
            trans.commit();
        }catch(HibernateException he){
            if(trans!=null)
                trans.rollback();
            throw new HibernateException("Hibernate - Teacher Read by Grade: " + he);
        }
        return teacher;
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
