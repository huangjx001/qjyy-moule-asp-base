package com.qjyy.base.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线")
@TableName("process_route")
public class ProcessRoute {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "路线编码")
	private String routeCode;

	@ApiModelProperty(value = "路线名称")
	private String routeName;

	@ApiModelProperty(value = "产品编码")
	private String productCode;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "剂型")
	private String dosageForm;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
