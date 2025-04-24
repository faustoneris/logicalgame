package br.com.weconcept.business.player.models;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int age;

    private LocalDateTime birthdayDate;

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public LocalDateTime getBirthdayDate() {
        return birthdayDate;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthdayDate(LocalDateTime birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public void setName(String name) {
        this.name = name;
    }




}
