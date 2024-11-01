package br.com.guessApi.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guessApi.entitys.CardEntity;
import br.com.guessApi.enums.LevelType;

public interface CardRepository extends JpaRepository<CardEntity, Integer>{
	

	Optional<List<CardEntity>> findListByLevel (LevelType level);
	
	Optional<CardEntity> findByIdentifier(UUID identifier);
	
}
