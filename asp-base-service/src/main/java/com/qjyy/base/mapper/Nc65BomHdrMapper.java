package com.qjyy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjyy.base.domain.bo.Nc65BomHdrCountBo;
import com.qjyy.base.domain.entity.Nc65BomHdr;

@Mapper
public interface Nc65BomHdrMapper extends BaseMapper<Nc65BomHdr> {

	/**
	 * 批量统计 BOM 主表数量：每个 ml_id 一行
	 */
	List<Nc65BomHdrCountBo> countByMlIds(@Param("mlIds") List<String> mlIds);
}
