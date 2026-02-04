package com.qjyy.base.domain.enums;

import lombok.Getter;

@Getter
public enum DependencyTypeEnum {

	FS("FS", "完成-开始"),
	SS("SS", "开始-开始"),
	FF("FF", "完成-完成"),
	SF("SF", "开始-完成");

	private final String code;
	private final String desc;

	DependencyTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
