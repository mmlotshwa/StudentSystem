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
import java.sql.Date;

/**
 *
 * @author mlots
 */
@Entity
@Table(name="termsystem")
public class TermSystem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="termid")
    private Integer termid;
    
    @Column(name="term")
    private Integer term;
    
    @Column(name="startmonth")
    private Integer startMonth;

    public TermSystem() {
    }

    public TermSystem(Integer term, Integer startMonth) {
        this.term = term;
        this.startMonth = startMonth;
    }

    public Integer getTermid() {
        return termid;
    }

    public void setTermid(Integer termid) {
        this.termid = termid;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    @Override
    public String toString() {
        return String.format("%d", getTerm());
    }    
}
