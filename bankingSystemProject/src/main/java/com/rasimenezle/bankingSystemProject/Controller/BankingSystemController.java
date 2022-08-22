package com.rasimenezle.bankingSystemProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasimenezle.bankingSystemProject.dtoResponse.CreateBankResponseDto;
import com.rasimenezle.bankingSystemProject.dtoResponse.GenericResponseDto;
import com.rasimenezle.bankingSystemProject.entity.BankEntity;
import com.rasimenezle.bankingSystemProject.entity.CreateBankRequestDto;
import com.rasimenezle.bankingSystemProject.service.BankService;

@RestController
@RequestMapping(path = "/banking")
public class BankingSystemController {

	private BankService bankService;

	@Autowired
	public BankingSystemController(BankService bankService) {
		this.bankService = bankService;
	}
	//Create Bank Controller 
	@PostMapping(path="/banks",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBank(@RequestBody CreateBankRequestDto createBankRequestDto){
		if(bankService.findByBankName(createBankRequestDto.getName())!=null) {
			 return new ResponseEntity<>(GenericResponseDto.builder().success(false).message("Given username already Used").build(),HttpStatus.UNPROCESSABLE_ENTITY);
		}
		bankService.createBank(createBankRequestDto);
		BankEntity bankEntity=bankService.findByBankName(createBankRequestDto.getName());
		return new ResponseEntity<>(CreateBankResponseDto.builder().success(true).message("Created Successfully").bank(bankEntity).build(),HttpStatus.CREATED);
	}

}
