package br.com.weconcept.business.usescases.challenge.repositories;

import br.com.weconcept.business.usescases.challenge.models.ChallengeResult;
import br.com.weconcept.business.usescases.player.models.Player;
import br.com.weconcept.business.usescases.tournament.models.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<ChallengeResult, UUID> {
    List<ChallengeResult> findByPlayer(Player player);
    List<ChallengeResult> findByTournament(Tournament tournament);
}
