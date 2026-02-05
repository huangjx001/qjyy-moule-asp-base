package com.qjyy.base.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "路线-产品关联")
@TableName("process_route_product")
public class ProcessRouteProduct {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "路线ID")
	private Long routeId;

	@ApiModelProperty(value = "产品编码(为空表示默认路线)")
	private String productCode;

	@ApiModelProperty(value = "是否默认路线")
	private Integer isDefault;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
