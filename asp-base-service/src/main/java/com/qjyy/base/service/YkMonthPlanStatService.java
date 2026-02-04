package com.qjyy.base.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qjyy.base.domain.bo.YkMonthPlanStatPageQuery;
import com.qjyy.base.domain.entity.YkMonthPlanStat;
import com.qjyy.base.domain.vo.YkMonthPlanStatVO;

public interface YkMonthPlanStatService extends IService<YkMonthPlanStat> {

	/**
	 * 月度计划统计分页查询。
	 */
	IPage<YkMonthPlanStatVO> page(YkMonthPlanStatPageQuery bo);

	/**
	 * 月度计划统计列表查询（不分页）。
	 */
	List<YkMonthPlanStatVO> list(YkMonthPlanStatPageQuery bo);
}
