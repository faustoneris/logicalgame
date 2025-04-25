package br.com.weconcept.services;

import br.com.weconcept.business.challenge.models.ChallengeResult;
import br.com.weconcept.business.challenge.repositories.ChallengeRepository;
import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.ranking.services.RankingService;
import br.com.weconcept.business.tournament.models.Tournament;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private ChallengeRepository resultRepository;

    @InjectMocks
    private RankingService rankingService;

    private Player player1;
    private Player player2;
    private UUID tournamentId;

    @BeforeEach
    void setUp() {
        player1 = new Player();
        player1.setId(UUID.randomUUID());
        player1.setName("Player 1");

        player2 = new Player();
        player2.setId(UUID.randomUUID());
        player2.setName("Player 2");

        tournamentId = UUID.randomUUID();
    }

    @Test
    void getGlobalRanking_ShouldReturnCorrectRanking() {
        List<ChallengeResult> results = Arrays.asList(
                createResult(player1, 10),
                createResult(player1, 5),
                createResult(player2, 7)
        );

        when(resultRepository.findAll()).thenReturn(results);

        Map<Player, Integer> ranking = rankingService.getGlobalRanking();

        assertEquals(2, ranking.size());
        assertEquals(15, ranking.get(player1));
        assertEquals(7, ranking.get(player2));
    }

    @Test
    void getGlobalRanking_WithNoResults_ShouldReturnEmptyMap() {
        when(resultRepository.findAll()).thenReturn(Collections.emptyList());

        Map<Player, Integer> ranking = rankingService.getGlobalRanking();

        assertTrue(ranking.isEmpty());
    }

    @Test
    void getTournamentRanking_ShouldReturnFilteredRanking() {
        Tournament tournament1 = new Tournament();
        tournament1.setId(tournamentId);

        Tournament tournament2 = new Tournament();
        tournament2.setId(UUID.randomUUID());

        List<ChallengeResult> results = Arrays.asList(
                createResult(player1, tournament1, 10),
                createResult(player1, tournament1, 5),
                createResult(player2, tournament2, 7)
        );

        when(resultRepository.findAll()).thenReturn(results);

        Map<Player, Integer> ranking = rankingService.getTournamentRanking(tournamentId);

        assertEquals(1, ranking.size());
        assertEquals(15, ranking.get(player1));
        assertNull(ranking.get(player2));
    }

    private ChallengeResult createResult(Player player, int score) {
        return createResult(player, new Tournament(), score);
    }

    private ChallengeResult createResult(Player player, Tournament tournament, int score) {
        ChallengeResult result = new ChallengeResult();
        result.setPlayer(player);
        result.setTournament(tournament);
        result.setScore(score);
        return result;
    }
}
