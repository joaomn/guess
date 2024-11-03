package br.com.guessApi.entitys.dtos;


import br.com.guessApi.enums.LevelType;

public record CardRecordResponse(String identifier, String question, String photo, LevelType level, String msg) {


}
