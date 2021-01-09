package com.rjh.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rjh.security1.model.User;
import com.rjh.security1.repository.UserRepository;

//시큐리티 설정에서  loginProcessingUrl("/login")요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername가 호출됨 

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	//	시큐리티session( <= Authentication (<= UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

	
}
