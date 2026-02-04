package com.qjyy.base.domain.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Nc65MarbasclassTreeVO", description = "NC65物料分类树节点")
public class Nc65MarbasclassTreeVO {

	@ApiModelProperty(value = "本地主键", example = "1")
	private Long id;

	@ApiModelProperty(value = "NC物料分类主键", example = "1001A110000000000ABC")
	private String ncId;

	@ApiModelProperty(value = "分类编号", example = "010201")
	private String code;

	@ApiModelProperty(value = "分类名称", example = "片剂")
	private String name;

	@ApiModelProperty(value = "父类编号(根节点为空)", example = "01")
	private String parentCode;

	@ApiModelProperty(value = "父类名称", example = "制剂")
	private String parentName;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdAt;

	@ApiModelProperty(value = "更新时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updatedAt;

	@ApiModelProperty(value = "该分类下直接挂载的物料数量(typeNo=code)")
	private Integer materialCount = 0;

	@ApiModelProperty(value = "该分类树(含子孙)物料总数")
	private Integer totalMaterialCount = 0;

	@ApiModelProperty(value = "子节点")
	private List<Nc65MarbasclassTreeVO> children = new ArrayList<>();
}
