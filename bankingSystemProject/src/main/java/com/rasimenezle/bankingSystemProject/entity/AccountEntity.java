package com.rasimenezle.bankingSystemProject.entity;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("Account")
public class AccountEntity {
	
	 private int id;
     private int bankId;
     private int userId;
     private int accountNumber;
     private AccountsType type;
     private double balance;
     private Timestamp creationDate;
     private Timestamp  lastUpdateDate;
     private boolean isDeleted;

}
