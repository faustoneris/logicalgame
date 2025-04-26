package br.com.weconcept.services;

import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.usescases.player.models.Player;
import br.com.weconcept.business.usescases.player.models.PlayerModel;
import br.com.weconcept.business.usescases.player.repositories.PlayerRepository;
import br.com.weconcept.business.usescases.player.services.PlayerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player validPlayer;
    private String validPlayerId;
    private String invalidPlayerId;

    @BeforeEach
    void setUp() {
        validPlayerId = UUID.randomUUID().toString();
        invalidPlayerId = "invalid-id";

        validPlayer = new Player();
        validPlayer.setId(UUID.fromString(validPlayerId));
        validPlayer.setName("John Doe");
        validPlayer.setAge(20);
    }

    @Test
    void fetchAllPlayers_ShouldReturnAllPlayers() {
        // Arrange
        List<Player> expectedPlayers = List.of(new Player(), new Player());
        when(playerRepository.findAll()).thenReturn(expectedPlayers);

        // Act
        List<Player> result = playerService.fetchAllPlayers();

        // Assert
        assertEquals(expectedPlayers, result);
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void createPlayer_WithValidAge_ShouldReturnSavedPlayer() {
        // Arrange
        Player playerToSave = new Player();
        playerToSave.setAge(16);
        when(playerRepository.save(playerToSave)).thenReturn(playerToSave);

        // Act
        Player result = playerService.createPlayer(playerToSave);

        // Assert
        assertEquals(playerToSave, result);
        verify(playerRepository, times(1)).save(playerToSave);
    }

    @Test
    void createPlayer_WithAgeZero_ShouldThrowValidationException() {
        // Arrange
        Player invalidPlayer = new Player();
        invalidPlayer.setAge(0);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> playerService.createPlayer(invalidPlayer));

        assertEquals("Idade do jogador(a) não pode ser menor ou igual a 0.", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void createPlayer_WithAge15_ShouldThrowValidationException() {
        // Arrange
        Player invalidPlayer = new Player();
        invalidPlayer.setAge(15);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> playerService.createPlayer(invalidPlayer));

        assertEquals("Jogador(a) precisa ter ao menos 16 anos para participar.", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void fetchPlayerById_WithValidId_ShouldReturnPlayerModel() {
        // Arrange
        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(validPlayer));

        // Act
        PlayerModel result = playerService.fetchPlayerById(validPlayerId);

        // Assert
        assertNotNull(result);
        assertEquals(validPlayer.getName(), result.getName());
        assertEquals(validPlayer.getAge(), result.getAge());
        verify(playerRepository, times(1)).findById(UUID.fromString(validPlayerId));
    }

    @Test
    void fetchPlayerById_WithInvalidId_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> playerService.fetchPlayerById(invalidPlayerId));

        assertEquals("PlayerId inválido.", exception.getMessage());
        verify(playerRepository, never()).findById(any());
    }

    @Test
    void fetchPlayerById_WithNonExistentId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(playerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> playerService.fetchPlayerById(validPlayerId));

        assertEquals("Jogador(a) não encontrado(a) com o id: " + validPlayerId, exception.getMessage());
        verify(playerRepository, times(1)).findById(UUID.fromString(validPlayerId));
    }

    @Test
    void fetchPlayerByName_WithValidName_ShouldReturnPlayerModel() {
        // Arrange
        String playerName = "John Doe";
        when(playerRepository.findByName(playerName)).thenReturn(validPlayer);

        // Act
        PlayerModel result = playerService.fetchPlayerByName(playerName);

        // Assert
        assertNotNull(result);
        assertEquals(playerName, result.getName());
        verify(playerRepository, times(1)).findByName(playerName);
    }

    @Test
    void fetchPlayerByName_WithEmptyName_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> playerService.fetchPlayerByName(""));

        assertEquals("Nome do jogador(a) inválido.", exception.getMessage());
        verify(playerRepository, never()).findByName(any());
    }

    @Test
    void fetchPlayerByName_WithNullName_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> playerService.fetchPlayerByName(null));

        assertEquals("Nome do jogador(a) inválido.", exception.getMessage());
        verify(playerRepository, never()).findByName(any());
    }

    @Test
    void fetchPlayerByName_WithNonExistentName_ShouldThrowValidationException() {
        // Arrange
        String nonExistentName = "Non Existent";
        when(playerRepository.findByName(nonExistentName)).thenReturn(null);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> playerService.fetchPlayerByName(nonExistentName));

        assertEquals("Não foi possível encontrar um jogador com esse nome: " + nonExistentName, exception.getMessage());
        verify(playerRepository, times(1)).findByName(nonExistentName);
    }

    @Test
    void updatePlayer_WithValidId_ShouldReturnUpdatedPlayer() {
        // Arrange
        Player updatedData = new Player();
        updatedData.setName("Updated Name");
        updatedData.setAge(25);

        when(playerRepository.findById(UUID.fromString(validPlayerId))).thenReturn(Optional.of(validPlayer));
        when(playerRepository.save(validPlayer)).thenReturn(validPlayer);

        // Act
        Player result = playerService.updatePlayer(validPlayerId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals(updatedData.getName(), validPlayer.getName());
        assertEquals(updatedData.getAge(), validPlayer.getAge());
        verify(playerRepository, times(1)).findById(UUID.fromString(validPlayerId));
        verify(playerRepository, times(1)).save(validPlayer);
    }

    @Test
    void updatePlayer_WithNonExistentId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(playerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> playerService.updatePlayer(validPlayerId, new Player()));

        assertEquals("Não foi possivel localizar o jogador por este ID: " + validPlayerId, exception.getMessage());
        verify(playerRepository, times(1)).findById(UUID.fromString(validPlayerId));
        verify(playerRepository, never()).save(any());
    }

    @Test
    void deletePlayer_ShouldCallRepositoryDelete() {
        // Arrange
        String playerUuid = validPlayerId;

        // Act
        playerService.deletePlayer(playerUuid);

        // Assert
        verify(playerRepository, times(1)).deleteById(UUID.fromString(playerUuid));
    }
}
