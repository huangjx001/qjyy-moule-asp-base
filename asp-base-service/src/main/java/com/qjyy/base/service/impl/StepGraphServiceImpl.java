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
import com.qjyy.base.domain.bo.StepEdgeBo;
import com.qjyy.base.domain.bo.StepGraphSaveBo;
import com.qjyy.base.domain.bo.StepNodeBo;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.entity.ResourceRoom;
import com.qjyy.base.domain.entity.StepBase;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.StepEdgeVO;
import com.qjyy.base.domain.vo.StepGraphVO;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.mapper.ResourceRoomMapper;
import com.qjyy.base.mapper.StepBaseMapper;
import com.qjyy.base.mapper.StepEdgeMapper;
import com.qjyy.base.mapper.StepNodeMapper;
import com.qjyy.base.service.GraphValidationService;
import com.qjyy.base.service.StepGraphService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StepGraphServiceImpl implements StepGraphService {

	private final ResourceRoomMapper resourceRoomMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final StepNodeMapper stepNodeMapper;
	private final StepEdgeMapper stepEdgeMapper;
	private final StepBaseMapper stepBaseMapper;
	private final GraphValidationService graphValidationService;
	private final RouteConvert routeConvert;

	@Override
	public StepGraphVO getGraph(Long resourceRoomId) {
		// 获取工步画布数据
		StepGraphVO vo = new StepGraphVO();
		vo.setResourceRoomId(resourceRoomId);
		LambdaQueryWrapper<StepNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(StepNode::getResourceRoomId, resourceRoomId);
		LambdaQueryWrapper<StepEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(StepEdge::getResourceRoomId, resourceRoomId);
		vo.setNodes(routeConvert.toStepNodeVOList(stepNodeMapper.selectList(nodeWrapper)));
		vo.setEdges(routeConvert.toStepEdgeVOList(stepEdgeMapper.selectList(edgeWrapper)));
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveGraph(StepGraphSaveBo bo) {
		// 保存工步画布
		if (bo == null || bo.getResourceRoomId() == null) {
			log.warn("保存工步图失败, 参数为空");
			return false;
		}
		ResourceRoom resource = resourceRoomMapper.selectById(bo.getResourceRoomId());
		if (resource == null) {
			log.warn("保存工步图失败, 资源不存在, resourceRoomId={}", bo.getResourceRoomId());
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(resource.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			log.warn("保存工步图失败, 版本不可编辑, resourceRoomId={}", bo.getResourceRoomId());
			return false;
		}
		LambdaQueryWrapper<StepEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(StepEdge::getResourceRoomId, bo.getResourceRoomId());
		stepEdgeMapper.delete(edgeWrapper);
		LambdaQueryWrapper<StepNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(StepNode::getResourceRoomId, bo.getResourceRoomId());
		stepNodeMapper.delete(nodeWrapper);
		if (!CollectionUtils.isEmpty(bo.getNodes())) {
			Map<Long, StepBase> stepBaseMap = buildStepBaseMap(bo.getNodes());
			if (!validateStepBase(bo.getNodes(), stepBaseMap)) {
				log.warn("保存工步图失败, 工步基础数据不存在, resourceRoomId={}", bo.getResourceRoomId());
				return false;
			}
			for (StepNodeBo nodeBo : bo.getNodes()) {
				applyStepBase(nodeBo, stepBaseMap);
				StepNode node = routeConvert.toStepNode(nodeBo);
				node.setRouteVersionId(resource.getRouteVersionId());
				node.setResourceRoomId(bo.getResourceRoomId());
				stepNodeMapper.insert(node);
			}
		}
		if (!CollectionUtils.isEmpty(bo.getEdges())) {
			for (StepEdgeBo edgeBo : bo.getEdges()) {
				StepEdge edge = routeConvert.toStepEdge(edgeBo);
				edge.setRouteVersionId(resource.getRouteVersionId());
				edge.setResourceRoomId(bo.getResourceRoomId());
				stepEdgeMapper.insert(edge);
			}
		}
		log.info("保存工步图成功, resourceRoomId={}, nodeSize={}, edgeSize={}", bo.getResourceRoomId(),
				bo.getNodes() == null ? 0 : bo.getNodes().size(), bo.getEdges() == null ? 0 : bo.getEdges().size());
		return true;
	}

	@Override
	public GraphValidationResultVO validateGraph(StepGraphSaveBo bo) {
		// 校验工步画布
		return graphValidationService.validateStepGraph(bo.getNodes(), bo.getEdges());
	}

	@Override
	public List<StepEdgeVO> listRelations(Long stepNodeId) {
		LambdaQueryWrapper<StepEdge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(StepEdge::getFromNodeId, stepNodeId).or().eq(StepEdge::getToNodeId, stepNodeId);
		return routeConvert.toStepEdgeVOList(stepEdgeMapper.selectList(wrapper));
	}

	private Map<Long, StepBase> buildStepBaseMap(List<StepNodeBo> nodes) {
		List<Long> baseIds = nodes.stream()
				.map(StepNodeBo::getStepBaseId)
				.filter(id -> id != null)
				.distinct()
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(baseIds)) {
			return new HashMap<>();
		}
		List<StepBase> bases = stepBaseMapper.selectBatchIds(baseIds);
		return bases.stream().collect(Collectors.toMap(StepBase::getId, Function.identity()));
	}

	private void applyStepBase(StepNodeBo nodeBo, Map<Long, StepBase> baseMap) {
		if (nodeBo == null || nodeBo.getStepBaseId() == null) {
			return;
		}
		StepBase base = baseMap.get(nodeBo.getStepBaseId());
		if (base == null) {
			return;
		}
		nodeBo.setNodeCode(base.getStepCode());
		nodeBo.setNodeName(base.getStepName());
		nodeBo.setNodeType(base.getStepType());
		if (nodeBo.getDurationMinutes() == null) {
			nodeBo.setDurationMinutes(base.getDefaultDurationMinutes());
		}
		if (nodeBo.getQcFlag() == null) {
			nodeBo.setQcFlag(base.getQcFlag());
		}
	}

	private boolean validateStepBase(List<StepNodeBo> nodes, Map<Long, StepBase> baseMap) {
		for (StepNodeBo node : nodes) {
			if (node.getStepBaseId() != null && !baseMap.containsKey(node.getStepBaseId())) {
				return false;
			}
		}
		return true;
	}
}
