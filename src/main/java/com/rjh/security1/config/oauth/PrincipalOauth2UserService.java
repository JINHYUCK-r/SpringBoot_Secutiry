package com.rjh.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.rjh.security1.config.auth.PrincipalDetails;
import com.rjh.security1.config.oauth.provider.FacebookUserInfo;
import com.rjh.security1.config.oauth.provider.GoogleUserInfo;
import com.rjh.security1.config.oauth.provider.NaverUserInfo;
import com.rjh.security1.config.oauth.provider.OAuth2UserInfo;
import com.rjh.security1.model.User;
import com.rjh.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	//구글 로그인 버튼 -> 구글로그인창 -> 로그인 완료 -> code리턴 -> OAuth-Client라이브러리 ->Access 토큰 요청  : userRequest 정보
	//loadUser: userRequest 정보에서 회원 프로필을받아올수 있음 
	
	//구글로 부터받은 userRequest데이터에 대한 후처리가 진행되는 함수 
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());  //registrationId : 어떤 OAuth로 로그인했는가 
		System.out.println("getAccessToken : " + userRequest.getAccessToken());	
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		System.out.println("getAttributes : " + oauth2User.getAttributes()); //사용자의 정보가 담겨있음 
		//sub : 구글의 프라이머리키 
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes()); //로그인 요청시 받은 정보를 우리가 만든 클래스에 담는다 
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes()); 
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map<String, Object>)oauth2User.getAttributes().get("response"));
			//우리가 yml에서 response라고 등록한 attributes안의 response에 우리가 필요한 값이 있기때문에 이렇게받아와야한다. 
		}else {
			System.out.println("우리는 구글과 페이스북, 네이버만  지원해요");
		}
		/* 유저정보를 담는 클래스를 나누어서 만들지않고 구글만 했을 경우에는 이렇게받아도 된다.
		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		String providerId = oauth2User.getAttribute("sub"); //facebook일때는 sub값이 없기때문에 null이 됨 
		String username = provider + "_" + providerId; //google_~~~~
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		*/
		
		String provider = oAuth2UserInfo.getProvier();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId; 
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);		
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes()); //리턴타입이 OAuth2User이기때문에 우리가 extends 해놓음 
	}

}
