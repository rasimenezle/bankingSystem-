package com.rasimenezle.bankingSystemProject.entity;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyResult {
	public String base;
	private String lastupdate;
	private List<CurrencyData> data;
	

}
