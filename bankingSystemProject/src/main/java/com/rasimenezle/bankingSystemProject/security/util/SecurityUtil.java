package com.rasimenezle.bankingSystemProject.security.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUtil {
	public static final String AUTH_HEADER="authorization";
	public static final String AUTH_TOKEN_TYPE="Bearer";
	public static final String AUTH_TOKEN_PREFİX=AUTH_TOKEN_TYPE+" ";
	
	
	
	public static String extractAuthTokenFromRequest(HttpServletRequest request) {
		String bearerToken=request.getHeader(AUTH_HEADER);
		if(bearerToken != null&& bearerToken.startsWith(AUTH_TOKEN_PREFİX)) {
			
			return bearerToken.substring(7);
		}
	  return null;
	}

}
