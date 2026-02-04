package com.qjyy.base.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("nc65_marbasclass")
@ApiModel(value = "Nc65Marbasclass", description = "NC65物料分类信息实体(Marbasclass)")
public class Nc65Marbasclass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "本地主键", example = "1")
    private Long id;

    @ApiModelProperty(value = "NC物料分类主键(id)", example = "1001A110000000000ABC")
    private String ncId;

    @ApiModelProperty(value = "物料分类编号(code)", required = true, example = "010201")
    private String code;

    @ApiModelProperty(value = "物料分类名称(name)", required = true, example = "片剂")
    private String name;

    @ApiModelProperty(value = "父类编号(parentcode)", example = "01")
    private String parentCode;

    @ApiModelProperty(value = "父类名称(parentname)", example = "制剂")
    private String parentName;

    @ApiModelProperty(value = "创建时间", example = "2026-01-27T10:20:30")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间", example = "2026-01-27T10:20:30")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
