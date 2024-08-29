/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author mlots
 */

@Entity
@Immutable
@Table(name="subjectsummaries")
public class SubjectSummaries {
    @Id
    @Column(name="ID")
    private Integer Id;
    
    @Column(name="subjectid")
    private Integer subjectid;
    
    @ManyToOne
    @JoinColumn(name="subjectid",insertable=false,updatable=false)
    private Subject subject;
    
    @Column(name="grade")
    private String grade;
    
    @Column(name="average")
    private Double average;
    
    @Column(name="tyear")
    private Integer tyear;
    
    @Column(name="term")
    private Integer term;
    
    @Column(name="ranked")
    private Integer ranked;

    public SubjectSummaries() {
    }

    public Integer getId() {
        return Id;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }

    public Double getAverage() {
        return average;
    }

    public Integer getTyear() {
        return tyear;
    }

    public Integer getTerm() {
        return term;
    }

    public Integer getRanked() {
        return ranked;
    }

    @Override
    public String toString() {
        return String.format("[%04d] %-20s", this.getSubject().getSubjectid(), this.getGrade());
    }
}
