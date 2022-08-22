package com.rasimenezle.bankingSystemProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rasimenezle.bankingSystemProject.jwt.AuthorizationFilter;


@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private AuthorizationFilter authorizationFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/banking/auth/**").permitAll();
		http.authorizeRequests().antMatchers("/banking/register/**").permitAll();
		http.authorizeRequests().antMatchers("/banking/banks").hasAuthority("CREATE_BANKS");
		http.authorizeRequests().antMatchers("/banking/users/{id}**").hasAuthority("ACTIVATE_DEACTIVATE_USER");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/banking/accounts").hasAuthority("REMOVE_ACCOUNT");
		http.authorizeRequests().antMatchers("/banking/createAccounts").hasAuthority("CREATE_ACCOUNT");
//		http.authorizeRequests().antMatchers("/banking/accounts/**").hasAuthority("CREATE_ACCOUNT");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
		http.formLogin().disable();
	}
	
	
	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				
				registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*");
			}
			
		};
	}
	

}
