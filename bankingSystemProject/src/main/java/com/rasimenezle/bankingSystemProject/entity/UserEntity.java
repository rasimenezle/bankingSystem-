package com.rasimenezle.bankingSystemProject.entity;


import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("User")
public class UserEntity {

	private int id;
	private String userName;
	private String email;
	private String password;
	private boolean enabled;
	private String authorities;
	
    transient private String token;

}
