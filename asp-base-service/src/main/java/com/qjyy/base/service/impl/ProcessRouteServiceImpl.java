package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ProcessRouteSaveBo;
import com.qjyy.base.domain.entity.ProductBase;
import com.qjyy.base.domain.entity.ProcessRoute;
import com.qjyy.base.domain.vo.ProcessRouteVO;
import com.qjyy.base.mapper.ProductBaseMapper;
import com.qjyy.base.mapper.ProcessRouteMapper;
import com.qjyy.base.service.ProcessRouteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessRouteServiceImpl implements ProcessRouteService {

	private final ProcessRouteMapper processRouteMapper;
	private final ProductBaseMapper productBaseMapper;
	private final RouteConvert routeConvert;

	@Override
	public Long create(ProcessRouteSaveBo bo) {
		// 创建工艺路线
		ProcessRoute entity = routeConvert.toProcessRoute(bo);
		if (!applyProductBase(entity)) {
			log.warn("创建工艺路线失败, 产品基础数据不存在, productId={}", entity.getProductId());
			return null;
		}
		normalize(entity);
		processRouteMapper.insert(entity);
		log.info("创建工艺路线成功, routeId={}, routeCode={}", entity.getId(), entity.getRouteCode());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProcessRouteSaveBo bo) {
		// 更新工艺路线
		ProcessRoute entity = routeConvert.toProcessRoute(bo);
		if (!applyProductBase(entity)) {
			log.warn("更新工艺路线失败, 产品基础数据不存在, productId={}", entity.getProductId());
			return false;
		}
		normalize(entity);
		entity.setId(id);
		boolean updated = processRouteMapper.updateById(entity) > 0;
		log.info("更新工艺路线, routeId={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public ProcessRouteVO get(Long id) {
		// 查询工艺路线详情
		ProcessRoute entity = processRouteMapper.selectById(id);
		return entity == null ? null : routeConvert.toRouteVO(entity);
	}

	@Override
	public List<ProcessRouteVO> listAll() {
		// 查询工艺路线列表
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

	private boolean applyProductBase(ProcessRoute entity) {
		if (entity == null || entity.getProductId() == null) {
			return true;
		}
		ProductBase base = productBaseMapper.selectById(entity.getProductId());
		if (base == null) {
			return false;
		}
		entity.setProductCode(base.getProductCode());
		entity.setProductName(base.getProductName());
		entity.setDosageForm(base.getDosageForm());
		return true;
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
