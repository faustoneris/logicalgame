package br.com.weconcept.business.usescases.player.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerModel {

    private UUID id;

    private String name;

    private int age;

    public PlayerModel() {
        super();
    }

    public PlayerModel(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.age = player.getAge();
    }

    public static PlayerModel of(Player player) {
        return new PlayerModel(player);
    }

    public int getAge() {
        return age;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
