package com.pankiba.mapstruct.domain;

public enum Grade {

	Developer(1001, "Developer"), Lead(1002, "Lead"), Architect(1003, "Architect");

	private final int levelCode;
	private final String level;

	Grade(int levelCode, String level) {
		this.levelCode = levelCode;
		this.level = level;
	}

	public int getLevelCode() {
		return this.levelCode;
	}

	public String getLevel() {
		return level;
	}
}