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
@Table(name="teacher")
public class Teacher implements Comparable<Teacher>{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="teacherid")
    private Integer teacherid;
    
    @Column(name="title")
    private String title;
    
    @Column(name="firstname")
    private String firstName;
    
    @Column(name="surname")
    private String surname;
    
    @Column(name="gender")
    private String gender;
    
    @Column(name="email")
    private String email;
    
    @Column(name="department")
    private String department;

    public Teacher() {
    }

    public Teacher(String title, String firstName, String surname, String gender, String email, String department) {
        this.title = title;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.email = email;
        this.department = department;
    }

    public Teacher(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("%-50s",  getSurname() + ", " + getFirstName());
    }

    @Override
    public int compareTo(Teacher o) {
        if(this.getSurname().compareTo(o.getSurname()) == 0)
            return this.getFirstName().compareTo(o.getFirstName());
        return this.getSurname().compareTo(o.getSurname());
    }
}
