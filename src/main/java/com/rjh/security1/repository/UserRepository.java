package com.rjh.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rjh.security1.model.User;

//CRUD함수를 japrepository가 들고있음
//@repository를 쓰지않아도 상속되어서 자동으로 IoC가 됨 
public interface UserRepository extends JpaRepository<User, Integer>{

}
