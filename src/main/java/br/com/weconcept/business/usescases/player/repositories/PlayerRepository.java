package br.com.weconcept.business.usescases.player.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.weconcept.business.usescases.player.models.Player;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Player findByName(String name);
}
