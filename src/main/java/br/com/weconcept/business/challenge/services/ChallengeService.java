package br.com.weconcept.business.challenge.services;

import br.com.weconcept.business.challenge.models.ChallengeResult;
import br.com.weconcept.business.challenge.models.ChallengeType;
import br.com.weconcept.business.challenge.repositories.ChallengeRepository;
import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.player.repositories.PlayerRepository;
import br.com.weconcept.business.tournament.repositories.TournamentRepository;
import br.com.weconcept.utils.UUIDValidator;


import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeResultRepository;

    private final PlayerRepository playerRepository;

    private final TournamentRepository tournamentRepository;

    private final Map<ChallengeType, Integer> challengeWeights = Map.of(
            ChallengeType.PALINDROME, 5,
            ChallengeType.FIBONACCI, 10,
            ChallengeType.SORTING, 7);

    public ChallengeService(
            ChallengeRepository challengeResultRepository,
            PlayerRepository playerRepository,
            TournamentRepository tournamentRepository) {
        this.challengeResultRepository = challengeResultRepository;
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public int executeFibonacci(int n) {
        if (n <= 1)
            return n;
        int a = 0, b = 1, result = 1;
        for (int i = 2; i <= n; i++) {
            result = a + b;
            a = b;
            b = result;
        }
        return result;
    }

    public boolean isPalindrome(String input) {
        String cleaned = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        return new StringBuilder(cleaned)
                .reverse()
                .toString()
                .equals(cleaned);
    }

    public int[] customSort(int[] input) {
        for (int i = 0; i < input.length - 1; i++) {
            for (int j = 0; j < input.length - i - 1; j++) {
                if (input[j] > input[j + 1]) {
                    int temp = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = temp;
                }
            }
        }
        return input;
    }

    public ChallengeResult registerScore(String playerId, String tournamentId, ChallengeType type) {
        if (playerId == null || tournamentId == null || !UUIDValidator.isValidUUID(tournamentId) || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("PlayerId e/ou TournamentId inválidos.");
        }
        var player = playerRepository.findById(UUID.fromString(playerId))
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar o(a) jogador(a)."));
        var tournament = tournamentRepository.findById(UUID.fromString(tournamentId))
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar o torneio."));

        int score = challengeWeights.getOrDefault(type, 0);
        return challengeResultRepository.save(ChallengeResult.ofResult(player, tournament, type, score));
    }
}
