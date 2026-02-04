package com.qjyy.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import com.qjyy.common.security.annotation.EnableCustomConfig;
import com.qjyy.common.security.annotation.EnableRyFeignClients;
import com.qjyy.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author qjyy
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@ComponentScan(basePackages = "com.qjyy")
@EnableCaching
public class AspBaseServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AspBaseServiceApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  asp-base基础数据模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" + " .-------.       ____     __        \n"
				+ " |  _ _   \\      \\   \\   /  /    \n" + " | ( ' )  |       \\  _. /  '       \n"
				+ " |(_ o _) /        _( )_ .'         \n" + " | (_,_).' __  ___(_ o _)'          \n"
				+ " |  |\\ \\  |  ||   |(_,_)'         \n" + " |  | \\ `'   /|   `-'  /           \n"
				+ " |  |  \\    /  \\      /           \n" + " ''-'   `'-'    `-..-'              ");
	}
}
