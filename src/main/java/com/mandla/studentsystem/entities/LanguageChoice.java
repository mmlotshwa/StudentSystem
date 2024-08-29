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
import org.hibernate.type.TrueFalseConverter;

/**
 *
 * @author mlots
 */

@Entity
@Table(name="languagechoice")
public class LanguageChoice {
    @Column(name="grade")
    private String grade;
    
    @Id
    @Column(name="studentid")
    private Integer studentid;
    
    @ManyToOne
    @JoinColumn(name="studentid", updatable=false, insertable=false)
    Student student;
    
    @Column(name="french")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean french;
    
    @Column(name="setswana")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean setswana;

    public LanguageChoice() {
    }

    public LanguageChoice(String grade, Integer studentid, Boolean french, Boolean setswana) {
        this.grade = grade;
        this.studentid = studentid;
        this.french = french;
        this.setswana = setswana;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public Boolean getFrench() {
        return french;
    }

    public void setFrench(Boolean french) {
        this.french = french;
    }

    public Boolean getSetswana() {
        return setswana;
    }

    public void setSetswana(Boolean setswana) {
        this.setswana = setswana;
    }

    @Override
    public String toString() {
        return String.format("%-50s", "[" + getGrade() + "] " + getStudent().getSurname() + ", " + getStudent().getFirstname());
    }
}
