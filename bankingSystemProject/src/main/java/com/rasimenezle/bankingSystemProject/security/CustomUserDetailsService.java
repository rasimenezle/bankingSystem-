package com.rasimenezle.bankingSystemProject.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rasimenezle.bankingSystemProject.entity.CustomUserEntity;
import com.rasimenezle.bankingSystemProject.entity.UserEntity;
import com.rasimenezle.bankingSystemProject.service.UserService;

@Configuration
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userService.findByUsername(username);
		if (user == null || user.isEnabled() == true) {
			throw new UsernameNotFoundException(username);
		}
		String[] authoritiesList = user.getAuthorities().split(",");

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String auth : authoritiesList) {
			authorities.add(new SimpleGrantedAuthority(auth));
		}
		return CustomUserEntity.builder().userEntity(user).id(user.getId()).userName(username)
				.password(user.getPassword()).authorities(authorities)
				.build();
	}
}
