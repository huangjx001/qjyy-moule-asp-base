package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProcessRouteSaveBo;
import com.qjyy.base.domain.vo.ProcessRouteVO;

public interface ProcessRouteService {

	Long create(ProcessRouteSaveBo bo);

	boolean update(Long id, ProcessRouteSaveBo bo);

	ProcessRouteVO get(Long id);

	List<ProcessRouteVO> listAll();
}
