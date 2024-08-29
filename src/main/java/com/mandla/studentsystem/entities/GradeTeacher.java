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
import java.sql.Date;

/**
 *
 * @author mlots
 */
@Entity
@Table(name="gradeteacher")
public class GradeTeacher {
    @Column(name="grade")
    private String grade;
    
    @Column(name="teacherid")
    private Integer teacherid;
    
    @ManyToOne
    @JoinColumn(name="teacherid", insertable=false, updatable=false)
    private Teacher teacher;
    
    @Column(name="tyear")
    private Integer tyear;
    
    @Column(name="term")
    private Integer term;
    
    @Column(name="termbegins")
    private Date termbegins;
    
    @Column(name="termends")
    private Date termends;
    
    @Column(name="numofdays")
    private Integer numofdays;
    
    @Column(name="nexttermbegins")
    private Date nexttermbegins;
    
    @Column(name="nexttermends")
    private Date nexttermends;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="gradeteacherid")
    private Integer gradeteacherid;

    public GradeTeacher() {
    }

    public GradeTeacher(Integer teacherid, Integer tyear, Integer term, String grade, Date termbegins, Date termends, Integer numofdays, Date nexttermbegins, Date nexttermends) {
        this.teacherid = teacherid;
        this.tyear = tyear;
        this.term = term;
        this.grade = grade;
        this.termbegins = termbegins;
        this.termends = termends;
        this.numofdays = numofdays;
        this.nexttermbegins = nexttermbegins;
        this.nexttermends = nexttermends;
    }

    public Integer getGradeteacherid() {
        return gradeteacherid;
    }

    public void setGradeteacherid(Integer gradeteacherid) {
        this.gradeteacherid = gradeteacherid;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getTermbegins() {
        return termbegins;
    }

    public void setTermbegins(Date termbegins) {
        this.termbegins = termbegins;
    }

    public Date getTermends() {
        return termends;
    }

    public void setTermends(Date termends) {
        this.termends = termends;
    }

    public Integer getNumofdays() {
        return numofdays;
    }

    public void setNumofdays(Integer numofdays) {
        this.numofdays = numofdays;
    }

    public Date getNexttermbegins() {
        return nexttermbegins;
    }

    public void setNexttermbegins(Date nexttermbegins) {
        this.nexttermbegins = nexttermbegins;
    }

    public Date getNexttermends() {
        return nexttermends;
    }

    public void setNexttermends(Date nexttermends) {
        this.nexttermends = nexttermends;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return String.format("%-20s%-40s", getGrade(),getTeacher().getSurname() + ", " + getTeacher().getFirstName());
    }
}
