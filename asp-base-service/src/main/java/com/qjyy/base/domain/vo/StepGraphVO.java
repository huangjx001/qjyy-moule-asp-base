package com.qjyy.base.domain.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工步图")
public class StepGraphVO {

	@ApiModelProperty(value = "资源ID")
	private Long resourceRoomId;

	@ApiModelProperty(value = "工步节点")
	private List<StepNodeVO> nodes;

	@ApiModelProperty(value = "工步依赖边")
	private List<StepEdgeVO> edges;
}
