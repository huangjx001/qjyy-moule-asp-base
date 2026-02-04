package com.qjyy.base.domain.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序图")
public class ProcessGraphVO {

	@ApiModelProperty(value = "路线版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "工序节点")
	private List<ProcessNodeVO> nodes;

	@ApiModelProperty(value = "工序依赖边")
	private List<ProcessEdgeVO> edges;
}
