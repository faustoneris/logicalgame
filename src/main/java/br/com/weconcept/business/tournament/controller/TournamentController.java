package br.com.weconcept.business.tournament.controller;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.services.TournamentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/{tournamentId}/players")
    public ResponseEntity<Set<Player>> listPlayers(@PathVariable String tournamentId) {
        return ResponseEntity.ok(tournamentService.listPlayers(tournamentId));
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

    @PutMapping("/{id}/finish")
    public ResponseEntity<Tournament> finishTournament(@PathVariable String id) {
        return ResponseEntity.ok(tournamentService.finishTournament(id));
    }

    @PutMapping("{tournamentId}/players")
    public ResponseEntity<Tournament> addPlayer(@PathVariable String tournamentId, @RequestParam String playerId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.tournamentService.addPlayer(tournamentId, playerId));
    }

    @DeleteMapping("{tournamentId}/players/{playerId}")
    public ResponseEntity<Tournament> removePlayer(@PathVariable String tournamentId, @PathVariable String playerId) {
        return ResponseEntity.ok(tournamentService.removePlayer(tournamentId, playerId));
    }
}
