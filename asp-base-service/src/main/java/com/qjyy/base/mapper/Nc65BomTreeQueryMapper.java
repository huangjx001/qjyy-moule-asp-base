package com.qjyy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qjyy.base.domain.bo.Nc65BomFlatRow;

public interface Nc65BomTreeQueryMapper {

    List<Nc65BomFlatRow> selectBomFlatRows(@Param("mlId") String mlId,
                                          @Param("onlyDefault") Boolean onlyDefault,
                                          @Param("includeRepl") Boolean includeRepl);
}
