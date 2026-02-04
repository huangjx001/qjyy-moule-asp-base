package com.qjyy.base.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序依赖边")
@TableName("process_edge")
public class ProcessEdge {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "前置工序ID")
	private Long fromNodeId;

	@ApiModelProperty(value = "后置工序ID")
	private Long toNodeId;

	@ApiModelProperty(value = "依赖类型:FS/SS/FF/SF")
	private String dependencyType;

	@ApiModelProperty(value = "依赖强度:HARD/SOFT")
	private String dependencyStrength;

	@ApiModelProperty(value = "滞后分钟")
	private Integer lagMinutes;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
