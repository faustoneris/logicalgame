package br.com.weconcept.business.player.services;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.repositories.PlayerRepository;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Optional<Player> fetchPlayerById(UUID id) {
        return playerRepository.findById(id);
    }

    public List<Player> fetchPlayersByName(String name) {
        return playerRepository.findByNameContainingIgnoreCase(name);
    }

    public Player updatePlayer(UUID id, String name) {
        return playerRepository.findById(id).map(player -> {
            player.setName(name);
            return playerRepository.save(player);
        }).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    public void deletePlayer(UUID id) {
        playerRepository.deleteById(id);
    }
}
