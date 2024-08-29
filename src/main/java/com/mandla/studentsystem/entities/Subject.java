/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author mlots
 */
@Entity
@Table(name="subject")
public class Subject implements Comparable<Subject>{
    @Id
    @Column(name="subjectid")
    private Integer subjectid;
    
    @Column(name="title")
    private String title;
    
    @Column(name="department")
    private String department;

    public Subject() {
    }

    public Subject(Integer subjectid, String title) {
        this.subjectid = subjectid;
        this.title = title;
    }

    public Subject(Integer subjectid, String title, String department) {
        this.subjectid = subjectid;
        this.title = title;
        this.department = department;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Integer subjectid) {
        this.subjectid = subjectid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("[%04d] %-40s", getSubjectid(), getTitle());
    }

    @Override
    public int compareTo(Subject o) {
        return this.getTitle().compareTo(o.getTitle());
    }
}
