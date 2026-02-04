package com.qjyy.base.api.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.qjyy.base.api.BaseApi;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BaseApiFallbackFactory implements FallbackFactory<BaseApi> {

	@Override
	public BaseApi create(Throwable cause) {
		log.error("BaseApi调用失败，进入fallback，异常信息：", cause);
		return new BaseApi() {

		};
	}
}
