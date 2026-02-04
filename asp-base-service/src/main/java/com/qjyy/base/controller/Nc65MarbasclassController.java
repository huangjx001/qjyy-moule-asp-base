package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.vo.Nc65MarbasclassTreeVO;
import com.qjyy.base.service.Nc65MarbasclassService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/nc65/marbasclass")
@RequiredArgsConstructor
@Api(tags = "NC65物料分类")
public class Nc65MarbasclassController {

	private final Nc65MarbasclassService nc65MarbasclassService;

	@GetMapping("/tree")
	@ApiOperation(value = "查询物料分类树", notes = "parentCode 为空为根节点，支持多个根节点")
	public R<List<Nc65MarbasclassTreeVO>> tree() {
		return R.ok(nc65MarbasclassService.tree());
	}
}
