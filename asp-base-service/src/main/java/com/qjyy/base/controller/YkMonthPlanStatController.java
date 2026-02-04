package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qjyy.base.domain.bo.YkMonthPlanStatPageQuery;
import com.qjyy.base.domain.vo.YkMonthPlanStatVO;
import com.qjyy.base.service.YkMonthPlanStatService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 月度计划统计接口。
 */
@Api(tags = "月度计划统计")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ykMonthPlanStat")
public class YkMonthPlanStatController {

	private final YkMonthPlanStatService ykMonthPlanStatService;

	@ApiOperation(value = "月度计划统计分页查询")
	@GetMapping("/page")
	public R<IPage<YkMonthPlanStatVO>> page(YkMonthPlanStatPageQuery bo) {
		return R.ok(ykMonthPlanStatService.page(bo));
	}

	@ApiOperation(value = "月度计划统计列表查询(不分页)")
	@GetMapping("/list")
	public R<List<YkMonthPlanStatVO>> list(YkMonthPlanStatPageQuery bo) {
		return R.ok(ykMonthPlanStatService.list(bo));
	}
}
