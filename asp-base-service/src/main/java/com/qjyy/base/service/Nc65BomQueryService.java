package com.qjyy.base.service;

import com.qjyy.base.domain.vo.Nc65BomTreeVO;

public interface Nc65BomQueryService {

    /**
     * 根据物料编码查询BOM树：多版本->子项->替代料
     */
    Nc65BomTreeVO queryBomTree(String mlId, Boolean onlyDefault, Boolean includeRepl);
}
