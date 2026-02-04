package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qjyy.base.domain.bo.AnnualDemandQueryBo;
import com.qjyy.base.domain.vo.AnnualDemandVO;
import com.qjyy.base.mapper.AnnualDemandMapper;
import com.qjyy.base.service.AnnualDemandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnualDemandServiceImpl implements AnnualDemandService {

	private final AnnualDemandMapper annualDemandMapper;

	@Override
	public IPage<AnnualDemandVO> page(AnnualDemandQueryBo bo) {
		normalize(bo);

		long pageNum = bo.getPageNum() == null || bo.getPageNum() < 1 ? 1L : bo.getPageNum();
		long pageSize = bo.getPageSize() == null || bo.getPageSize() < 1 ? 20L : bo.getPageSize();

		Page<AnnualDemandVO> page = new Page<>(pageNum, pageSize);
		return annualDemandMapper.selectPage(page, bo);
	}

	@Override
	public List<AnnualDemandVO> list(AnnualDemandQueryBo bo) {
		normalize(bo);
		return annualDemandMapper.selectList(bo);
	}

	private void normalize(AnnualDemandQueryBo bo) {
		bo.setWorkshopId(trimToNull(bo.getWorkshopId()));
		bo.setProductCode(trimToNull(bo.getProductCode()));
		bo.setMaterialName(trimToNull(bo.getMaterialName()));
		bo.setCurrencyName(trimToNull(bo.getCurrencyName()));
		bo.setProductionType(trimToNull(bo.getProductionType()));
	}

	private String trimToNull(String s) {
		return StringUtils.hasText(s) ? s.trim() : null;
	}
}
