package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProcessRouteProductSaveBo;
import com.qjyy.base.domain.vo.ProcessRouteProductVO;
import com.qjyy.base.domain.vo.ProcessRouteVO;

public interface ProcessRouteProductService {

	Long create(ProcessRouteProductSaveBo bo);

	boolean update(Long id, ProcessRouteProductSaveBo bo);

	boolean delete(Long id);

	List<ProcessRouteProductVO> list(String productCode);

	ProcessRouteVO resolveRoute(String productCode);
}
