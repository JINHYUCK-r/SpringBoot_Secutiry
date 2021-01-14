package com.rjh.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rjh.security1.model.User;
import com.rjh.security1.repository.UserRepository;

@Controller // view를 리턴
public class indexController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

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
	@GetMapping("/loginForm")
	public  String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public  String joinForm() {
		return "joinForm";
	}
	
	
	@PostMapping("/join")
	public  String join(User user) {
		//System.out.println(user); 
		user.setRole("ROLE_USER");
		String rowPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rowPassword);
		user.setPassword(encPassword);
		userRepository.save(user); //회원가입 . 비밀번호 보안이 안됨 => 시큐리티로 로그인을 할수없음. 패스워드 암호화가 안되어 있기 때문 
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") //SecurityConfig	에서 설정한 @EnableGlobalMethodSecurity  로 간단하게 설정할수 있음 
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") ////prePostEnabled로 활성화. 밑에서 정의한 data()가 실행되기 직전에 실행됨  
	//@PostAuthorize :함수가 끝나고 난뒤 권한설
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터";
	}
	
}
