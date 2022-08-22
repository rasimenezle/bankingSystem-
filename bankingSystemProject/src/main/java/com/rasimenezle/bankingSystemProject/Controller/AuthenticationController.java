package com.rasimenezle.bankingSystemProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasimenezle.bankingSystemProject.dtoRequest.LoginRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.SignupRequestDto;
import com.rasimenezle.bankingSystemProject.dtoRequest.UserEnableDtoRequest;
import com.rasimenezle.bankingSystemProject.dtoResponse.GenericResponseDto;
import com.rasimenezle.bankingSystemProject.dtoResponse.LoginDtoResponse;
import com.rasimenezle.bankingSystemProject.dtoResponse.SignUpDontResponseDto;
import com.rasimenezle.bankingSystemProject.dtoResponse.SignupResponseDto;
import com.rasimenezle.bankingSystemProject.entity.UserEntity;
import com.rasimenezle.bankingSystemProject.jwt.AuthTokenFilter;
import com.rasimenezle.bankingSystemProject.service.UserService;

@RestController
@RequestMapping(path = "/banking")
@CrossOrigin(origins ="http://localhost:4200")
public class AuthenticationController {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthTokenFilter authTokenFilter;

	@PostMapping("/register")
	public ResponseEntity<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {

		if (userService.findByUsername(signupRequestDto.getUserName()) != null) {

			return new ResponseEntity<>(
					SignUpDontResponseDto.builder().success(false)
							.message("Given username already Used : [" + signupRequestDto.getUserName() + "]").build(),
					HttpStatus.CONFLICT);
		}

		if (userService.findByEmail(signupRequestDto.getEmail()) != null) {
			return new ResponseEntity<>(
					SignUpDontResponseDto.builder().success(false)
							.message("Given username already Email :[" + signupRequestDto.getEmail() + "]").build(),
					HttpStatus.CONFLICT);
		}

		userService.userSave(signupRequestDto);
		UserEntity user = userService.findByUsername(signupRequestDto.getUserName());
		return new ResponseEntity<>(
				SignupResponseDto.builder().success(true).message("Created Successfully").user(user).build(),
				HttpStatus.CREATED);

	}

	@PostMapping("/auth")
	public ResponseEntity<?> login(@RequestBody @Validated LoginRequestDto request) {
		LoginDtoResponse dto = this.authTokenFilter.login(request);

		if (dto == null) {

			return new ResponseEntity<>(GenericResponseDto.builder().success(false).message("User Not Found").build(),HttpStatus.FORBIDDEN);
		}
		return ResponseEntity.ok().body(dto);
	}

	@PatchMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserActiveDeactive(@PathVariable(name = "id") int id,
			@RequestBody UserEnableDtoRequest userEnableDtoRequest) {

		UserEntity user = userService.findByUserId(id);
		if (user != null) {
			userService.userUpdateEnable(id, userEnableDtoRequest);
			UserEntity userUpdate = userService.findByUserId(id);
			if (userUpdate.isEnabled() == true) {
				return new ResponseEntity<>(GenericResponseDto.builder().success(true).message("User Disabled").build(),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						GenericResponseDto.builder().success(true).message("User Enabled ").build(),
						HttpStatus.OK);
			}

		} else {
			return new ResponseEntity<>(
					GenericResponseDto.builder().success(false).message("There is no user in this id").build(),
					HttpStatus.NOT_FOUND);

		}

	}

}
