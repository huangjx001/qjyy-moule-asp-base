package com.qjyy.base.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qjyy.base.domain.bo.AnnualDemandQueryBo;
import com.qjyy.base.domain.vo.AnnualDemandVO;

public interface AnnualDemandService {

	IPage<AnnualDemandVO> page(AnnualDemandQueryBo bo);

	List<AnnualDemandVO> list(AnnualDemandQueryBo bo);
}
