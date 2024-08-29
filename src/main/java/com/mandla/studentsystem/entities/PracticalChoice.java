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
@Table(name="practicalchoice")
public class PracticalChoice {
    @Column(name="grade")
    private String grade;
    
    @Id
    @Column(name="studentid")
    private Integer studentid;
    
    @ManyToOne
    @JoinColumn(name="studentid", updatable=false, insertable=false)
    Student student;
    
    @Column(name="agriculture")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean agriculture;
    
    @Column(name="design_tech")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean design_tech;
    
    @Column(name="food_nutrition")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean food_nutrition;

    public PracticalChoice() {
    }

    public PracticalChoice(String grade, Integer studentid, Boolean agriculture, Boolean design_tech, Boolean food_nutrition) {
        this.grade = grade;
        this.studentid = studentid;
        this.agriculture = agriculture;
        this.design_tech = design_tech;
        this.food_nutrition = food_nutrition;
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

    public Boolean getAgriculture() {
        return agriculture;
    }

    public void setAgriculture(Boolean agriculture) {
        this.agriculture = agriculture;
    }

    public Boolean getDesign_tech() {
        return design_tech;
    }

    public void setDesign_tech(Boolean design_tech) {
        this.design_tech = design_tech;
    }

    public Boolean getFood_nutrition() {
        return food_nutrition;
    }

    public void setFood_nutrition(Boolean food_nutrition) {
        this.food_nutrition = food_nutrition;
    }
    
    @Override
    public String toString() {
        return String.format("%-50s", "[" + getGrade() + "] " + getStudent().getSurname() + ", " + getStudent().getFirstname());
    }
    
}
