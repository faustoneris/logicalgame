package br.com.weconcept.business.player.controller;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.models.PlayerModel;
import br.com.weconcept.business.player.services.PlayerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> fetchAllPlayers() {
        return ResponseEntity.ok(this.playerService.fetchAllPlayers());
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerModel> fetchPlayerById(@PathVariable String playerId) {
        return ResponseEntity.ok(this.playerService.fetchPlayerById(playerId));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<PlayerModel> fetchPlayersByName(@PathVariable String name) {
        return ResponseEntity.ok(playerService.fetchPlayerByName(name));
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.playerService.createPlayer(player));
    }

    @PutMapping("{playerId}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String playerId, @RequestBody Player player) {
        return ResponseEntity.ok(this.playerService.updatePlayer(playerId, player));
    }

    @DeleteMapping("{playerId}")
    public ResponseEntity<String> deletePlayerById(@PathVariable String playerId) {
        this.playerService.deletePlayer(playerId);
        return ResponseEntity.noContent().build();
    }
}
