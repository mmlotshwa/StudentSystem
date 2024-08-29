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

/**
 *
 * @author mlots
 */

@Entity
@Table(name="specializationchoice")
public class SpecializationChoice {
    @Column(name="grade")
    private String grade;
    
    @Id
    @Column(name="studentid")
    private Integer studentid;
    
    @ManyToOne
    @JoinColumn(name="studentid",updatable=false,insertable=false)
    private Student student;
    
    @Column(name="subject1")
    private Integer subject1;
    
    @Column(name="subject2")
    private Integer subject2;
    
    @Column(name="subject3")
    private Integer subject3;
    
    @Column(name="subject4")
    private Integer subject4;
    
    @Column(name="subject5")
    private Integer subject5;
    
    @Column(name="subject6")
    private Integer subject6;
    
    @Column(name="subject7")
    private Integer subject7;

    public SpecializationChoice() {
    }

    public SpecializationChoice(String grade, Integer studentid, Integer subject1, Integer subject2, Integer subject3, Integer subject5, Integer subject6) {
        this.grade = grade;
        this.studentid = studentid;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject5 = subject5;
        this.subject6 = subject6;
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

    public Integer getSubject1() {
        return subject1;
    }

    public void setSubject1(Integer subject1) {
        this.subject1 = subject1;
    }

    public Integer getSubject2() {
        return subject2;
    }

    public void setSubject2(Integer subject2) {
        this.subject2 = subject2;
    }

    public Integer getSubject3() {
        return subject3;
    }

    public void setSubject3(Integer subject3) {
        this.subject3 = subject3;
    }

    public Integer getSubject4() {
        return subject4;
    }

    public void setSubject4(Integer subject4) {
        this.subject4 = subject4;
    }

    public Integer getSubject5() {
        return subject5;
    }

    public void setSubject5(Integer subject5) {
        this.subject5 = subject5;
    }

    public Integer getSubject6() {
        return subject6;
    }

    public void setSubject6(Integer subject6) {
        this.subject6 = subject6;
    }

    public Integer getSubject7() {
        return subject7;
    }

    public void setSubject7(Integer subject7) {
        this.subject7 = subject7;
    }
    
    @Override
    public String toString() {
        return String.format("%-50s", "[" + getGrade() + "] " + getStudent().getSurname() + ", " + getStudent().getFirstname());
    }      
}
