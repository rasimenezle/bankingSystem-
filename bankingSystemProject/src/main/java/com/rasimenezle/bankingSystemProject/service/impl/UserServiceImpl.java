package com.rasimenezle.bankingSystemProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rasimenezle.bankingSystemProject.dtoRequest.SignupRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.UserEnableDtoRequest;
import com.rasimenezle.bankingSystemProject.entity.UserEntity;
import com.rasimenezle.bankingSystemProject.repository.UserRepository;
import com.rasimenezle.bankingSystemProject.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserEntity findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void userSave(SignupRequestDto user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserName(user.getUserName());
		userEntity.setEnabled(false);
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
		userEntity.setAuthorities("CREATE_ACCOUNT");
		this.userRepository.saveUser(userEntity);

	}

	@Override
	public UserEntity findByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	@Override
	public void userUpdateEnable(int id ,UserEnableDtoRequest userEnableDtoRequest) {
		UserEntity user=userRepository.findByUserId(id);
		
		user.setEnabled(userEnableDtoRequest.isEnabled());
		this.userRepository.updateEnable(id, user.isEnabled());
		
	}

	@Override
	public UserEntity findByUserId(int id) {
		
		return userRepository.findByUserId(id);
	}

}
