package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProcessGraphSaveBo;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessGraphVO;
import com.qjyy.base.domain.vo.ProcessNodeSummaryVO;

public interface ProcessGraphService {

	ProcessGraphVO getGraph(Long routeVersionId);

	boolean saveGraph(ProcessGraphSaveBo bo);

	GraphValidationResultVO validateGraph(ProcessGraphSaveBo bo);

	List<ProcessNodeSummaryVO> listNodeSummary(Long routeVersionId);
}
