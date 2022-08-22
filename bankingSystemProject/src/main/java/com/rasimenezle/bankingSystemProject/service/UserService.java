package com.rasimenezle.bankingSystemProject.service;

import com.rasimenezle.bankingSystemProject.dtoRequest.SignupRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.UserEnableDtoRequest;
import com.rasimenezle.bankingSystemProject.entity.UserEntity;

public interface UserService {
	public UserEntity findByUsername(String username);

	public void userSave(SignupRequestDto user);

	public UserEntity findByEmail(String email);

	public void userUpdateEnable(int id ,UserEnableDtoRequest userEnableDtoRequest);
	
	public UserEntity findByUserId(int id);
}