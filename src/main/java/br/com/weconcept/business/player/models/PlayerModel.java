package br.com.weconcept.business.player.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerModel {

    private UUID id;

    private String name;

    private int age;

    private LocalDateTime birthdayDate;

    public PlayerModel() {
        super();
    }

    public PlayerModel(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.age = player.getAge();
        this.birthdayDate = player.getBirthdayDate();
    }

    public static PlayerModel of(Player player) {
        return new PlayerModel(player);
    }

    public int getAge() {
        return age;
    }

    public LocalDateTime getBirthdayDate() {
        return birthdayDate;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
