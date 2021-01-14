package com.rjh.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration 	// 메모리에 띄우기 
@EnableWebSecurity  //스프링 시큐리티 필터가 스프링 필터 체인에 등록됨 
@EnableGlobalMethodSecurity(securedEnabled =  true, prePostEnabled = true) //securedEnabled: secured 어노테이션 활성화,  prePostEnabled: PreAuthorize,PostAuthorize 활성화 
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean //해당 메소드의 리턴되는 오브젝트를 IoC로 등록해줌 
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() //인증만 되면들어갈수있는 주소 
			//.antMatchers("/manager/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm") //인증이 필요하면 이쪽으로 이동  
			.loginProcessingUrl("/login") // /login주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌. 컨트롤러에 /login을 만들필요가 없음
			.defaultSuccessUrl("/") // 기본이동주소는 / 이지만 특정 주소를 요청해서 로그인하게 되면 그 주소로 바로 이동시켜 줌 
			.and()
			.oauth2Login()
			.loginPage("/loginForm"); //구글 로그인창으로 이동됨 . 구글로그인이 완료된 뒤 후처리가 필요  
	}
	
}
