package com.qjyy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qjyy.base.domain.bo.AnnualDemandQueryBo;
import com.qjyy.base.domain.vo.AnnualDemandVO;

@Mapper
public interface AnnualDemandMapper {

	IPage<AnnualDemandVO> selectPage(Page<AnnualDemandVO> page, @Param("bo") AnnualDemandQueryBo bo);

	List<AnnualDemandVO> selectList(@Param("bo") AnnualDemandQueryBo bo);
}
