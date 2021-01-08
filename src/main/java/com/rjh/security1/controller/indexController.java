package com.rjh.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view를 리턴
public class indexController {

	@GetMapping({ "", "/" })
	public String index() {
		// 머스테치 src/main/resources/ 가 기본 설정
		// 뷰 리졸버 설정: templates (prefix), .mustache(suffix)
		return "index"; // src/main/resources/templates/index.mustache로 이동
	}

	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}

	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}

	//스프링시큐리티가 해당 주소는 낚아챔  -> SecurityConfig 설정으로 막을수 잇음 
	@GetMapping("/login")
	public @ResponseBody String login() {
		return "login";
	}
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료";
	}

}
