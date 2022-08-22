package com.rasimenezle.bankingSystemProject.dtoResponse;

import com.rasimenezle.bankingSystemProject.entity.BankEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBankResponseDto {

	private boolean success;
	private String message;
	private BankEntity bank;
}
