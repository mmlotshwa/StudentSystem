/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author mlots
 */
@Entity
@Table(name="subjectteacher")
public class SubjectTeacher {
    @Column(name="grade")
    private String grade;
    
    @Column(name="teacherid")
    private Integer teacherid;
    
    @ManyToOne
    @JoinColumn(name="teacherid", insertable=false, updatable=false)
    private Teacher teacher;
    
    @Column(name="subjectid")
    private Integer subjectid;
    
    @ManyToOne
    @JoinColumn(name="subjectid", insertable=false, updatable=false)
    private Subject subject;
    
    @Column(name="tyear")
    private Integer tyear;
    
    @Column(name="term")
    private Integer term;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="subjectteacherid")
    private Integer subjectteacherid;

    public SubjectTeacher() {
    }

    public SubjectTeacher(String grade, Integer teacherid, Integer subjectid, Integer tyear, Integer term) {
        this.grade = grade;
        this.teacherid = teacherid;
        this.subjectid = subjectid;
        this.tyear = tyear;
        this.term = term;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Integer subjectid) {
        this.subjectid = subjectid;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getTyear() {
        return tyear;
    }

    public void setTyear(Integer tyear) {
        this.tyear = tyear;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getSubjectteacherid() {
        return subjectteacherid;
    }

    public void setSubjectteacherid(Integer subjectteacherid) {
        this.subjectteacherid = subjectteacherid;
    }

    @Override
    public String toString() {
        return String.format("%-70s",getSubject().toString().trim() + " (" + getTeacher().getSurname().trim() + ", " + getTeacher().getFirstName().trim() + 
                ")" + " - [{" + getGrade().trim() + "}]");
    }
}
