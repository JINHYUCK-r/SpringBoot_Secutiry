package com.rjh.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rjh.security1.config.auth.PrincipalDetails;
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

	//OAuth로그인을 해도 PrincipalDetails로 받을수 있고
	//일반로그인을 해도 PrincipalDetails로 받을수 있다. 
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails ) {
		System.out.println("principalDetails : " + principalDetails.getUser());
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
	//시큐리티 세션안에는 Authentication 객체만 존재할수있다. 객체가 생성됐을때 로그인이 된것.  Authentication이 가지고 있을수 있는 타입은
	//UserDetails(일반적로그인 ), OAuth2User(OAuth를 통한 로그인 )만 가능. 각각의 상황에 맞게 만들기가 복잡하기 때문에 두개를 상속받는 하나의 클래스를 만들어서 사용 
	
	//일반적인 로그인 정보 받기 
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, // 1번 
			@AuthenticationPrincipal PrincipalDetails userDetails) { //DI(의존성주입)  , @AuthenticationPrincipal를 통해서 세션정보에 접근 가능. 2번 
		//원래는 UserDetails userDetails로 받아야 하지만  PrincipalDetails 타입으로 받아도됨.  왜냐하면 UserDetails로 extends했기 때문 
		System.out.println("/test/login=========");
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();//authentication.getPrincipal() 리턴타입: 오브젝트. 1번 
		//구글로 로그인 하려고하면 오류가 발생함 PrincipalDetails 캐스팅이 안됨 
		System.out.println("authentication : " + principalDetails.getUser()); //유저의 정보를 받아올수 있음 
		
		System.out.println("userDeatails : " + userDetails.getUser()); // 2번
		//1번을 통해서 다운캐스팅을 통해서 User오브젝트를 찾을수도 있고 ,2번을 통해 User를 찾을수도 있다. 
		return "세션 정보 확인하기 ";
		
	}
	
	//OAuth2를 이용한 로그인정보 받기 
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication, //1번 
			@AuthenticationPrincipal OAuth2User oauth) { //2번 
		System.out.println("/test/oauth/login=========");
		OAuth2User oauth2User = (OAuth2User)authentication.getPrincipal();  //1번 
		//구글로 로그인해도 캐스팅에 오류가 생기지 않음 
		System.out.println("authentication : " + oauth2User.getAttributes());  
		
		System.out.println("oauth2User: "+ oauth.getAttributes()); // 2번 
	
		
		return "OAuth 세션 정보 확인하기 ";
	}
	
	
}
