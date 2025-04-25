package br.com.weconcept.business.tournament.services;

import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import br.com.weconcept.business.player.models.Player;
import br.com.weconcept.business.player.repositories.PlayerRepository;
import br.com.weconcept.business.player.services.PlayerService;
import br.com.weconcept.business.tournament.models.Tournament;
import br.com.weconcept.business.tournament.repositories.TournamentRepository;
import br.com.weconcept.utils.UUIDValidator;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    public TournamentService(
            TournamentRepository tournamentRepository,
            PlayerRepository playerRepository,
            PlayerService playerService) {
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerService = playerService;
    }

    public Tournament createTournament(Tournament tournament) {
        if (tournament == null) {
            throw new ValidationException("Preencha corretamente as informações do torneio. ");
        }
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> fetchTournaments() {
        return this.tournamentRepository.findAll();
    }

    public Tournament fetchTournamentById(String tournamentId) {
        log.info("Validando torneio pelo tournamentId: {}", tournamentId);
        if (tournamentId == null || tournamentId.isEmpty() || !UUIDValidator.isValidUUID(tournamentId)) {
            throw new ValidationException("tournamentId inválido.");
        }
        log.info("Buscando torneio pelo tournamentId: {}", tournamentId);
        return tournamentRepository.findById(UUID.fromString(tournamentId))
            .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar o torneio."));
    }

    public Tournament addPlayer(String tournamentId, String playerId) {
        var tournament = this.fetchTournamentById(tournamentId);
        var player = this.fetchPlayerById(playerId);
        tournament.getPlayers().add(player);
        log.info("Jogador(a)s adicionados {}", tournament.getPlayers().size());
        return tournamentRepository.save(tournament);
    }

    public Tournament addPlayerFromExcel(String tournamentId, Player player) {
        var tournament = this.fetchTournamentById(tournamentId);
        tournament.getPlayers().add(player);
        log.info("Jogador(a)s adicionados {}", tournament.getPlayers().size());
        return tournamentRepository.save(tournament);
    }

    public Tournament removePlayer(String tournamentId, String playerId) {
        var tournament = this.fetchTournamentById(tournamentId);
        var player = this.fetchPlayerById(playerId);
        tournament.getPlayers().remove(player);
        return tournamentRepository.save(tournament);
    }

    public Set<Player> listTournamentsPlayers(String tournamentId) {
        Tournament tournament = this.fetchTournamentById(tournamentId);
        return tournament.getPlayers();
    }

    public Tournament finishTournament(String tournamentId) {
        log.info("Finalizando torneio {}", tournamentId);
        Tournament tournament = this.fetchTournamentById(tournamentId);
        tournament.setFinished(true);
        log.info("Torneio finalizado {}", tournament.hasFinished());
        return tournamentRepository.save(tournament);
    }

    public List<Player> extractPlayersFromExcel(MultipartFile file, String tournamentId) throws IOException {
        List<Player> players = new ArrayList<>();

        log.info("Iniciando leitura do excel {} - {}", file.getSize(), tournamentId);
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            var sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Player player = new Player();

                player.setName(getStringValue(currentRow.getCell(0)));
                player.setAge((int) currentRow.getCell(1).getNumericCellValue());

                log.info("Criando Jogador(a) {} - {}", player.getName(), tournamentId);
                var playerCreated = this.playerService.createPlayer(player);
                this.addPlayerFromExcel(tournamentId, playerCreated);
                log.info("Jogador(a) criado(a) {} - {}", playerCreated.getId(), tournamentId);
                players.add(player);
            }

            workbook.close();
        }

        return players;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private Player fetchPlayerById(String playerId) {
        log.info("Validando Jogador(a) pelo playerId: {}", playerId);
        if (playerId == null || playerId.isEmpty() || !UUIDValidator.isValidUUID(playerId)) {
            throw new ValidationException("playerId inválido.");
        }
        log.info("Buscando Jogador(a) pelo playerId: {}", playerId);
        return this.playerRepository.findById(UUID.fromString(playerId))
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar o jogador(a)."));
    }
}
