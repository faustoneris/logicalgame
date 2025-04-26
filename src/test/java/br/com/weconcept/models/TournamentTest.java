package br.com.weconcept.models;

import br.com.weconcept.business.usescases.player.models.Player;
import br.com.weconcept.business.usescases.tournament.models.Tournament;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    @Test
    void testTournamentGettersAndSetters() {
        Tournament tournament = new Tournament();
        UUID id = UUID.randomUUID();
        String name = "Champions League";
        LocalDate date = LocalDate.now();
        boolean finished = true;

        tournament.setId(id);
        tournament.setName(name);
        tournament.setFinished(finished);
        tournament.setDate(date);

        assertEquals(id, tournament.getId());
        assertEquals(date, tournament.getDate());
        assertEquals(name, tournament.getName());
        assertEquals(finished, tournament.hasFinished());
    }

    @Test
    void testPlayersManagement() {
        Tournament tournament = new Tournament();
        Player player1 = new Player();
        player1.setId(UUID.randomUUID());
        Player player2 = new Player();
        player2.setId(UUID.randomUUID());

        Set<Player> players = new HashSet<>();
        players.add(player1);
        players.add(player2);

        tournament.setPlayers(players);

        assertEquals(2, tournament.getPlayers().size());
        assertTrue(tournament.getPlayers().contains(player1));
        assertTrue(tournament.getPlayers().contains(player2));
    }

    @Test
    void testAddAndRemovePlayers() {
        Tournament tournament = new Tournament();
        Player player = new Player();
        player.setId(UUID.randomUUID());

        Set<Player> players = new HashSet<>();
        players.add(player);
        tournament.setPlayers(players);

        assertEquals(1, tournament.getPlayers().size());

        tournament.getPlayers().remove(player);
        assertEquals(0, tournament.getPlayers().size());
    }
}
