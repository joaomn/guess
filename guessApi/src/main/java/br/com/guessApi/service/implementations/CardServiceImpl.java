package br.com.guessApi.service.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.guessApi.entitys.CardEntity;
import br.com.guessApi.entitys.dtos.CardUpdateObject;
import br.com.guessApi.enums.LevelType;
import br.com.guessApi.exceptions.CardException;
import br.com.guessApi.exceptions.OutLimitCardsException;
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
	public CardEntity store(CardUpdateObject newCard) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando salvar novo card");
		try {
			CardEntity card = new CardEntity(null, UUID.randomUUID().toString(), newCard.question(), newCard.photo(), newCard.level());
			 this.cardRepository.save(card);
			 LOGGER.info("[CARDSERVICEIMPL]: CARD SALVO COM SUCESSO! E COM IDENTIFIER: " + card.getIdentifier());
			 return card;
		} catch (RuntimeException e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO AO PERSISITR CARD");
			throw new CardException("ERRO AO SALVAR CARD");
		}

	}

	@Override
	public void update(String identifier, CardUpdateObject newCard) throws CardException {

		  LOGGER.info("[CARDSERVICEIMPL]: Iniciando a atualização do card com identifier: " + identifier);

		    try {
		        CardEntity existingCard = cardRepository.findByIdentifier(identifier)
		            .orElseThrow(() -> new CardException("Card com identifier " + identifier + " não encontrado."));

		        existingCard.setQuestion(
		            (newCard.question() != null && !newCard.question().isBlank()) ? newCard.question() : existingCard.getQuestion()
		        );
		        
		        existingCard.setPhoto(
		            (newCard.photo() != null && !newCard.photo().isBlank()) ? newCard.photo() : existingCard.getPhoto()
		        );
		        
		        existingCard.setLevel(
		            (newCard.level() != null) ? newCard.level() : existingCard.getLevel()
		        );

		        cardRepository.save(existingCard);

		        LOGGER.info("[CARDSERVICEIMPL]: Sucesso na atualização do card com identifier: " + identifier);
		    } catch (RuntimeException e) {
		        LOGGER.error("[CARDSERVICEIMPL]: Erro na atualização do card com identifier: " + identifier, e);
		        throw new CardException("Erro ao atualizar o card");
		    }
	}

	@Override
	public void delete(String identifier) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando salvar novo card");
		try {
			CardEntity findByIdentifier = this.cardRepository.findByIdentifier(identifier)
					.orElseThrow(() -> new CardException("Card com identifier " + identifier + " não encontrado."));
			this.cardRepository.delete(findByIdentifier);
			LOGGER.info("[CARDSERVICEIMPL]: SUCESSO AO DELETAR CARD COM IDENTIFIER: " + identifier);
		} catch (RuntimeException e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO AO DELETAR CARD COM IDENTIFIER: " + identifier);
			throw new CardException("ERRO AO DELETAR O CARD");
		}

	}

	@Override
	public List<CardEntity> getAll() throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Tentando listar todos card");
		
		try {
			 return this.cardRepository.findAll();
			
		} catch (RuntimeException e) {
			LOGGER.error("[CARDSERVICEIMPL]: erro ao retornar cards");
			throw new CardException(" ERRO AO LISTAR TODOS OS CARDS");
		}
	}

	@Override
	public CardEntity getByUUID(String identifier) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Iniciando a busca do card de identificador : " + identifier);
		try {
			CardEntity findByIdentifier = this.cardRepository.findByIdentifier(identifier)
					.orElseThrow(() -> new CardException("Card com identifier " + identifier + " não encontrado."));
			
			LOGGER.info("[CARDSERVICEIMPL]: SUCESSO NA BUSCA DO CARD COM IDENTIFIER: " + identifier);
			return findByIdentifier;
		} catch (RuntimeException e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO AO BUSCAR CARD COM IDENTIFIER: " + identifier);
			throw new CardException("ERRO AO RETORNAR CARD");
		}

	}

	@Override
	public List<CardEntity> getByLevel(LevelType level) throws CardException {
		LOGGER.info("[CARDSERVICEIMPL]: Iniciando a busca dos cards de level : " + level.name());
		try {
			List<CardEntity> findByLevel = this.cardRepository.findListByLevel(level)
					.orElseThrow(() -> new CardException("Card com level " + level.name() + " não encontrado."));
			
			LOGGER.info("[CARDSERVICEIMPL]: SUCESSO NA BUSCA DOS CARDS COM LEVEL: " + level.name());
			return findByLevel;
		} catch (RuntimeException e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO NA BUSCA DOS CARDS COM LEVEL: " + level.name());
			throw new CardException("ERRO NA OPERAÇÃO GETBYLEVEL");
		}
	}

	@Override
	public List<CardEntity> getRandoByLimit(int limit) throws CardException {
		 LOGGER.info("[CARDSERVICEIMPL]: Iniciando a busca de cards com limite de: " + limit);
		 
			List<CardEntity> allCards = this.cardRepository.findAll();
			if (allCards.size() < limit) {
				LOGGER.error("[CARDSERVICEIMPL]: Desculpe, nosso baralho só vai até {} cartas ", allCards.size());
				throw new OutLimitCardsException("Numero pedido de cartas superou o nosso deck que tem um total de " + allCards.size() + " cartas");
			}
		    try {
		        Collections.shuffle(allCards); 
		        return allCards.stream().limit(limit).collect(Collectors.toList()); 
		    } catch (RuntimeException e) {
		        LOGGER.error("[CARDSERVICEIMPL]: Erro ao buscar cards com limite de: " + limit, e);
		        throw new CardException("ERRO NA OPERAÇÃO GETBYLIMIT");
		    }
	}

	@Override
	public Integer getCountDeck() throws OutLimitCardsException {
		LOGGER.info("[CARDSERVICEIMPL]: Iniciando a contagem das cartas existentes no deck" );
		try {
			
			
			List<CardEntity> allCards = this.cardRepository.findAll();
			
			Integer total = allCards.size();
					
			
			LOGGER.info("[CARDSERVICEIMPL]: SUCESSO NA CONTAGEM DAS CARTAS, ATUALMENTE TEMOS UM TOTAL DE : {} CARTAS", 	total );
			return total;
		} catch (OutLimitCardsException e) {
			LOGGER.error("[CARDSERVICEIMPL]: ERRO NA CONTAGEM DAS CARTAS ");
			throw new OutLimitCardsException("ERRO NA CONTAGEM DAS CARTAS");
		}
		
	}

}
