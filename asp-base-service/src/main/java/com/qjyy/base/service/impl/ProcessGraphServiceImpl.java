package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ProcessEdgeBo;
import com.qjyy.base.domain.bo.ProcessGraphSaveBo;
import com.qjyy.base.domain.bo.ProcessNodeBo;
import com.qjyy.base.domain.entity.ProcessEdge;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessGraphVO;
import com.qjyy.base.domain.vo.ProcessNodeSummaryVO;
import com.qjyy.base.mapper.ProcessEdgeMapper;
import com.qjyy.base.mapper.ProcessGraphQueryMapper;
import com.qjyy.base.mapper.ProcessNodeMapper;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.service.GraphValidationService;
import com.qjyy.base.service.ProcessGraphService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessGraphServiceImpl implements ProcessGraphService {

	private final ProcessNodeMapper processNodeMapper;
	private final ProcessEdgeMapper processEdgeMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final ProcessGraphQueryMapper graphQueryMapper;
	private final GraphValidationService graphValidationService;
	private final RouteConvert routeConvert;

	@Override
	public ProcessGraphVO getGraph(Long routeVersionId) {
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
		if (bo == null || bo.getRouteVersionId() == null) {
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(bo.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return false;
		}
		LambdaQueryWrapper<ProcessEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(ProcessEdge::getRouteVersionId, bo.getRouteVersionId());
		processEdgeMapper.delete(edgeWrapper);
		LambdaQueryWrapper<ProcessNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(ProcessNode::getRouteVersionId, bo.getRouteVersionId());
		processNodeMapper.delete(nodeWrapper);
		if (!CollectionUtils.isEmpty(bo.getNodes())) {
			for (ProcessNodeBo nodeBo : bo.getNodes()) {
				ProcessNode node = new ProcessNode();
				node.setId(nodeBo.getId());
				node.setRouteVersionId(bo.getRouteVersionId());
				node.setNodeCode(nodeBo.getNodeCode());
				node.setNodeName(nodeBo.getNodeName());
				node.setNodeType(nodeBo.getNodeType());
				node.setCriticalFlag(nodeBo.getCriticalFlag());
				node.setDurationMinutes(nodeBo.getDurationMinutes());
				node.setPositionX(nodeBo.getPositionX());
				node.setPositionY(nodeBo.getPositionY());
				node.setStatus(nodeBo.getStatus());
				processNodeMapper.insert(node);
			}
		}
		if (!CollectionUtils.isEmpty(bo.getEdges())) {
			for (ProcessEdgeBo edgeBo : bo.getEdges()) {
				ProcessEdge edge = new ProcessEdge();
				edge.setId(edgeBo.getId());
				edge.setRouteVersionId(bo.getRouteVersionId());
				edge.setFromNodeId(edgeBo.getFromNodeId());
				edge.setToNodeId(edgeBo.getToNodeId());
				edge.setDependencyType(edgeBo.getDependencyType());
				edge.setDependencyStrength(edgeBo.getDependencyStrength());
				edge.setLagMinutes(edgeBo.getLagMinutes());
				processEdgeMapper.insert(edge);
			}
		}
		return true;
	}

	@Override
	public GraphValidationResultVO validateGraph(ProcessGraphSaveBo bo) {
		return graphValidationService.validateProcessGraph(bo.getNodes(), bo.getEdges());
	}

	@Override
	public List<ProcessNodeSummaryVO> listNodeSummary(Long routeVersionId) {
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
}
