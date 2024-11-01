package br.com.guessApi.service.implementations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.guessApi.entitys.CardEntity;
import br.com.guessApi.entitys.dtos.CardUpdateObject;
import br.com.guessApi.enums.LevelType;
import br.com.guessApi.exceptions.CardException;
import br.com.guessApi.repository.CardRepository;
import br.com.guessApi.service.CardService;

@Service
public class CardServiceImpl implements CardService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CardServiceImpl.class);
	
	private CardRepository cardRepository;
	
	public CardServiceImpl(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public void store(CardUpdateObject newCard) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando salvar novo card");
		try {
			CardEntity card = new CardEntity(null, UUID.randomUUID(), newCard.question(), newCard.photo(), newCard.level());
			this.cardRepository.save(card);
			LOGGER.info("[CARDSERVICEIMPL]: CARD SALVO COM SUCESSO! E COM IDENTIFIER: " + card.getIdentifier());
		} catch (Exception e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO AO PERSISITR CARD");
			throw new CardException(e.getMessage());
		}

	}

	@Override
	public void update(UUID identifier, CardUpdateObject newCard) throws CardException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(UUID identifier) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando salvar novo card");
		try {
			CardEntity findByIdentifier = this.cardRepository.findByIdentifier(identifier).orElseThrow();
			this.cardRepository.delete(findByIdentifier);
			LOGGER.info("[CARDSERVICEIMPL]: SUCESSO AO DELETAR CARD COM IDENTIFIER: " + identifier);
		} catch (Exception e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO AO DELETAR CARD COM IDENTIFIER: " + identifier);
			throw new CardException(e.getMessage());
		}

	}

	@Override
	public List<CardEntity> getAll() throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando listar todos card");
		
		try {
			 return this.cardRepository.findAll();
			
		} catch (Exception e) {
			LOGGER.error("[CARDSERVICEIMPL]: erro ao retornar cards");
			throw new CardException( e.getMessage());
		}
	}

	@Override
	public CardEntity getByUUID(UUID identifier) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando salvar novo card");
		try {
			CardEntity findByIdentifier = this.cardRepository.findByIdentifier(identifier).orElseThrow();
			
			LOGGER.info("[CARDSERVICEIMPL]: SUCESSO AO DELETAR CARD COM IDENTIFIER: " + identifier);
			return findByIdentifier;
		} catch (Exception e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO AO DELETAR CARD COM IDENTIFIER: " + identifier);
			throw new CardException(e.getMessage());
		}

	}

	@Override
	public List<CardEntity> getByLevel(LevelType level) throws CardException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CardEntity> getRandoByLimit(int limit) throws CardException {
		// TODO Auto-generated method stub
		return null;
	}

}
