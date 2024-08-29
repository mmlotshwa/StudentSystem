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
import jakarta.persistence.Table;

/**
 *
 * @author mlots
 */
@Entity
@Table(name="gradename")
public class GradeName {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "gradeid")
    private Integer gradeid;
    
    @Column(name = "grade")
    private String grade;

    public GradeName() {
    }

    public GradeName(String grade) {
        this.grade = grade;
    }

    public Integer getGradeid() {
        return gradeid;
    }

    public void setGradeid(Integer gradeid) {
        this.gradeid = gradeid;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return String.format("%-20s", getGrade());
    }
    
    
}
