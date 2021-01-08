package com.rjh.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //IoC등록 
public class WebMvcConfig implements WebMvcConfigurer{
	
	//머스테치 재설정 
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		MustacheViewResolver resolver = new MustacheViewResolver();
		resolver.setCharset("UTF-8");
		resolver.setContentType("text/html; charset=UTF-8");
		resolver.setPrefix("classpath:/templates/"); //classpath 는 프로젝트 경로를 의미 
		resolver.setSuffix(".html");
		
		registry.viewResolver(resolver);
	}

}
