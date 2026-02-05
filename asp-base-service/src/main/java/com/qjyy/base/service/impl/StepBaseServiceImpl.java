package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.BaseDataConvert;
import com.qjyy.base.domain.bo.StepBaseSaveBo;
import com.qjyy.base.domain.entity.StepBase;
import com.qjyy.base.domain.vo.StepBaseOptionVO;
import com.qjyy.base.domain.vo.StepBaseVO;
import com.qjyy.base.mapper.StepBaseMapper;
import com.qjyy.base.service.StepBaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StepBaseServiceImpl implements StepBaseService {

	private final StepBaseMapper stepBaseMapper;
	private final BaseDataConvert baseDataConvert;

	@Override
	public Long create(StepBaseSaveBo bo) {
		StepBase entity = baseDataConvert.toStepBase(bo);
		normalize(entity);
		stepBaseMapper.insert(entity);
		log.info("创建工步基础数据成功, id={}, code={}", entity.getId(), entity.getStepCode());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, StepBaseSaveBo bo) {
		StepBase entity = baseDataConvert.toStepBase(bo);
		normalize(entity);
		entity.setId(id);
		boolean updated = stepBaseMapper.updateById(entity) > 0;
		log.info("更新工步基础数据, id={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted = stepBaseMapper.deleteById(id) > 0;
		log.info("删除工步基础数据, id={}, deleted={}", id, deleted);
		return deleted;
	}

	@Override
	public StepBaseVO get(Long id) {
		StepBase entity = stepBaseMapper.selectById(id);
		return entity == null ? null : baseDataConvert.toStepBaseVO(entity);
	}

	@Override
	public List<StepBaseVO> listAll() {
		LambdaQueryWrapper<StepBase> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(StepBase::getId);
		return baseDataConvert.toStepBaseVOList(stepBaseMapper.selectList(wrapper));
	}

	@Override
	public List<StepBaseOptionVO> listOptions(String keyword) {
		LambdaQueryWrapper<StepBase> wrapper = buildKeywordWrapper(keyword);
		wrapper.eq(StepBase::getEnabled, 1);
		wrapper.orderByDesc(StepBase::getId);
		return baseDataConvert.toStepBaseOptionList(stepBaseMapper.selectList(wrapper));
	}

	private void normalize(StepBase entity) {
		entity.setStepCode(trimToNull(entity.getStepCode()));
		entity.setStepName(trimToNull(entity.getStepName()));
		entity.setStepType(trimToNull(entity.getStepType()));
		entity.setRemark(trimToNull(entity.getRemark()));
	}

	private LambdaQueryWrapper<StepBase> buildKeywordWrapper(String keyword) {
		LambdaQueryWrapper<StepBase> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(keyword)) {
			String like = "%" + keyword.trim() + "%";
			wrapper.and(q -> q.like(StepBase::getStepCode, like).or()
					.like(StepBase::getStepName, like).or()
					.like(StepBase::getStepType, like));
		}
		return wrapper;
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
