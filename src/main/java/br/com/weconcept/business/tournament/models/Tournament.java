package br.com.weconcept.business.tournament.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

import br.com.weconcept.business.player.models.Player;

@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private boolean finished;
    private String name;
    private LocalDate date;


    @ManyToMany
    private Set<Player> players = new HashSet<>();

   public LocalDate getDate() {
       return date;
   }

   public UUID getId() {
       return id;
   }

   public String getName() {
       return name;
   }

   public Set<Player> getPlayers() {
       return players;
   }

   public boolean hasFinished() {
       return this.finished;
   }

   public void setFinished(boolean finished) {
       this.finished = finished;
   }

   public void setId(UUID id) {
       this.id = id;
   }

   public void setName(String name) {
       this.name = name;
   }
   public void setPlayers(Set<Player> players) {
        this.players = players;
   }

   public void setDate(LocalDate date) {
       this.date = date;
   }

}
