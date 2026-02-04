package com.qjyy.base.domain.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "图校验结果")
public class GraphValidationResultVO {

	@ApiModelProperty(value = "是否通过")
	private boolean valid;

	@ApiModelProperty(value = "错误列表")
	private List<GraphValidationErrorVO> errors;
}
