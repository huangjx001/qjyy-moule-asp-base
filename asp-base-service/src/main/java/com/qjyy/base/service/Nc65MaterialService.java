package com.qjyy.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qjyy.base.domain.bo.Nc65MaterialQueryBo;
import com.qjyy.base.domain.entity.Nc65Material;
import com.qjyy.base.domain.vo.Nc65MaterialBriefVO;

public interface Nc65MaterialService extends IService<Nc65Material> {

	Page<Nc65MaterialBriefVO> pageByBo(Nc65MaterialQueryBo bo);

	/**
	 * 根据产品编码获取物料名称（查不到返回 "未知物料"）
	 */
	String getMaterialNameByProductCode(String productCode);

	/**
	 * 批量根据产品编码获取物料名称（查不到的不放入 map）
	 */
	Map<String, String> getMaterialNameMapByProductCodes(List<String> productCodes);
}
