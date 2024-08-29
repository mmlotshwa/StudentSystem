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
@Table(name = "student")
public class Student implements Comparable<Student>{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="studentid")
    private Integer studentid;
    
    @Column(name = "firstname")
    private String firstname;
    
    @Column(name = "surname")
    private String surname;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "grade")
    private String grade;
    
    @Column(name = "rstatus")
    private String rstatus;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "dob")
    private Date dob;

    public Student() {
    }

    public Student(String firstname, String surname, String gender, String grade, String rstatus, String email, Date dob) {
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.grade = grade;
        this.rstatus = rstatus;
        this.email = email;
        this.dob = dob;
    }
    
    @Id
    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(Integer studentid) {
        this.studentid = studentid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRstatus() {
        return rstatus;
    }

    public void setRstatus(String rstatus) {
        this.rstatus = rstatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
    @Override
    public String toString() {
        return String.format("%-60s", "[" + getGrade() + "] " + getSurname() + ", " + getFirstname());
    }

    @Override
    public int compareTo(Student o) {
        if(this.getSurname().compareTo(o.getSurname()) == 0)
            return this.getFirstname().compareTo(o.getFirstname());
        return this.getSurname().compareTo(o.getSurname());
    }
    
    
}
