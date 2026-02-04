package com.qjyy.base.service;

import com.qjyy.base.domain.bo.StepGraphSaveBo;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.StepGraphVO;

public interface StepGraphService {

	StepGraphVO getGraph(Long resourceRoomId);

	boolean saveGraph(StepGraphSaveBo bo);

	GraphValidationResultVO validateGraph(StepGraphSaveBo bo);
}
