package br.com.weconcept.business.tournament.services;

import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.repositories.PlayerRepository;
import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.repositories.TournamentRepository;
import br.com.weconcept.utils.UUIDValidator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    private final PlayerRepository playerRepository;

    public TournamentService(
            TournamentRepository tournamentRepository,
            PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public Tournament createTournament(Tournament tournament) {
        if (tournament == null) {
            throw new ValidationException("Preencha corretamente as informações do torneio. ");
        }
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> fetchTournaments() {
        return this.tournamentRepository.findAll();
    }

    public Tournament fetchTournamentById(String tournamentId) {
        log.info("Procurando torneio pelo Id: " + tournamentId);
        if (tournamentId == null || tournamentId.isEmpty() || !UUIDValidator.isValidUUID(tournamentId)) {
            throw new ValidationException("tournamentId inválido.");
        }
        return tournamentRepository.findById(UUID.fromString(tournamentId))
            .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar o torneio."));
    }

    public Tournament addPlayer(String tournamentId, String playerId) {
        var tournament = this.fetchTournamentById(tournamentId);
        var player = this.fetchPlayerById(playerId);
        tournament.getPlayers().add(player);
        return tournamentRepository.save(tournament);
    }

    public Tournament removePlayer(String tournamentId, String playerId) {
        var tournament = this.fetchTournamentById(tournamentId);
        var player = this.fetchPlayerById(playerId);
        tournament.getPlayers().remove(player);
        return tournamentRepository.save(tournament);
    }

    public Set<Player> listPlayers(String tournamentId) {
        Tournament tournament = this.fetchTournamentById(tournamentId);
        return tournament.getPlayers();
    }

    public Tournament finishTournament(String tournamentId) {
        Tournament tournament = this.fetchTournamentById(tournamentId);
        tournament.setFinished(true);
        return tournamentRepository.save(tournament);
    }

    private Player fetchPlayerById(String playerId) {
        if (playerId == null || playerId.isEmpty() || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("playerId inválido.");
        }
        return this.playerRepository.findById(UUID.fromString(playerId))
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar o jogador(a)."));
    }
}
