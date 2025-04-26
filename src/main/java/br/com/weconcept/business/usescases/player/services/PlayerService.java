package br.com.weconcept.business.usescases.player.services;

import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.usescases.player.models.Player;
import br.com.weconcept.business.usescases.player.models.PlayerModel;
import br.com.weconcept.business.usescases.player.repositories.PlayerRepository;
import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.utils.UUIDValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Slf4j
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
        log.info("Validando idade do jogador(a) - {} - {}", player.getName(), player.getAge());
        if (player.getAge() <= 0) {
            throw new ValidationException("Idade do jogador(a) não pode ser menor ou igual a 0.");
        } else if (player.getAge() <= 15) {
            throw new ValidationException("Jogador(a) precisa ter ao menos 16 anos para participar.");
        } else {
            log.info("Criando jogador(a) - {} - {}", player.getName(), player.getAge());
            return this.playerRepository.save(player);
        }
    }

    public PlayerModel fetchPlayerById(String playerId) {
        log.info("Validando playerId do(a) Jogador(a) - {}", playerId);
        if (playerId == null || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("PlayerId inválido.");
        }
        log.info("Buscando Jogador(a) pelo PlayerId - {}", playerId);
        Player player = playerRepository.findById(UUID.fromString(playerId))
            .orElseThrow(() -> new ResourceNotFoundException("Jogador(a) não encontrado(a) com o id: " + playerId));;
        return PlayerModel.of(player);
    }

    public PlayerModel fetchPlayerByName(String name) {
        log.info("Validando nome do(a) Jogador(a) - {}", name);
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Nome do jogador(a) inválido.");
        }
        log.info("Buscando pelo nome do(a) Jogador(a) - {}", name);
        var player = playerRepository.findByName(name);
        if (player == null) {
            throw new ValidationException("Não foi possível encontrar um jogador com esse nome: " + name);
        }
        return PlayerModel.of(player);
    }

    public Player updatePlayer(String playerId, Player player) {
        log.info("Validando playerId do(a) Jogador(a) - {}", playerId);
        if (playerId == null || playerId.isEmpty() || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("playerId inválido.");
        }
        log.info("Validando payload do(a) Jogador(a) - {}", playerId);
        if (player == null) {
            throw new ValidationException("Preencha as informações do Jogador(a) corretamente.");
        }
        log.info("Efetuando update com base no payload do(a) Jogador(a) - {}", playerId);
        return playerRepository.findById(UUID.fromString(playerId))
            .map(p -> {
                p.setAge(player.getAge());
                p.setName(player.getName());
                return playerRepository.save(p);
        }).orElseThrow(() -> new ResourceNotFoundException("Não foi possivel localizar o jogador por este ID: " + playerId));
    }

    public void deletePlayer(String playerId) {
        if (playerId == null || playerId.isEmpty() || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("playerId inválido.");
        }
        log.info("Removendo Jogador(a) - {}", playerId);
        playerRepository.deleteById(UUID.fromString(playerId));
    }
}
