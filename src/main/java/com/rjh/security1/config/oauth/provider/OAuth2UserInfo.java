package com.rjh.security1.config.oauth.provider;

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvier();
	String getEmail();
	String getName();
}
