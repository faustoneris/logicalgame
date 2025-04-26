package br.com.weconcept.services;

import br.com.weconcept.business.challenge.models.ChallengeResult;
import br.com.weconcept.business.challenge.models.ChallengeType;
import br.com.weconcept.business.challenge.repositories.ChallengeRepository;
import br.com.weconcept.business.challenge.services.ChallengeService;
import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.repositories.PlayerRepository;
import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.repositories.TournamentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeResultRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private ChallengeService challengeService;

    private String validPlayerId;
    private String validTournamentId;
    private Player mockPlayer;
    private Tournament mockTournament;

    @BeforeEach
    void setUp() {
        validPlayerId = UUID.randomUUID().toString();
        validTournamentId = UUID.randomUUID().toString();
        mockPlayer = new Player();
        mockTournament = new Tournament();
    }

    @Test
    void calculateFibonacci_WithValidInput_ShouldReturnCorrectResult() {
        assertEquals(0, challengeService.calculateFibonacci(0));
        assertEquals(1, challengeService.calculateFibonacci(1));
        assertEquals(1, challengeService.calculateFibonacci(2));
        assertEquals(2, challengeService.calculateFibonacci(3));
        assertEquals(55, challengeService.calculateFibonacci(10));
    }

    @Test
    void isPalindrome_WithValidInput_ShouldReturnCorrectResult() {
        assertTrue(challengeService.isPalindrome("madam"));
        assertTrue(challengeService.isPalindrome("A man, a plan, a canal, Panama"));
        assertFalse(challengeService.isPalindrome("not a palindrome"));
        assertTrue(challengeService.isPalindrome("12321"));
    }

    @Test
    void customSort_WithValidInput_ShouldReturnSortedArray() {
        int[] input = {5, 3, 8, 1, 2};
        int[] expected = {1, 2, 3, 5, 8};
        assertArrayEquals(expected, challengeService.customSort(input));
    }

    @Test
    void registerScore_WithValidInput_ShouldReturnChallengeResult() {
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(mockPlayer));
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(mockTournament));
        when(challengeResultRepository.save(any(ChallengeResult.class))).thenAnswer(i -> i.getArgument(0));

        ChallengeResult result = challengeService.registerScore(validPlayerId, validTournamentId, ChallengeType.FIBONACCI);

        assertNotNull(result);
        assertEquals(mockPlayer, result.getPlayer());
        assertEquals(mockTournament, result.getTournament());
        assertEquals(ChallengeType.FIBONACCI, result.getChallengeType());
        assertEquals(10, result.getScore());
        verify(challengeResultRepository, times(1)).save(any(ChallengeResult.class));
    }

    @Test
    void registerScore_WithInvalidPlayerId_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () ->
            challengeService.registerScore("invalid-id", validTournamentId, ChallengeType.FIBONACCI));
    }

    @Test
    void registerScore_WithInvalidTournamentId_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () ->
            challengeService.registerScore(validPlayerId, "invalid-id", ChallengeType.FIBONACCI));
    }

    @Test
    void registerScore_WithNonExistentPlayer_ShouldThrowResourceNotFoundException() {
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            challengeService.registerScore(validPlayerId, validTournamentId, ChallengeType.FIBONACCI));
    }

    @Test
    void registerScore_WithNonExistentTournament_ShouldThrowResourceNotFoundException() {
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(mockPlayer));
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            challengeService.registerScore(validPlayerId, validTournamentId, ChallengeType.FIBONACCI));
    }

    @Test
    void registerScore_WithUnknownChallengeType_ShouldUseDefaultScore() {
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(mockPlayer));
        when(tournamentRepository.findById(UUID.fromString(validTournamentId))).thenReturn(Optional.of(mockTournament));
        when(challengeResultRepository.save(any(ChallengeResult.class))).thenAnswer(i -> i.getArgument(0));

        ChallengeResult result = challengeService.registerScore(validPlayerId, validTournamentId, ChallengeType.FIBONACCI);

        assertNotNull(result);
        assertEquals(10, result.getScore());
    }
}
