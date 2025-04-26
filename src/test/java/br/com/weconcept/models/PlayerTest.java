package br.com.weconcept.models;

import org.junit.jupiter.api.Test;

import br.com.weconcept.business.usescases.player.models.Player;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayerGettersAndSetters() {
        Player player = new Player();
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        int age = 25;
        LocalDateTime birthday = LocalDateTime.now();

        player.setId(id);
        player.setName(name);
        player.setAge(age);

        assertEquals(id, player.getId());
        assertEquals(name, player.getName());
        assertEquals(age, player.getAge());
    }

    @Test
    void testPlayerAgeValidation() {
        Player player = new Player();
        assertDoesNotThrow(() -> player.setAge(16));
        assertDoesNotThrow(() -> player.setAge(100));
    }
}
