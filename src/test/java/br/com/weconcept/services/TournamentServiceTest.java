package br.com.weconcept.services;

import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.repositories.PlayerRepository;
import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.repositories.TournamentRepository;
import br.com.weconcept.business.tournament.services.TournamentService;

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
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TournamentService tournamentService;

    private String validTournamentId;
    private String validPlayerId;
    private Tournament tournament;
    private Player player;

    @BeforeEach
    void setUp() {
        validTournamentId = UUID.randomUUID().toString();
        validPlayerId = UUID.randomUUID().toString();

        tournament = new Tournament();
        tournament.setId(UUID.fromString(validTournamentId));
        tournament.setPlayers(new HashSet<>());

        player = new Player();
        player.setId(UUID.fromString(validPlayerId));
    }

    @Test
    void createTournament_WithValidInput_ShouldReturnSavedTournament() {
        Tournament newTournament = new Tournament();
        when(tournamentRepository.save(newTournament)).thenReturn(newTournament);

        Tournament result = tournamentService.createTournament(newTournament);

        assertNotNull(result);
        verify(tournamentRepository, times(1)).save(newTournament);
    }

    @Test
    void createTournament_WithNullInput_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> tournamentService.createTournament(null));
    }

    @Test
    void fetchTournaments_ShouldReturnAllTournaments() {
        List<Tournament> expected = Arrays.asList(new Tournament(), new Tournament());
        when(tournamentRepository.findAll()).thenReturn(expected);

        List<Tournament> result = tournamentService.fetchTournaments();

        assertEquals(expected, result);
    }

    @Test
    void fetchTournamentById_WithValidId_ShouldReturnTournament() {
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(tournament));

        Tournament result = tournamentService.fetchTournamentById(validTournamentId);

        assertEquals(tournament, result);
    }

    @Test
    void fetchTournamentById_WithInvalidId_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> tournamentService.fetchTournamentById("invalid-id"));
    }

    @Test
    void fetchTournamentById_WithNonExistentId_ShouldThrowResourceNotFoundException() {
        when(tournamentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.fetchTournamentById(validTournamentId));
    }

    @Test
    void addPlayer_WithValidIds_ShouldAddPlayerToTournament() {
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(tournament));
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(player));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        Tournament result = tournamentService.addPlayer(validTournamentId, validPlayerId);

        assertTrue(result.getPlayers().contains(player));
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void removePlayer_WithValidIds_ShouldRemovePlayerFromTournament() {
        tournament.getPlayers().add(player);

        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(tournament));
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(player));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        Tournament result = tournamentService.removePlayer(validTournamentId, validPlayerId);

        assertFalse(result.getPlayers().contains(player));
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void listTournamentsPlayers_ShouldReturnTournamentPlayers() {
        tournament.getPlayers().add(player);
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(tournament));

        Set<Player> players = tournamentService.listTournamentsPlayers(validTournamentId);

        assertEquals(1, players.size());
        assertTrue(players.contains(player));
    }

    @Test
    void finishTournament_ShouldSetFinishedToTrue() {
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        Tournament result = tournamentService.finishTournament(validTournamentId);

        assertTrue(result.hasFinished());
        verify(tournamentRepository, times(1)).save(tournament);
    }
}
