package com.qjyy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjyy.base.domain.bo.TypeNoCountDTO;
import com.qjyy.base.domain.entity.Nc65Material;

@Mapper
public interface Nc65MaterialMapper extends BaseMapper<Nc65Material> {
	@Select("SELECT type_no AS typeNo, COUNT(1) AS cnt FROM nc65_material GROUP BY type_no")
	List<TypeNoCountDTO> countByTypeNo();
}
