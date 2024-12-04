package br.com.guessApi.entitys.dtos;

import br.com.guessApi.enums.LevelType;

public record CardUpdateObject(String question,String awnser, String photo, LevelType level) {

}
