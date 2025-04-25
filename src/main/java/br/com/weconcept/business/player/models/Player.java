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

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
