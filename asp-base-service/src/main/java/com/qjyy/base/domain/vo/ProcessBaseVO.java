package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序基础数据")
public class ProcessBaseVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "工序编码")
	private String processCode;

	@ApiModelProperty(value = "工序名称")
	private String processName;

	@ApiModelProperty(value = "工序类型")
	private String processType;

	@ApiModelProperty(value = "默认时长(分钟)")
	private Integer defaultDurationMinutes;

	@ApiModelProperty(value = "是否关键工序")
	private Integer criticalFlag;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;
}
