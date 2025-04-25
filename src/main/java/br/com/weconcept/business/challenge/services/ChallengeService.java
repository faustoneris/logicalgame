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

    public int calculateFibonacci(int indice) {
        if (indice <= 1) {
            return indice;
        }

        int numberBefore = 0, actualNumber = 1;

        for (int i = 2; i <= indice; i++) {
            actualNumber = numberBefore + actualNumber;
            numberBefore = actualNumber - numberBefore;
        }

        return actualNumber;
    }

    public boolean isPalindrome(String texto) {
        String cleanText = texto.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String reverseText = new StringBuilder(cleanText).reverse().toString();
        return cleanText.equals(reverseText);
    }

    public int[] customSort(int[] input) {
        boolean swapped;
        for (int i = 0; i < input.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < input.length - i - 1; j++) {
                if (input[j] > input[j + 1]) {
                    int temp = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
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
