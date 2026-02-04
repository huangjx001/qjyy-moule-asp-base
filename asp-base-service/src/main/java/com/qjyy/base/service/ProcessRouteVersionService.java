package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProcessRouteVersionCopyBo;
import com.qjyy.base.domain.bo.ProcessRouteVersionSaveBo;
import com.qjyy.base.domain.bo.ProcessRouteVersionUpdateBo;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessRouteVersionVO;

public interface ProcessRouteVersionService {

	Long create(ProcessRouteVersionSaveBo bo);

	boolean update(Long id, ProcessRouteVersionUpdateBo bo);

	ProcessRouteVersionVO get(Long id);

	List<ProcessRouteVersionVO> listByRouteId(Long routeId);

	Long copy(Long sourceVersionId, ProcessRouteVersionCopyBo bo);

	GraphValidationResultVO release(Long versionId);
}
