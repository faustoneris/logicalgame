package br.com.weconcept.business.player.repositories;

import br.com.weconcept.business.player.models.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Player findByName(String name);
}
