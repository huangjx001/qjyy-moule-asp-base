package com.qjyy.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qjyy.base.domain.entity.Nc65Marbasclass;
import com.qjyy.base.domain.vo.Nc65MarbasclassTreeVO;

public interface Nc65MarbasclassService extends IService<Nc65Marbasclass> {

	/**
	 * 查询物料分类树（parentCode 为空为根，可多根）
	 */
	List<Nc65MarbasclassTreeVO> tree();

	/**
	 * 获取某分类 code 的自身 + 所有子孙分类 code
	 */
	List<String> selfAndDescendantCodes(String rootCode);
}
