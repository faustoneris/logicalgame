package br.com.weconcept.business.player.services;

import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.models.PlayerModel;
import br.com.weconcept.business.player.repositories.PlayerRepository;
import br.com.weconcept.utils.UUIDValidator;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> fetchAllPlayers() {
        return this.playerRepository.findAll();
    }

    public Player createPlayer(Player player) {
        if (player.getAge() <= 0) {
            throw new ValidationException("Idade do jogador(a) não pode ser menor ou igual a 0.");
        } else if (player.getAge() <= 15) {
            throw new ValidationException("Jogador(a) precisa ter ao menos 16 anos para participar.");
        } else {
            return this.playerRepository.save(player);
        }
    }

    public PlayerModel fetchPlayerById(String playerId) {
        if (playerId == null || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("PlayerId inválido.");
        }
        Player player = playerRepository.findById(UUID.fromString(playerId))
            .orElseThrow(() -> new ResourceNotFoundException("Jogador(a) não encontrado(a) com o id: " + playerId));;
        return PlayerModel.of(player);
    }

    public PlayerModel fetchPlayerByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Nome do jogador(a) inválido.");
        }
        var player = playerRepository.findByName(name);
        if (player == null) {
            throw new ValidationException("Não foi possível encontrar um jogador com esse nome: " + name);
        }
        return PlayerModel.of(player);
    }

    public Player updatePlayer(String playerId, Player player) {
        return playerRepository.findById(UUID.fromString(playerId))
            .map(p -> {
                p.setAge(player.getAge());
                p.setName(player.getName());
                p.setBirthdayDate(player.getBirthdayDate());
                return playerRepository.save(p);
        }).orElseThrow(() -> new ResourceNotFoundException("Não foi possivel localizar o jogador por este ID: " + playerId));
    }

    public void deletePlayer(UUID id) {
        playerRepository.deleteById(id);
    }
}
