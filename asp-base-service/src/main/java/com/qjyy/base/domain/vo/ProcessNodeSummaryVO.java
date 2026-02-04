package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序节点摘要")
public class ProcessNodeSummaryVO {

	@ApiModelProperty(value = "工序ID")
	private Long nodeId;

	@ApiModelProperty(value = "工序名称")
	private String nodeName;

	@ApiModelProperty(value = "状态")
	private String status;

	@ApiModelProperty(value = "资源数")
	private Integer resourceCount;

	@ApiModelProperty(value = "工步数")
	private Integer stepCount;

	@ApiModelProperty(value = "设备数")
	private Integer deviceCount;
}
