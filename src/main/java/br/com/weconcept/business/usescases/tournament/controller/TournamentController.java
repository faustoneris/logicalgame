package br.com.weconcept.business.usescases.tournament.controller;

import br.com.weconcept.business.usescases.player.models.Player;
import br.com.weconcept.business.usescases.tournament.models.Tournament;
import br.com.weconcept.business.usescases.tournament.services.TournamentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("{tournamentId}/players")
    public ResponseEntity<Set<Player>> listTournamentsPlayers(@PathVariable String tournamentId) {
        return ResponseEntity.ok(tournamentService.listTournamentsPlayers(tournamentId));
    }

    @GetMapping("{tournamentId}")
    public ResponseEntity<Tournament> fetchTournamentById(@PathVariable String tournamentId) {
        return ResponseEntity.ok(this.tournamentService.fetchTournamentById(tournamentId));
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> fetchTournaments() {
        return ResponseEntity.ok(this.tournamentService.fetchTournaments());
    }

    @PostMapping
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.tournamentService.createTournament(tournament));
    }

    @PostMapping("{tournamentId}/upload")
    public ResponseEntity<String> extractPlayersFromExcel(@PathVariable String tournamentId, @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, envie um arquivo Excel.");
        }
        try {
            var players = this.tournamentService.extractPlayersFromExcel(file, tournamentId);
            return ResponseEntity.ok("Arquivo processado com sucesso. Jogadores inseridos: " + players.size());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Falha ao processar o arquivo: " + e.getMessage());
        }
    }


    @PutMapping("/{tournamentId}/finish")
    public ResponseEntity<Tournament> finishTournament(@PathVariable String tournamentId) {
        return ResponseEntity.ok(tournamentService.finishTournament(tournamentId));
    }

    @PutMapping("{tournamentId}/players/{playerId}")
    public ResponseEntity<Tournament> addPlayer(@PathVariable String tournamentId, @PathVariable String playerId) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.tournamentService.addPlayer(tournamentId, playerId));
    }

    @DeleteMapping("{tournamentId}/players/{playerId}")
    public ResponseEntity<Tournament> removePlayer(@PathVariable String tournamentId, @PathVariable String playerId) {
        return ResponseEntity.ok(tournamentService.removePlayer(tournamentId, playerId));
    }
}
