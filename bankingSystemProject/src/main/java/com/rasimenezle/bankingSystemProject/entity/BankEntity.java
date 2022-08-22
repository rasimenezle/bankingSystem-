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
@Alias("Bank")
public class BankEntity {
	private int id;
    private String name ;
}
