package br.com.weconcept.controller;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.tournament.controller.TournamentController;
import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.services.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    private String tournamentId;
    private String playerId;
    private Tournament tournament;
    private Player player;

    @BeforeEach
    void setUp() {
        tournamentId = UUID.randomUUID().toString();
        playerId = UUID.randomUUID().toString();
        tournament = new Tournament();
        player = new Player();
    }

    @Test
    void createTournament_ShouldReturnCreatedStatus() {
        when(tournamentService.createTournament(tournament)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.createTournament(tournament);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tournament, response.getBody());
    }

    @Test
    void fetchTournaments_ShouldReturnOkStatus() {
        List<Tournament> tournaments = Collections.singletonList(tournament);
        when(tournamentService.fetchTournaments()).thenReturn(tournaments);

        ResponseEntity<List<Tournament>> response = tournamentController.fetchTournaments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournaments, response.getBody());
    }

    @Test
    void fetchTournamentById_ShouldReturnTournament() {
        when(tournamentService.fetchTournamentById(tournamentId)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.fetchTournamentById(tournamentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournament, response.getBody());
    }

    @Test
    void listTournamentsPlayers_ShouldReturnPlayersSet() {
        Set<Player> players = Collections.singleton(player);
        when(tournamentService.listTournamentsPlayers(tournamentId)).thenReturn(players);

        ResponseEntity<Set<Player>> response = tournamentController.listTournamentsPlayers(tournamentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(players, response.getBody());
    }

    @Test
    void finishTournament_ShouldReturnUpdatedTournament() {
        when(tournamentService.finishTournament(tournamentId)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.finishTournament(tournamentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournament, response.getBody());
    }

    @Test
    void addPlayer_ShouldReturnUpdatedTournament() {
        when(tournamentService.addPlayer(tournamentId, playerId)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.addPlayer(tournamentId, playerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tournament, response.getBody());
    }

    @Test
    void removePlayer_ShouldReturnUpdatedTournament() {
        when(tournamentService.removePlayer(tournamentId, playerId)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.removePlayer(tournamentId, playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournament, response.getBody());
    }
}
