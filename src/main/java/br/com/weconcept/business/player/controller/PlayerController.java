package br.com.weconcept.business.player.controller;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.services.PlayerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return ResponseEntity.ok(playerService.createPlayer(player));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> fetchPlayerById(@PathVariable UUID id) {
        Optional<Player> player = playerService.fetchPlayerById(id);
        return player.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Player>> fetchPlayersByName(@RequestParam String name) {
        return ResponseEntity.ok(playerService.fetchPlayersByName(name));
    }
}
