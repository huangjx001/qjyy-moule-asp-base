package com.qjyy.base.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
@Slf4j
public class BaseApiImpl implements BaseApi {


}
