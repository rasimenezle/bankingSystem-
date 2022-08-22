package com.rasimenezle.bankingSystemProject.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.rasimenezle.bankingSystemProject.dtoRequest.LoginRequestDto;
import com.rasimenezle.bankingSystemProject.dtoResponse.LoginDtoResponse;
import com.rasimenezle.bankingSystemProject.security.CustomUserDetailsService;


@Component
public class AuthTokenFilter {
	@Autowired
	private JwtUtils jwtUtil;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;

	

	public LoginDtoResponse login(LoginRequestDto loginRequestDto) {
		LoginDtoResponse loginDtoResponse = new LoginDtoResponse();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUserName(),
					loginRequestDto.getPassword()));

			final UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(loginRequestDto.getUserName());
			final String token = jwtUtil.generateToken(userDetails);

			loginDtoResponse.setStatus("success");
			loginDtoResponse.setToken(token);

		} catch (BadCredentialsException e) {
			loginDtoResponse = null;
		} catch (DisabledException e) {
		}
		return loginDtoResponse;
	}
}