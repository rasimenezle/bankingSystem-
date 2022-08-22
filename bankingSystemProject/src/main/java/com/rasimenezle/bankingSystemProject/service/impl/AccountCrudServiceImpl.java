package com.rasimenezle.bankingSystemProject.service.impl;

import java.sql.Timestamp;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rasimenezle.bankingSystemProject.currency.CurrencyExchangeUnit;
import com.rasimenezle.bankingSystemProject.dtoRequest.CreateAccountDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoRequest.DepositAccountRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.TransferAccountDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoResponse.GenericResponseDto;
import com.rasimenezle.bankingSystemProject.entity.AccountEntity;
import com.rasimenezle.bankingSystemProject.entity.AccountsType;
import com.rasimenezle.bankingSystemProject.entity.CustomUserEntity;
import com.rasimenezle.bankingSystemProject.repository.AccountRepository;
import com.rasimenezle.bankingSystemProject.service.AccountCrudService;

@Service
public class AccountCrudServiceImpl implements AccountCrudService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CurrencyExchangeUnit exchange;

	@Autowired
	KafkaTemplate<String, String> sender;

	@Override
	public AccountEntity saveAccount(CreateAccountDtoRequest createAccountDtoRequest) {

		CustomUserEntity user = (CustomUserEntity) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AccountEntity accountEntity = new AccountEntity();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Random rand = new Random();
		int num = rand.nextInt(900000000) + 1000000000;

		accountEntity.setCreationDate(time);
		accountEntity.setLastUpdateDate(time);
		accountEntity.setBankId(createAccountDtoRequest.getBankId());
		accountEntity.setBalance(0);
		accountEntity.setType(Enum.valueOf(AccountsType.class, createAccountDtoRequest.getType()));
		accountEntity.setUserId(user.getId());
		accountEntity.setDeleted(false);
		accountEntity.setAccountNumber(num);

		accountRepository.saveAccount(accountEntity);
		return accountRepository.findByAccountNumber(num);

	}

	@Override
	public AccountEntity findByAccountNumber(int accountNumber) {

		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Override
	public void deleteByAccountNumber(int accountNumber) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		accountRepository.deleteByAccountNumber(accountNumber, true, time);
	}

	@Override
	public void depositByAccountNumber(int accountNumber, DepositAccountRequestDto depositAccountRequestDto) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
		accountEntity.setBalance(accountEntity.getBalance() + depositAccountRequestDto.getAmount());
		accountEntity.setLastUpdateDate(time);
		this.accountRepository.depositByAccountNumber(accountNumber, accountEntity.getBalance(),
				accountEntity.getLastUpdateDate());

		String message=accountNumber+","+depositAccountRequestDto.getAmount()+" :deposited ";
		sender.send("logs", message);
	}

	@Override
	public GenericResponseDto transferAmount(int accountNumber, TransferAccountDtoRequest transferAccountDtoRequest) {
		AccountEntity senderAccount = accountRepository.findByAccountNumber(accountNumber);
		AccountEntity receiverAccount = accountRepository
				.findByAccountNumber(transferAccountDtoRequest.getReceiverAccountNumber());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String result = "Insufficient balance";

		double amount = 0;

		if ((senderAccount.getBalance() > 0) && (transferAccountDtoRequest.getAmount() <= senderAccount.getBalance())) {
			if (senderAccount.getBankId() != receiverAccount.getBankId()) {
				if (senderAccount.getType() == AccountsType.TRY) {
					senderAccount.setBalance(senderAccount.getBalance() - 3);
				} else if (senderAccount.getType() == AccountsType.USD) {
					senderAccount.setBalance(senderAccount.getBalance() - 1);
				}
			}
			senderAccount.setBalance(senderAccount.getBalance() - transferAccountDtoRequest.getAmount());
			senderAccount.setLastUpdateDate(time);
			result = "Transferred Successfully";
			accountRepository.depositByAccountNumber(accountNumber, senderAccount.getBalance(),
					senderAccount.getLastUpdateDate());
		}
		if (senderAccount.getType().equals(receiverAccount.getType())) {
			amount = transferAccountDtoRequest.getAmount();
		} else if (senderAccount.getType().equals(AccountsType.TRY)
				&& receiverAccount.getType().equals(AccountsType.USD)) {

			amount = (transferAccountDtoRequest.getAmount()) / exchange.currencyChange(receiverAccount.getBalance(),
					senderAccount.getType().toString(), receiverAccount.getType().toString());
		} else if (senderAccount.getType().equals(AccountsType.TRY)
				&& receiverAccount.getType().equals(AccountsType.GAU)) {

			amount = (transferAccountDtoRequest.getAmount()) / exchange.buyingGramGAU(receiverAccount.getBalance(),
					senderAccount.getType().toString(), receiverAccount.getType().toString());
		} else if (senderAccount.getType().equals(AccountsType.USD)
				&& receiverAccount.getType().equals(AccountsType.TRY)) {

			amount = (transferAccountDtoRequest.getAmount()) * exchange.currencyChange(receiverAccount.getBalance(),
					senderAccount.getType().toString(), receiverAccount.getType().toString());
		} else if (senderAccount.getType().equals(AccountsType.GAU)
				&& receiverAccount.getType().equals(AccountsType.TRY)) {

			amount = (transferAccountDtoRequest.getAmount()) * exchange.sellingGramGAU(receiverAccount.getBalance(),
					senderAccount.getType().toString(), receiverAccount.getType().toString());
		} else if (senderAccount.getType().equals(AccountsType.GAU)
				&& receiverAccount.getType().equals(AccountsType.USD)) {
			double exchanger = (transferAccountDtoRequest.getAmount()) * exchange.sellingGramGAU(
					receiverAccount.getBalance(), senderAccount.getType().toString(), AccountsType.TRY.toString());
			amount = exchanger / (exchange.currencyChange(receiverAccount.getBalance(), AccountsType.TRY.toString(),
					receiverAccount.getType().toString()));
		} else {
			double exchanger = (transferAccountDtoRequest.getAmount()) * exchange.currencyChange(
					receiverAccount.getBalance(), senderAccount.getType().toString(), AccountsType.TRY.toString());
			amount = exchanger / exchange.buyingGramGAU(receiverAccount.getBalance(),
					senderAccount.getType().toString(), receiverAccount.getType().toString());
		}

		receiverAccount.setBalance(receiverAccount.getBalance() + amount);
		receiverAccount.setLastUpdateDate(time);
		accountRepository.depositByAccountNumber(receiverAccount.getAccountNumber(), receiverAccount.getBalance(),
				receiverAccount.getLastUpdateDate());

		String message = transferAccountDtoRequest.getAmount() + "," + accountNumber + " to "
				+ transferAccountDtoRequest.getReceiverAccountNumber() + " :transferred";

		sender.send("logs", message);
		return GenericResponseDto.builder().success(true).message(result).build();

	}
}
