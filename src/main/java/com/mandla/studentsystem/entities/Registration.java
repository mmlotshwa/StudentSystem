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
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author mlots
 */

@Entity
@Table(name="registration")
public class Registration {
    @Column(name="grade")
    private String grade;
    
    @Column(name="subjectid")
    private Integer subjectid;
    
    @ManyToOne
    @JoinColumn(name="subjectid", insertable=false, updatable=false)
    private Subject subject;
    
    @Column(name="studentid")
    private Integer studentid;
    
    @ManyToOne
    @JoinColumn(name="studentid", insertable=false, updatable=false)
    private Student student;
   
    @Column(name="exammark")
    private Integer exammark;
    
     @Column(name="tavemark")
    private Integer tavemark;
    
    @Column(name="comments")
    private String comments;
    
    @Column(name="tyear")
    private Integer tyear;
    
    @Column(name="term")
    private Integer term;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="registrationid")
    private Integer registrationid;

    public Registration() {
    }

    public Registration(String grade, Integer subjectid, Integer studentid, Integer exammark, Integer tavemark, String comments, Integer tyear, Integer term) {
        this.grade = grade;
        this.subjectid = subjectid;
        this.studentid = studentid;
        this.exammark = exammark;
        this.tavemark = tavemark;
        this.comments = comments;
        this.tyear = tyear;
        this.term = term;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(Integer studentid) {
        this.studentid = studentid;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getExammark() {
        return exammark;
    }

    public void setExammark(Integer exammark) {
        this.exammark = exammark;
    }

    public Integer getTavemark() {
        return tavemark;
    }

    public void setTavemark(Integer tavemark) {
        this.tavemark = tavemark;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Integer getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(Integer registrationid) {
        this.registrationid = registrationid;
    }

    @Override
    public String toString() {
        return String.format("%-60s", "(" + getGrade() + ") - " + getSubject().toString().trim() + " {" +
                getStudentid() + "}");
    }
}
