package br.com.guessApi.enums;

import br.com.guessApi.exceptions.EnumException;

public enum LevelType {
	EASY(1), MEDIUM(2), HARD(3);

	private int level;

	LevelType(int value) {
		this.level = value;
	}

	public int getLevel() {
		return level;
	}

	public static LevelType getLevel(int value) throws EnumException {

			for (LevelType enu : LevelType.values()) {
				if (enu.level == value) {
					return enu;
				}

			}
			throw new EnumException("Erro ao achar o level do enum " + value);

	}
}
