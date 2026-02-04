package com.qjyy.base.domain.enums;

import lombok.Getter;

@Getter
public enum DependencyStrengthEnum {

	HARD("HARD", "硬约束"),
	SOFT("SOFT", "软约束");

	private final String code;
	private final String desc;

	DependencyStrengthEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
