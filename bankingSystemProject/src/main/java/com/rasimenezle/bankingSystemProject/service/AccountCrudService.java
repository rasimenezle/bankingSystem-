package com.rasimenezle.bankingSystemProject.service;

import com.rasimenezle.bankingSystemProject.dtoRequest.CreateAccountDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoRequest.DepositAccountRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.TransferAccountDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoResponse.GenericResponseDto;
import com.rasimenezle.bankingSystemProject.entity.AccountEntity;

public interface AccountCrudService {
	public AccountEntity saveAccount(CreateAccountDtoRequest createAccountDtoRequest);

	public AccountEntity findByAccountNumber(int accountNumber);
	
	public void deleteByAccountNumber(int accountNumber);
	public void depositByAccountNumber(int accountNumber,DepositAccountRequestDto depositAccountRequestDto);

	public GenericResponseDto transferAmount(int accountNumber,TransferAccountDtoRequest transferAccountDtoRequest);

}
