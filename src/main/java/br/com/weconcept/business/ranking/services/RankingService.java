package br.com.weconcept.business.ranking.services;

import br.com.weconcept.business.challenge.models.ChallengeResult;
import br.com.weconcept.business.challenge.repositories.ChallengeRepository;
import br.com.weconcept.business.player.models.Player;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class RankingService {

    private final ChallengeRepository resultRepository;

    public RankingService(ChallengeRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Map<Player, Integer> getGlobalRanking() {
        return resultRepository.findAll().stream()
                .collect(Collectors.groupingBy(ChallengeResult::getPlayer,
                        Collectors.summingInt(ChallengeResult::getScore)));
    }

    public Map<Player, Integer> getTournamentRanking(UUID tournamentId) {
        return resultRepository.findAll().stream()
                .filter(r -> r.getTournament().getId().equals(tournamentId))
                .collect(Collectors.groupingBy(ChallengeResult::getPlayer,
                        Collectors.summingInt(ChallengeResult::getScore)));
    }
}
