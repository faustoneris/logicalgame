package br.com.weconcept.business.player.repositories;

import br.com.weconcept.business.player.models.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    List<Player> findByNameContainingIgnoreCase(String name);
}
