package com.rasimenezle.bankingSystemProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rasimenezle.bankingSystemProject.entity.BankEntity;
import com.rasimenezle.bankingSystemProject.entity.CreateBankRequestDto;
import com.rasimenezle.bankingSystemProject.repository.BankRepository;
import com.rasimenezle.bankingSystemProject.service.BankService;

@Service
public class BankServiceImpl implements BankService {

	
	private BankRepository bankRepository;
	
	@Autowired
	public BankServiceImpl(BankRepository bankRepository) {
		
		this.bankRepository = bankRepository;
	}


	@Override
	public void createBank(CreateBankRequestDto bank) {
		BankEntity bankEntity=new BankEntity();
		bankEntity.setName(bank.getName());
		bankRepository.createBank(bankEntity);
	}


	@Override
	public BankEntity findByBankName(String bankName) {
		return this.bankRepository.findByBankName(bankName);
	}

}
