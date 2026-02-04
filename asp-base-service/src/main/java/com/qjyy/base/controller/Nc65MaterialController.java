package com.qjyy.base.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qjyy.base.domain.bo.Nc65MaterialQueryBo;
import com.qjyy.base.domain.vo.Nc65MaterialBriefVO;
import com.qjyy.base.service.Nc65MaterialService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/nc65/material")
@RequiredArgsConstructor
@Api(tags = "NC65物料")
public class Nc65MaterialController {

    private final Nc65MaterialService nc65MaterialService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询物料(按分类code，可包含子类型)", notes = "classCode必填；includeChildren默认true")
    public R<Page<Nc65MaterialBriefVO>> page(@ModelAttribute Nc65MaterialQueryBo bo) {
        if (bo == null || StringUtils.isBlank(bo.getClassCode())) {
            return R.fail("classCode不能为空");
        }
        return R.ok(nc65MaterialService.pageByBo(bo));
    }
}
