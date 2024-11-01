package br.com.guessApi.entitys.dtos;

import java.util.UUID;

import br.com.guessApi.enums.LevelType;

public record CardRecordResponse(UUID identifier, String question, String photo, LevelType level) {

}
