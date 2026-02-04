package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ProcessRouteSaveBo;
import com.qjyy.base.domain.entity.ProcessRoute;
import com.qjyy.base.domain.vo.ProcessRouteVO;
import com.qjyy.base.mapper.ProcessRouteMapper;
import com.qjyy.base.service.ProcessRouteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessRouteServiceImpl implements ProcessRouteService {

	private final ProcessRouteMapper processRouteMapper;
	private final RouteConvert routeConvert;

	@Override
	public Long create(ProcessRouteSaveBo bo) {
		ProcessRoute entity = routeConvert.toProcessRoute(bo);
		normalize(entity);
		processRouteMapper.insert(entity);
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProcessRouteSaveBo bo) {
		ProcessRoute entity = routeConvert.toProcessRoute(bo);
		normalize(entity);
		entity.setId(id);
		return processRouteMapper.updateById(entity) > 0;
	}

	@Override
	public ProcessRouteVO get(Long id) {
		ProcessRoute entity = processRouteMapper.selectById(id);
		return entity == null ? null : routeConvert.toRouteVO(entity);
	}

	@Override
	public List<ProcessRouteVO> listAll() {
		LambdaQueryWrapper<ProcessRoute> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(ProcessRoute::getId);
		return routeConvert.toRouteVOList(processRouteMapper.selectList(wrapper));
	}

	private void normalize(ProcessRoute entity) {
		entity.setRouteCode(trimToNull(entity.getRouteCode()));
		entity.setRouteName(trimToNull(entity.getRouteName()));
		entity.setProductCode(trimToNull(entity.getProductCode()));
		entity.setProductName(trimToNull(entity.getProductName()));
		entity.setDosageForm(trimToNull(entity.getDosageForm()));
		entity.setRemark(trimToNull(entity.getRemark()));
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
