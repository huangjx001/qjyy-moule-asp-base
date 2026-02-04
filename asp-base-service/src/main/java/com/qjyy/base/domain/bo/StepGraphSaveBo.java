package com.qjyy.base.domain.bo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工步图保存请求")
public class StepGraphSaveBo {

	@ApiModelProperty(value = "资源ID")
	private Long resourceRoomId;

	@ApiModelProperty(value = "工步节点")
	private List<StepNodeBo> nodes;

	@ApiModelProperty(value = "工步依赖边")
	private List<StepEdgeBo> edges;
}
