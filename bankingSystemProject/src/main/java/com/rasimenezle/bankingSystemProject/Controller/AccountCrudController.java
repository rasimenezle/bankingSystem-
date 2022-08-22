package com.rasimenezle.bankingSystemProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasimenezle.bankingSystemProject.dtoRequest.CreateAccountDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoRequest.DepositAccountRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.TransferAccountDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoResponse.CreateAccountResponseDto;
import com.rasimenezle.bankingSystemProject.dtoResponse.DepositAccountResponseDto;
import com.rasimenezle.bankingSystemProject.dtoResponse.GenericResponseDto;
import com.rasimenezle.bankingSystemProject.entity.AccountEntity;
import com.rasimenezle.bankingSystemProject.entity.AccountsType;
import com.rasimenezle.bankingSystemProject.entity.CustomUserEntity;
import com.rasimenezle.bankingSystemProject.service.AccountCrudService;

@RestController
@RequestMapping(path = "banking")
public class AccountCrudController {

	@Autowired
	AccountCrudService accountCrudService;
	
	
	@GetMapping(path = "/profile")
	public String welcome() {
		return "Welcome";
	}

	// Todo : create Account değişecek path değeri
	@PostMapping(path = "/createAccounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAccount(@RequestBody CreateAccountDtoRequest accountCreateRequest) {
		boolean typeControl = accountCreateRequest.getType().equals(AccountsType.GAU.toString())
				|| accountCreateRequest.getType().equals(AccountsType.USD.toString())
				|| accountCreateRequest.getType().equals(AccountsType.TRY.toString());

		if (typeControl) {
			AccountEntity accountEntity = accountCrudService.saveAccount(accountCreateRequest);

			return new ResponseEntity<>(CreateAccountResponseDto.builder().success(true).message("Account Created")
					.account(accountEntity).build(), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(
					GenericResponseDto.builder()
							.message("Invalid Account Type: " + accountCreateRequest.getType().toString()).build(),
					HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping(path = "/accounts/{accountNumber}")
	public ResponseEntity<?> detailAccount(@PathVariable(name = "accountNumber") int accountNumber) {
		CustomUserEntity user = (CustomUserEntity) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AccountEntity accountEntity = accountCrudService.findByAccountNumber(accountNumber);
		if (user.getId() != accountEntity.getUserId()) {
			return new ResponseEntity<>(GenericResponseDto.builder().message("Access Denied").build(),
					HttpStatus.FORBIDDEN);
		}
		if (accountNumber == accountEntity.getAccountNumber()) {
			return ResponseEntity.ok().lastModified(accountEntity.getLastUpdateDate().getTime()).body(accountEntity);
		} else {
			return new ResponseEntity<>(CreateAccountResponseDto.builder().account(accountEntity).build(),
					HttpStatus.NOT_FOUND);
		}

	}

	// Delete account Controller
	@DeleteMapping(path = "/accounts/{accountNumber}")
	public ResponseEntity<?> delete(@PathVariable int accountNumber) {
		CustomUserEntity user = (CustomUserEntity) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		AccountEntity accountEntity = accountCrudService.findByAccountNumber(accountNumber);

		if (user.getId() != accountEntity.getUserId()) {
			return new ResponseEntity<>(GenericResponseDto.builder().message("Access Denied").build(),
					HttpStatus.FORBIDDEN);
		}
		if (accountEntity.isDeleted() == true) {
			return new ResponseEntity<>(
					GenericResponseDto.builder().success(false).message("Account has been deleted before").build(),
					HttpStatus.BAD_REQUEST);
		} else {
			accountCrudService.deleteByAccountNumber(accountNumber);

			return new ResponseEntity<>(GenericResponseDto.builder().success(true).message("Account Deleted").build(),
					HttpStatus.OK);
		}

	}

	@PatchMapping(path = "/accounts/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> depositAccountBalance(@PathVariable(name = "accountNumber") int accountNumber,
			@RequestBody DepositAccountRequestDto depositAccountRequestDto) {

		CustomUserEntity user = (CustomUserEntity) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AccountEntity accountEntity = accountCrudService.findByAccountNumber(accountNumber);

		if (accountEntity != null && accountEntity.isDeleted() != true) {
			if (accountEntity.getUserId() != user.getId()) {
				return new ResponseEntity<>(GenericResponseDto.builder().message("Access Denied").build(),
						HttpStatus.FORBIDDEN);

			}
			accountCrudService.depositByAccountNumber(accountNumber, depositAccountRequestDto);
			AccountEntity accountUpdate = accountCrudService.findByAccountNumber(accountNumber);
			return new ResponseEntity<>(DepositAccountResponseDto.builder().success(true).message("Success")
					.balance(accountUpdate.getBalance()).build(), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(GenericResponseDto.builder().success(false).message("Invalid Type").build(),
					HttpStatus.BAD_REQUEST);

		}
	}
	
	@PatchMapping(path="/accounts/transfer/{senderAccountNumber}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> transferAmount(@PathVariable(name="senderAccountNumber") int senderAccountNumber,@RequestBody TransferAccountDtoRequest transferAccountDtoRequest){
		
		CustomUserEntity user = (CustomUserEntity) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AccountEntity accountEntity = accountCrudService.findByAccountNumber(senderAccountNumber);
		if (accountEntity != null && accountEntity.isDeleted() != true) {
		if (accountEntity.getUserId() != user.getId()) {
			return new ResponseEntity<>(GenericResponseDto.builder().message("Access Denied").build(),
					HttpStatus.FORBIDDEN);

		}
		return ResponseEntity.ok(accountCrudService.transferAmount(senderAccountNumber, transferAccountDtoRequest));
	}else {
		return new ResponseEntity<>(GenericResponseDto.builder().success(false).message("Invalid Type").build(),
				HttpStatus.BAD_REQUEST);

	}}

}
