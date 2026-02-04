package com.qjyy.base.api;

import org.springframework.cloud.openfeign.FeignClient;

import com.qjyy.base.api.fallback.BaseApiFallbackFactory;
import com.qjyy.base.enums.ApiConstants;

import io.swagger.v3.oas.annotations.tags.Tag;

@FeignClient(contextId = "baseApi", name = ApiConstants.NAME, value = ApiConstants.NAME, fallbackFactory = BaseApiFallbackFactory.class)
@Tag(name = "RPC 服务 -文件")
public interface BaseApi {

	String PREFIX = ApiConstants.PREFIX + "/base";
}
