/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name="subjectschedules")
public class SubjectSchedules {
    @Column(name="grade")
    private String grade;
     
    @Column(name="subjectid")
    private Integer subjectid;
    
    @ManyToOne
    @JoinColumn(name="subjectid",insertable=false,updatable=false)
    Subject subject;
    
    @Column(name="optionlevel")
    private Integer optionlevel;
    
    @Column(name="optiontype")
    private String optiontype;
    
    @Id
    @Column(name = "ID")
    private Long id;

    public SubjectSchedules() {
    }

    public String getGrade() {
        return grade;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public Subject getSubject() {
        return subject;
    }

    public Integer getOptionlevel() {
        return optionlevel;
    }

    public String getOptiontype() {
        return optiontype;
    }

    public Long getId() {
        return id;
    }
    
    

    @Override
    public String toString() {
        return String.format("%-65s", this.getSubject().toString().trim() + " {" + this.getGrade().trim() + "}");
    }
}
