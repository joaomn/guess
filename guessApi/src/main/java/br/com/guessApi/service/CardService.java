package br.com.guessApi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.guessApi.entitys.CardEntity;
import br.com.guessApi.entitys.dtos.CardUpdateObject;
import br.com.guessApi.enums.LevelType;
import br.com.guessApi.exceptions.CardException;

public interface CardService {

public void store(CardUpdateObject newCard)	throws CardException;

public void update(UUID identifier, CardUpdateObject newCard) throws CardException;

public void delete (UUID identifier) throws CardException;

public List<CardEntity> getAll() throws CardException;

public CardEntity getByUUID(UUID identifier)throws CardException;

public List<CardEntity> getByLevel(LevelType level) throws CardException;

public List<CardEntity> getRandoByLimit(int limit) throws CardException;
	


}
