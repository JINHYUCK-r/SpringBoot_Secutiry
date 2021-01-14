package com.rjh.security1.model;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private String role; //ROLE_USER, ROLE_ADMIN
	private String provider; //회원가입주체. google...
	private String providerId; //어트리뷰트 정보의 sub 
	
	@CreationTimestamp
	private Timestamp creatDate;

	@Builder
	public User(int id, String username, String password, String email, String role, String provider, String providerId,
			Timestamp creatDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.creatDate = creatDate;
	}

	
	
}
