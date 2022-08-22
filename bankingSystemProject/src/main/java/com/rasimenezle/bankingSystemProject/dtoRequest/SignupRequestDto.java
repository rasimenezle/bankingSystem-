package com.rasimenezle.bankingSystemProject.dtoRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {
	private String userName;
	private String email;
	private String password;
	

}
