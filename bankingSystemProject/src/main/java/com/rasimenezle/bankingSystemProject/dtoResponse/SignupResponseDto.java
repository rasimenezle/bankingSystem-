package com.rasimenezle.bankingSystemProject.dtoResponse;

import com.rasimenezle.bankingSystemProject.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponseDto {
	private boolean success;
	private String message;
	private UserEntity user;

}
