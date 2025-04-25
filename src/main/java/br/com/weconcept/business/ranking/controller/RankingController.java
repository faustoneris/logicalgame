package br.com.weconcept.business.ranking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.ranking.services.RankingService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("global")
    public ResponseEntity<Map<String, Integer>> globalRanking() {
        return ResponseEntity.ok(rankingService.getGlobalRanking());
    }

    @GetMapping("tournament/{tournamentId}")
    public ResponseEntity<Map<String, Integer>> tournamentRanking(@PathVariable UUID tournamentId) {
        return ResponseEntity.ok(rankingService.getTournamentRanking(tournamentId));
    }
}
