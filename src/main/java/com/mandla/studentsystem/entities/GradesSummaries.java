/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author mlots
 */

@Entity
@Immutable
@Table(name="gradessummaries")
public class GradesSummaries {
    @Id
    @Column(name="ID")
    private Integer Id;
    
    @Column(name="grade")
    private String grade;
    
    @Column(name="average")
    private Double average;
    
    @Column(name="tyear")
    private Integer tyear;
    
    @Column(name="term")
    private Integer term;

    public GradesSummaries() {
    }

    public Integer getId() {
        return Id;
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

    @Override
    public String toString() {
        return String.format("%-20s", this.getGrade());
    }
    
    
}
