package br.com.weconcept.business.tournament.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.weconcept.business.tournament.models.Tournament;

import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {}
