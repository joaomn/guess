package br.com.guessApi;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

import br.com.guessApi.entitys.CardEntity;
import br.com.guessApi.entitys.dtos.CardUpdateObject;
import br.com.guessApi.enums.LevelType;
import br.com.guessApi.exceptions.CardException;
import br.com.guessApi.repository.CardRepository;
import br.com.guessApi.service.implementations.CardServiceImpl;

@SpringBootTest
@ActiveProfiles("test") 
@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
public class CardServiceImplTest {

    private static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardServiceImpl cardService;

    @BeforeAll
    void setUp() {
        mysqlContainer.start();
    }

    @AfterAll
    void tearDown() {
        mysqlContainer.stop();
    }

    @BeforeEach
    void init() {
        cardRepository.deleteAll(); 
    }

    @Test
    void testStoreCard() throws CardException {
        CardUpdateObject newCard = new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY);
        cardService.store(newCard);

        List<CardEntity> cards = cardService.getAll();
        assertEquals(1, cards.size());
        assertEquals("Qual é a capital da França?", cards.get(0).getQuestion());
    }

    @Test
    void testUpdateCard() throws CardException {
        CardUpdateObject newCard = new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY);
        cardService.store(newCard);

        String identifier = cardService.getAll().get(0).getIdentifier();
        CardUpdateObject updatedCard = new CardUpdateObject("Qual é a capital da Alemanha?", null, null);
        cardService.update(identifier, updatedCard);

        CardEntity updatedEntity = cardService.getByUUID(identifier);
        assertEquals("Qual é a capital da Alemanha?", updatedEntity.getQuestion());
    }

    @Test
    void testDeleteCard() throws CardException {
        CardUpdateObject newCard = new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY);
        cardService.store(newCard);

        String identifier = cardService.getAll().get(0).getIdentifier();
        cardService.delete(identifier);

        List<CardEntity> cards = cardService.getAll();
        assertTrue(cards.isEmpty());
    }

    @Test
    void testGetAllCards() throws CardException {
        cardService.store(new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY));
        cardService.store(new CardUpdateObject("Qual é a capital da Alemanha?", "url.com.foto2", LevelType.MEDIUM));

        List<CardEntity> cards = cardService.getAll();
        assertEquals(2, cards.size());
    }
    
    @Test
    void testGetByUUID() throws CardException {
        CardUpdateObject newCard = new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY);
        cardService.store(newCard);

        String identifier = cardService.getAll().get(0).getIdentifier();
        CardEntity foundCard = cardService.getByUUID(identifier);
        assertEquals("Qual é a capital da França?", foundCard.getQuestion());
    }

    @Test
    void testGetByLevel() throws CardException {
        cardService.store(new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY));
        cardService.store(new CardUpdateObject("Qual é a capital da Alemanha?", "url.com.foto2", LevelType.MEDIUM));
        cardService.store(new CardUpdateObject("Qual é a capital do Brasil?", "url.com.foto3", LevelType.EASY));

        List<CardEntity> easyCards = cardService.getByLevel(LevelType.EASY);
        assertEquals(2, easyCards.size());
        assertTrue(easyCards.stream().allMatch(card -> card.getLevel() == LevelType.EASY));
    }

    @Test
    void testGetRandoByLimit() throws CardException {
        cardService.store(new CardUpdateObject("Qual é a capital da França?", "url.com.foto", LevelType.EASY));
        cardService.store(new CardUpdateObject("Qual é a capital da Alemanha?", "url.com.foto2", LevelType.MEDIUM));
        cardService.store(new CardUpdateObject("Qual é a capital do Brasil?", "url.com.foto3", LevelType.EASY));

        List<CardEntity> randomCards = cardService.getRandoByLimit(2);
        assertEquals(2, randomCards.size());
    }
    
    
    @Test
    void testUpdateNonExistentCard() {
        CardUpdateObject updatedCard = new CardUpdateObject("Qual é a capital da Alemanha?", "url.com.foto", LevelType.EASY);
        String nonExistentIdentifier = "non-existent-uuid";

        Exception exception = assertThrows(CardException.class, () -> {
            cardService.update(nonExistentIdentifier, updatedCard);
        });

        String expectedMessage = "Card com identifier " + nonExistentIdentifier + " não encontrado."; 
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testDeleteNonExistentCard() {
        String nonExistentIdentifier = "non-existent-uuid";

        Exception exception = assertThrows(CardException.class, () -> {
            cardService.delete(nonExistentIdentifier);
        });

        String expectedMessage = "Card com identifier " + nonExistentIdentifier + " não encontrado."; 
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testGetByUUIDNonExistentCard() {
        String nonExistentIdentifier = "non-existent-uuid";

        Exception exception = assertThrows(CardException.class, () -> {
            cardService.getByUUID(nonExistentIdentifier);
        });

        String expectedMessage = "Card com identifier " + nonExistentIdentifier + " não encontrado.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

//    @Test
//    void testStoreCardWithInvalidData() {
//        CardUpdateObject newCard = new CardUpdateObject(null, "url.com.foto", LevelType.EASY);
//
//        Exception exception = assertThrows(CardException.class, () -> {
//            cardService.store(newCard);
//        });
//
//        String expectedMessage = "Field Question is Required"; 
//        assertTrue(exception.getMessage().contains(expectedMessage));
//    }

}
