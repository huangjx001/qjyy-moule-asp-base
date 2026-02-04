package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProcessEdgeBo;
import com.qjyy.base.domain.bo.ProcessNodeBo;
import com.qjyy.base.domain.bo.StepEdgeBo;
import com.qjyy.base.domain.bo.StepNodeBo;
import com.qjyy.base.domain.entity.ProcessEdge;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.vo.GraphValidationResultVO;

public interface GraphValidationService {

	GraphValidationResultVO validateProcessGraph(List<ProcessNodeBo> nodes, List<ProcessEdgeBo> edges);

	GraphValidationResultVO validateStepGraph(List<StepNodeBo> nodes, List<StepEdgeBo> edges);

	GraphValidationResultVO validateProcessEntities(List<ProcessNode> nodes, List<ProcessEdge> edges);

	GraphValidationResultVO validateStepEntities(List<StepNode> nodes, List<StepEdge> edges);
}
