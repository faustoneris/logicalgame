package br.com.weconcept.models;

import org.junit.jupiter.api.Test;

import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.models.PlayerModel;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {

    @Test
    void testPlayerModelCreation() {
        Player player = new Player();
        UUID id = UUID.randomUUID();
        String name = "Jane Smith";
        int age = 30;

        player.setId(id);
        player.setName(name);
        player.setAge(age);

        PlayerModel model = PlayerModel.of(player);

        assertEquals(id, model.getId());
        assertEquals(name, model.getName());
        assertEquals(age, model.getAge());
    }

    @Test
    void testEmptyConstructor() {
        PlayerModel model = new PlayerModel();
        assertNull(model.getId());
        assertNull(model.getName());
        assertEquals(0, model.getAge());
        assertNull(model.getBirthdayDate());
    }
}
