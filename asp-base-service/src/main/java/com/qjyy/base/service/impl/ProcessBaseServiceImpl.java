package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.BaseDataConvert;
import com.qjyy.base.domain.bo.ProcessBaseSaveBo;
import com.qjyy.base.domain.entity.ProcessBase;
import com.qjyy.base.domain.vo.ProcessBaseOptionVO;
import com.qjyy.base.domain.vo.ProcessBaseVO;
import com.qjyy.base.mapper.ProcessBaseMapper;
import com.qjyy.base.service.ProcessBaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessBaseServiceImpl implements ProcessBaseService {

	private final ProcessBaseMapper processBaseMapper;
	private final BaseDataConvert baseDataConvert;

	@Override
	public Long create(ProcessBaseSaveBo bo) {
		ProcessBase entity = baseDataConvert.toProcessBase(bo);
		normalize(entity);
		processBaseMapper.insert(entity);
		log.info("创建工序基础数据成功, id={}, code={}", entity.getId(), entity.getProcessCode());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProcessBaseSaveBo bo) {
		ProcessBase entity = baseDataConvert.toProcessBase(bo);
		normalize(entity);
		entity.setId(id);
		boolean updated = processBaseMapper.updateById(entity) > 0;
		log.info("更新工序基础数据, id={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted = processBaseMapper.deleteById(id) > 0;
		log.info("删除工序基础数据, id={}, deleted={}", id, deleted);
		return deleted;
	}

	@Override
	public ProcessBaseVO get(Long id) {
		ProcessBase entity = processBaseMapper.selectById(id);
		return entity == null ? null : baseDataConvert.toProcessBaseVO(entity);
	}

	@Override
	public List<ProcessBaseVO> listAll() {
		LambdaQueryWrapper<ProcessBase> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(ProcessBase::getId);
		return baseDataConvert.toProcessBaseVOList(processBaseMapper.selectList(wrapper));
	}

	@Override
	public List<ProcessBaseOptionVO> listOptions(String keyword) {
		LambdaQueryWrapper<ProcessBase> wrapper = buildKeywordWrapper(keyword);
		wrapper.orderByDesc(ProcessBase::getId);
		return baseDataConvert.toProcessBaseOptionList(processBaseMapper.selectList(wrapper));
	}

	private void normalize(ProcessBase entity) {
		entity.setProcessCode(trimToNull(entity.getProcessCode()));
		entity.setProcessName(trimToNull(entity.getProcessName()));
		entity.setProcessType(trimToNull(entity.getProcessType()));
		entity.setRemark(trimToNull(entity.getRemark()));
	}

	private LambdaQueryWrapper<ProcessBase> buildKeywordWrapper(String keyword) {
		LambdaQueryWrapper<ProcessBase> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(keyword)) {
			String like = "%" + keyword.trim() + "%";
			wrapper.and(q -> q.like(ProcessBase::getProcessCode, like).or()
					.like(ProcessBase::getProcessName, like).or()
					.like(ProcessBase::getProcessType, like));
		}
		return wrapper;
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
