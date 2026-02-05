package com.qjyy.base.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ProcessEdgeBo;
import com.qjyy.base.domain.bo.ProcessGraphSaveBo;
import com.qjyy.base.domain.bo.ProcessNodeBo;
import com.qjyy.base.domain.entity.ProcessEdge;
import com.qjyy.base.domain.entity.ProcessBase;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessGraphVO;
import com.qjyy.base.domain.vo.ProcessNodeSummaryVO;
import com.qjyy.base.mapper.ProcessEdgeMapper;
import com.qjyy.base.mapper.ProcessGraphQueryMapper;
import com.qjyy.base.mapper.ProcessBaseMapper;
import com.qjyy.base.mapper.ProcessNodeMapper;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.service.GraphValidationService;
import com.qjyy.base.service.ProcessGraphService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessGraphServiceImpl implements ProcessGraphService {

	private final ProcessNodeMapper processNodeMapper;
	private final ProcessEdgeMapper processEdgeMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final ProcessGraphQueryMapper graphQueryMapper;
	private final GraphValidationService graphValidationService;
	private final ProcessBaseMapper processBaseMapper;
	private final RouteConvert routeConvert;

	@Override
	public ProcessGraphVO getGraph(Long routeVersionId) {
		// 获取工序画布数据
		List<ProcessNode> nodes = listNodes(routeVersionId);
		List<ProcessEdge> edges = listEdges(routeVersionId);
		ProcessGraphVO vo = new ProcessGraphVO();
		vo.setRouteVersionId(routeVersionId);
		vo.setNodes(routeConvert.toProcessNodeVOList(nodes));
		vo.setEdges(routeConvert.toProcessEdgeVOList(edges));
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveGraph(ProcessGraphSaveBo bo) {
		// 保存工序画布
		if (bo == null || bo.getRouteVersionId() == null) {
			log.warn("保存工序图失败, 参数为空");
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(bo.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			log.warn("保存工序图失败, 版本不可编辑, routeVersionId={}", bo.getRouteVersionId());
			return false;
		}
		LambdaQueryWrapper<ProcessEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(ProcessEdge::getRouteVersionId, bo.getRouteVersionId());
		processEdgeMapper.delete(edgeWrapper);
		LambdaQueryWrapper<ProcessNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(ProcessNode::getRouteVersionId, bo.getRouteVersionId());
		processNodeMapper.delete(nodeWrapper);
		if (!CollectionUtils.isEmpty(bo.getNodes())) {
			Map<Long, ProcessBase> processBaseMap = buildProcessBaseMap(bo.getNodes());
			if (!validateProcessBase(bo.getNodes(), processBaseMap)) {
				log.warn("保存工序图失败, 工序基础数据不存在, routeVersionId={}", bo.getRouteVersionId());
				return false;
			}
			for (ProcessNodeBo nodeBo : bo.getNodes()) {
				applyProcessBase(nodeBo, processBaseMap);
				ProcessNode node = routeConvert.toProcessNode(nodeBo);
				node.setRouteVersionId(bo.getRouteVersionId());
				processNodeMapper.insert(node);
			}
		}
		if (!CollectionUtils.isEmpty(bo.getEdges())) {
			for (ProcessEdgeBo edgeBo : bo.getEdges()) {
				ProcessEdge edge = routeConvert.toProcessEdge(edgeBo);
				edge.setRouteVersionId(bo.getRouteVersionId());
				processEdgeMapper.insert(edge);
			}
		}
		log.info("保存工序图成功, routeVersionId={}, nodeSize={}, edgeSize={}", bo.getRouteVersionId(),
				bo.getNodes() == null ? 0 : bo.getNodes().size(), bo.getEdges() == null ? 0 : bo.getEdges().size());
		return true;
	}

	@Override
	public GraphValidationResultVO validateGraph(ProcessGraphSaveBo bo) {
		// 校验工序画布
		return graphValidationService.validateProcessGraph(bo.getNodes(), bo.getEdges());
	}

	@Override
	public List<ProcessNodeSummaryVO> listNodeSummary(Long routeVersionId) {
		// 查询工序摘要
		return graphQueryMapper.selectProcessNodeSummary(routeVersionId);
	}

	private List<ProcessNode> listNodes(Long routeVersionId) {
		LambdaQueryWrapper<ProcessNode> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessNode::getRouteVersionId, routeVersionId);
		return processNodeMapper.selectList(wrapper);
	}

	private List<ProcessEdge> listEdges(Long routeVersionId) {
		LambdaQueryWrapper<ProcessEdge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessEdge::getRouteVersionId, routeVersionId);
		return processEdgeMapper.selectList(wrapper);
	}

	private Map<Long, ProcessBase> buildProcessBaseMap(List<ProcessNodeBo> nodes) {
		List<Long> baseIds = nodes.stream()
				.map(ProcessNodeBo::getProcessBaseId)
				.filter(id -> id != null)
				.distinct()
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(baseIds)) {
			return new HashMap<>();
		}
		List<ProcessBase> bases = processBaseMapper.selectBatchIds(baseIds);
		return bases.stream().collect(Collectors.toMap(ProcessBase::getId, Function.identity()));
	}

	private void applyProcessBase(ProcessNodeBo nodeBo, Map<Long, ProcessBase> baseMap) {
		if (nodeBo == null || nodeBo.getProcessBaseId() == null) {
			return;
		}
		ProcessBase base = baseMap.get(nodeBo.getProcessBaseId());
		if (base == null) {
			return;
		}
		nodeBo.setNodeName(base.getProcessName());
		if (nodeBo.getDurationMinutes() == null) {
			nodeBo.setDurationMinutes(base.getDefaultDurationMinutes());
		}
		if (nodeBo.getCriticalFlag() == null) {
			nodeBo.setCriticalFlag(base.getCriticalFlag());
		}
	}

	private boolean validateProcessBase(List<ProcessNodeBo> nodes, Map<Long, ProcessBase> baseMap) {
		for (ProcessNodeBo node : nodes) {
			if (node.getProcessBaseId() != null && !baseMap.containsKey(node.getProcessBaseId())) {
				return false;
			}
		}
		return true;
	}
}
