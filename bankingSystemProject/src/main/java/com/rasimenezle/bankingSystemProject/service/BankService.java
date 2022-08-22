package com.rasimenezle.bankingSystemProject.service;

import com.rasimenezle.bankingSystemProject.entity.BankEntity;
import com.rasimenezle.bankingSystemProject.entity.CreateBankRequestDto;


public interface BankService {
	
	public void createBank(CreateBankRequestDto bank);
	public BankEntity findByBankName(String bankName);
}
