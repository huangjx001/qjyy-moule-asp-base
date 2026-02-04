package com.qjyy.base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qjyy.base.domain.bo.AnnualDemandQueryBo;
import com.qjyy.base.domain.vo.AnnualDemandVO;
import com.qjyy.base.service.AnnualDemandService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "年度需求")
@RestController
@RequiredArgsConstructor
@RequestMapping("/annualDemand")
public class AnnualDemandController {

	private final AnnualDemandService annualDemandService;

	@ApiOperation(value = "年度需求分页查询")
	@GetMapping("/page")
	public R<IPage<AnnualDemandVO>> page(AnnualDemandQueryBo bo) {
		return R.ok(annualDemandService.page(bo));
	}

	@ApiOperation(value = "年度需求列表查询(不分页)")
	@GetMapping("/list")
	public R<?> list(AnnualDemandQueryBo bo) {
		return R.ok(annualDemandService.list(bo));
	}
}
