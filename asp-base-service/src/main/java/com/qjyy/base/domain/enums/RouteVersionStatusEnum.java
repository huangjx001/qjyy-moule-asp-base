package com.qjyy.base.domain.enums;

import lombok.Getter;

@Getter
public enum RouteVersionStatusEnum {

	DRAFT("DRAFT", "草稿"),
	RELEASED("RELEASED", "已发布");

	private final String code;
	private final String desc;

	RouteVersionStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
