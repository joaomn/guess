package br.com.guessApi.controlles.adivices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.guessApi.entitys.dtos.CardRecordResponse;
import br.com.guessApi.exceptions.CardException;
import br.com.guessApi.exceptions.OutLimitCardsException;

@ControllerAdvice
public class CardAdviceHandler {

	
	@ExceptionHandler(CardException.class)
	public ResponseEntity<CardRecordResponse> handleCardException(CardException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new CardRecordResponse("", "","", "", null, e.getMessage()));
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<CardRecordResponse> handleRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new CardRecordResponse("", "","", "", null, e.getMessage()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CardRecordResponse> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new CardRecordResponse("", "","", "", null, e.getMessage()));
	}
	
	@ExceptionHandler(OutLimitCardsException.class)
	public ResponseEntity<String> handleOutLimitException(OutLimitCardsException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(e.getMessage());
	}
}
