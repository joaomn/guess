package br.com.guessApi.controlles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.guessApi.entitys.CardEntity;
import br.com.guessApi.entitys.dtos.CardRecordResponse;
import br.com.guessApi.entitys.dtos.CardUpdateObject;
import br.com.guessApi.enums.LevelType;
import br.com.guessApi.service.implementations.CardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/card")
public class CardController {

	private CardServiceImpl cardService;

	public CardController(CardServiceImpl cardService) {
		this.cardService = cardService;
	}

	@Operation(description = "Salvar um card")
	@PostMapping
	public ResponseEntity<CardRecordResponse> store(@Valid @RequestBody CardUpdateObject card) {
		CardEntity newCard = cardService.store(card);
		return ResponseEntity.status(HttpStatus.CREATED).body(new CardRecordResponse(newCard.getIdentifier(),
				card.question(),card.awnser(), card.photo(), card.level(), "Criado com sucesso!"));
	}

	@Operation(description = "Obter todos os cards")
	@GetMapping
	public ResponseEntity<List<CardRecordResponse>> index() {
		List<CardRecordResponse> list = new ArrayList<>();
		List<CardEntity> all = cardService.getAll();
		if (all.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		list = all.stream().map(card -> new CardRecordResponse(card.getIdentifier(), card.getQuestion(),card.getAwnser(),
				card.getPhoto(), card.getLevel(), "")).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(list);

	}
	
	@Operation(description = "Obter Card pelo identifier")
	@GetMapping("/{identifier}")
	public ResponseEntity<CardRecordResponse> showCardById(@PathVariable String identifier) {

		CardEntity obj = cardService.getByUUID(identifier);
		if (obj.getIdentifier() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CardRecordResponse(null, null,null, null, null, "Identifier não existe ou foi digitado errado"));
		}
		
		CardRecordResponse response = new CardRecordResponse(obj.getIdentifier(), obj.getQuestion(),obj.getAwnser(), obj.getPhoto(), obj.getLevel(), "");
		
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
	@Operation(description = "Contador de quantas cartas tem no deck")
	@GetMapping("/count-deck")
	public ResponseEntity<Integer> showCountOfDeck() {

		Integer countDeck = cardService.getCountDeck();
		if (countDeck <= 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(countDeck);

	}

	@Operation(description = "Atualizar Card")
	@PutMapping("/{identifier}")
	public ResponseEntity<String> updateCard(@PathVariable String identifier, @RequestBody CardUpdateObject card) {

		CardEntity obj = cardService.getByUUID(identifier);

		if (obj.getIdentifier() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identifier errado ou não existe");
		}

		cardService.update(identifier, card);
		return ResponseEntity.status(HttpStatus.OK).body("Card atualizado com sucesso");

	}

	@Operation(description = "Deletar Card pelo identifier")
	@DeleteMapping("/{identifier}")
	public ResponseEntity<String> deleteCard(@PathVariable String identifier) {

		CardEntity obj = cardService.getByUUID(identifier);

		if (obj.getIdentifier() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identifier errado ou não existe");
		}

		cardService.delete(identifier);
		return ResponseEntity.status(HttpStatus.OK).body("Card deletado com sucesso");

	}
	
	
	@Operation(description = "Obter todos as cartas por level")
	@GetMapping("/by-level/{level}")
	public ResponseEntity<List<CardRecordResponse>> showcardsByLevel(@PathVariable LevelType level){
			
			List<CardRecordResponse> list = new ArrayList<>();
			List<CardEntity> all = cardService.getByLevel(level);
			if (all.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			list = all.stream().map(card -> new CardRecordResponse(card.getIdentifier(), card.getQuestion(),card.getAwnser(),
					card.getPhoto(), card.getLevel(), "")).collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	
	@Operation(description = "Obter um numero x de cartas, sorteadas randomicamente")
	@GetMapping("/by-random-count/{count}")
	public ResponseEntity<List<CardRecordResponse>> showDisplaysByUser(@PathVariable int count){
			
			List<CardRecordResponse> list = new ArrayList<>();
			List<CardEntity> all = cardService.getRandoByLimit(count);
			if (all.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			list = all.stream().map(card -> new CardRecordResponse(card.getIdentifier(), card.getQuestion(),card.getAwnser(),
					card.getPhoto(), card.getLevel(), "")).collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

}
