/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.entities;

import com.mandla.studentsystem.entities.Student;
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
@Table(name="studentsummaries")
public class StudentSummaries {
   @Id
    @Column(name="ID")
    private Integer Id;
    
    @Column(name="studentid")
    private Integer studentid;
    
    @ManyToOne
    @JoinColumn(name="studentid",insertable=false,updatable=false)
    private Student student;
    
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

    public StudentSummaries() {
    }

    public Integer getId() {
        return Id;
    }

    public Integer getStudentid() {
        return studentid;
    }

    public Student getStudent() {
        return student;
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
        return String.format("%45s", this.getStudent().getSurname() + ", " + this.getStudent().getFirstname().charAt(0)
        + " {" + this.getGrade() + "}");
    }
}
