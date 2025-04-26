package br.com.weconcept.business.usescases.ranking.services;

import br.com.weconcept.business.usescases.challenge.models.ChallengeResult;
import br.com.weconcept.business.usescases.challenge.repositories.ChallengeRepository;
import br.com.weconcept.business.usescases.player.models.Player;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class RankingService {

    private final ChallengeRepository resultRepository;

    public RankingService(ChallengeRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Map<String, Integer> getGlobalRanking() {
        Map<String, Integer> resultGlobalRanking = resultRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        ChallengeResult::getPlayerName,
                        Collectors.summingInt(ChallengeResult::getScore)));
        return resultGlobalRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public Map<String, Integer> getTournamentRanking(UUID tournamentId) {
        Map<String, Integer> resultTournamentRanking = resultRepository.findAll().stream()
                .filter(r -> r.getTournament().getId().equals(tournamentId))
                .collect(Collectors.groupingBy(
                        ChallengeResult::getPlayerName,
                        Collectors.summingInt(ChallengeResult::getScore)));
        return resultTournamentRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }
}
