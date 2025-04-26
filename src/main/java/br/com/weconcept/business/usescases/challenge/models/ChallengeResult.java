package br.com.weconcept.business.usescases.challenge.models;

import br.com.weconcept.business.usescases.player.models.Player;
import br.com.weconcept.business.usescases.tournament.models.Tournament;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ChallengeResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Tournament tournament;

    @Enumerated(EnumType.STRING)
    private ChallengeType challengeType;

    private int score;


    public ChallengeResult() {}

    public ChallengeResult(Player player, Tournament tournament, ChallengeType challengeType, int score) {
        this.player = player;
        this.tournament = tournament;
        this.challengeType = challengeType;
        this.score = score;
    }

    public static ChallengeResult ofResult(Player player, Tournament tournament, ChallengeType challengeType, int score) {
        return new ChallengeResult(player, tournament, challengeType, score);
    }

   public ChallengeType getChallengeType() {
       return challengeType;
   }

   public UUID getId() {
       return id;
   }

   public Player getPlayer() {
       return player;
   }

   public String getPlayerName() {
    if (player != null) {
        return player.getName();
    }
    return "Undefined Name";
    }

   public int getScore() {
       return score;
   }
   public Tournament getTournament() {
       return tournament;
   }

   public void setChallengeType(ChallengeType challengeType) {
       this.challengeType = challengeType;
   }
   public void setId(UUID id) {
       this.id = id;
   }

   public void setPlayer(Player player) {
       this.player = player;
   }
   public void setScore(int score) {
       this.score = score;
   }
   public void setTournament(Tournament tournament) {
       this.tournament = tournament;
   }


}
