package com.rasimenezle.bankingSystemProject.dtoResponse;

import com.rasimenezle.bankingSystemProject.entity.AccountEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountResponseDto {
	private boolean success;
	private String message;
	private AccountEntity account;

}
