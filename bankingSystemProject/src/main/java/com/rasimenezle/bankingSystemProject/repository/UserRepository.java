package com.rasimenezle.bankingSystemProject.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import com.rasimenezle.bankingSystemProject.entity.UserEntity;

@Mapper
public interface UserRepository {

	@Transactional
	@Insert("Insert into User (userName,email,password,enabled,authorities) Values(#{userName},#{email},#{password},#{enabled},#{authorities})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveUser(UserEntity user);

	@Transactional
	@Select("SELECT * FROM user WHERE userName=#{userName} ")
	public UserEntity findByUsername(String username);

	@Transactional
	@Select("SELECT * FROM user WHERE email=#{email}")
	public UserEntity findByEmail(String email);

	@Transactional
	@Update("update user set enabled=#{enabled} where id=#{id}")
	public void updateEnable(int id, boolean enabled);

	@Transactional
	@Select("select * from user where id = #{id}")
	public UserEntity findByUserId(int id);
}

	
