package com.qjyy.base.service.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qjyy.base.domain.bo.ProcessEdgeBo;
import com.qjyy.base.domain.bo.ProcessNodeBo;
import com.qjyy.base.domain.bo.StepEdgeBo;
import com.qjyy.base.domain.bo.StepNodeBo;
import com.qjyy.base.domain.entity.ProcessEdge;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.vo.GraphValidationErrorVO;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.service.GraphValidationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GraphValidationServiceImpl implements GraphValidationService {

	@Override
	public GraphValidationResultVO validateProcessGraph(List<ProcessNodeBo> nodes, List<ProcessEdgeBo> edges) {
		// 校验工序图
		return validate(nodes, edges);
	}

	@Override
	public GraphValidationResultVO validateStepGraph(List<StepNodeBo> nodes, List<StepEdgeBo> edges) {
		// 校验工步图
		return validate(nodes, edges);
	}

	@Override
	public GraphValidationResultVO validateProcessEntities(List<ProcessNode> nodes, List<ProcessEdge> edges) {
		// 校验工序实体
		List<ProcessNodeBo> nodeBos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(nodes)) {
			for (ProcessNode node : nodes) {
				ProcessNodeBo bo = new ProcessNodeBo();
				bo.setId(node.getId());
				nodeBos.add(bo);
			}
		}
		List<ProcessEdgeBo> edgeBos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(edges)) {
			for (ProcessEdge edge : edges) {
				ProcessEdgeBo bo = new ProcessEdgeBo();
				bo.setId(edge.getId());
				bo.setFromNodeId(edge.getFromNodeId());
				bo.setToNodeId(edge.getToNodeId());
				edgeBos.add(bo);
			}
		}
		return validate(nodeBos, edgeBos);
	}

	@Override
	public GraphValidationResultVO validateStepEntities(List<StepNode> nodes, List<StepEdge> edges) {
		// 校验工步实体
		List<StepNodeBo> nodeBos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(nodes)) {
			for (StepNode node : nodes) {
				StepNodeBo bo = new StepNodeBo();
				bo.setId(node.getId());
				nodeBos.add(bo);
			}
		}
		List<StepEdgeBo> edgeBos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(edges)) {
			for (StepEdge edge : edges) {
				StepEdgeBo bo = new StepEdgeBo();
				bo.setId(edge.getId());
				bo.setFromNodeId(edge.getFromNodeId());
				bo.setToNodeId(edge.getToNodeId());
				edgeBos.add(bo);
			}
		}
		return validate(nodeBos, edgeBos);
	}

	private <N, E> GraphValidationResultVO validate(List<N> nodes, List<E> edges) {
		// DAG 基础校验（唯一性/引用合法/无环）
		List<GraphValidationErrorVO> errors = new ArrayList<>();
		if (CollectionUtils.isEmpty(nodes)) {
			errors.add(buildError("NODE_EMPTY", "节点不能为空", null, null));
			return buildResult(errors);
		}
		Map<Long, Integer> inDegree = new HashMap<>();
		Map<Long, List<Long>> adjacency = new HashMap<>();
		Set<Long> nodeIds = new HashSet<>();
		for (N node : nodes) {
			Long nodeId = extractNodeId(node);
			if (nodeId == null) {
				errors.add(buildError("NODE_ID_EMPTY", "节点ID不能为空", null, null));
				continue;
			}
			if (!nodeIds.add(nodeId)) {
				errors.add(buildError("NODE_ID_DUP", "节点ID重复", nodeId, null));
				continue;
			}
			inDegree.put(nodeId, 0);
			adjacency.put(nodeId, new ArrayList<Long>());
		}
		if (!CollectionUtils.isEmpty(edges)) {
			for (E edge : edges) {
				Long fromId = extractEdgeFrom(edge);
				Long toId = extractEdgeTo(edge);
				Long edgeId = extractEdgeId(edge);
				if (fromId == null || toId == null) {
					errors.add(buildError("EDGE_NODE_EMPTY", "依赖边节点不能为空", null, edgeId));
					continue;
				}
				if (!nodeIds.contains(fromId) || !nodeIds.contains(toId)) {
					errors.add(buildError("EDGE_NODE_NOT_FOUND", "依赖边引用节点不存在", null, edgeId));
					continue;
				}
				adjacency.get(fromId).add(toId);
				inDegree.put(toId, inDegree.get(toId) + 1);
			}
		}
		if (!errors.isEmpty()) {
			return buildResult(errors);
		}
		Deque<Long> queue = new ArrayDeque<Long>();
		for (Map.Entry<Long, Integer> entry : inDegree.entrySet()) {
			if (entry.getValue() == 0) {
				queue.add(entry.getKey());
			}
		}
		int processed = 0;
		while (!queue.isEmpty()) {
			Long id = queue.poll();
			processed++;
			List<Long> next = adjacency.get(id);
			if (next == null) {
				continue;
			}
			for (Long toId : next) {
				int degree = inDegree.get(toId) - 1;
				inDegree.put(toId, degree);
				if (degree == 0) {
					queue.add(toId);
				}
			}
		}
		if (processed != nodeIds.size()) {
			errors.add(buildError("EDGE_CYCLE", "存在环路", null, null));
		}
		GraphValidationResultVO result = buildResult(errors);
		if (!result.isValid()) {
			log.warn("图校验失败, errorCount={}", errors.size());
		}
		return result;
	}

	private GraphValidationResultVO buildResult(List<GraphValidationErrorVO> errors) {
		GraphValidationResultVO result = new GraphValidationResultVO();
		result.setErrors(errors);
		result.setValid(errors == null || errors.isEmpty());
		return result;
	}

	private GraphValidationErrorVO buildError(String code, String message, Long nodeId, Long edgeId) {
		GraphValidationErrorVO error = new GraphValidationErrorVO();
		error.setCode(code);
		error.setMessage(message);
		error.setNodeId(nodeId);
		error.setEdgeId(edgeId);
		return error;
	}

	private Long extractNodeId(Object node) {
		if (node instanceof ProcessNodeBo) {
			return ((ProcessNodeBo) node).getId();
		}
		if (node instanceof StepNodeBo) {
			return ((StepNodeBo) node).getId();
		}
		return null;
	}

	private Long extractEdgeId(Object edge) {
		if (edge instanceof ProcessEdgeBo) {
			return ((ProcessEdgeBo) edge).getId();
		}
		if (edge instanceof StepEdgeBo) {
			return ((StepEdgeBo) edge).getId();
		}
		return null;
	}

	private Long extractEdgeFrom(Object edge) {
		if (edge instanceof ProcessEdgeBo) {
			return ((ProcessEdgeBo) edge).getFromNodeId();
		}
		if (edge instanceof StepEdgeBo) {
			return ((StepEdgeBo) edge).getFromNodeId();
		}
		return null;
	}

	private Long extractEdgeTo(Object edge) {
		if (edge instanceof ProcessEdgeBo) {
			return ((ProcessEdgeBo) edge).getToNodeId();
		}
		if (edge instanceof StepEdgeBo) {
			return ((StepEdgeBo) edge).getToNodeId();
		}
		return null;
	}
}
