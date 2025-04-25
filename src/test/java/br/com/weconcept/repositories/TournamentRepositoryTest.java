package br.com.weconcept.repositories;

import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.repositories.TournamentRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TournamentRepositoryTest {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Test
    void testSaveAndFindTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");

        Tournament saved = tournamentRepository.save(tournament);
        assertNotNull(saved.getId());

        Tournament found = tournamentRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test Tournament", found.getName());
    }

    @Test
    void testFindNonExistentTournament() {
        assertFalse(tournamentRepository.findById(UUID.randomUUID()).isPresent());
    }
}
