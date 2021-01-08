package com.rjh.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller //view를 리턴 
public class indexController {
	
	@GetMapping({"","/"})
	public String index() {
		//머스테치 src/main/resources/ 가 기본 설정
		//뷰 리졸버 설정: templates (prefix), .mustache(suffix)
		return "index"; //src/main/resources/templates/index.mustache로 이동 
	}
}
