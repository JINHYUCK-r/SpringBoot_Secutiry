package com.rjh.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.rjh.security1.model.User;

import lombok.Data;

//시큐리티가 /login 주소를 낚아채서 로그인을 행시킴
//로그인을 진행이 완료되면 시큐리티 session을 만들어줌 (Security ContextHolder)
//오브젝트가 정해져 있음. Authentication 타입 객체 
//Authentication 안에 User 정보가 있어야함
//User 오브젝트타입 => UserDetail 타입 객체 
//Security Session => Authentication => UserDetails(PrincipalDetail)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user; 
	private Map<String, Object> attributes;
	
	//일반로그인 사용 생성자 
	public PrincipalDetails (User user) {
		this.user = user;
	}
	//OAuth 사용 생성자 
	public PrincipalDetails (User user, Map<String, Object> attributes ) {
		this.user = user;
		this.attributes = attributes;
	}
	

	//해당 유저의권한을 리턴 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
			return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		//우리사이트에서 1년동안 회원이 로그인을 안하면 휴면계정
		//User 모델에 로그인 데이터를 만들어주어야함 user.getLoginDate()
		//현재 시간 - 로그인 시간 -> 1년을 초과하면 false로 바꿀수 있음 
		
		return true;
	}

	//OAuth2User 를 추가해서 생성된 메소드 
	
	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}
	
	
	
}
