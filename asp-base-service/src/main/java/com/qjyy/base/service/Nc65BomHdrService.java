package com.qjyy.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qjyy.base.domain.entity.Nc65BomHdr;

public interface Nc65BomHdrService extends IService<Nc65BomHdr> {

	/**
	 * 批量统计：key=mlId，value=hdr行数（通常=版本数）
	 */
	Map<String, Long> countBomHdrByMlIds(List<String> mlIds);
}
