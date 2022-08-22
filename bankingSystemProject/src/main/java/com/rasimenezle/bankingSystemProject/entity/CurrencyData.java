package com.rasimenezle.bankingSystemProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyData {

	public String code;

	public String name;

	public String rate;

	public String calculatedstr;

	public double calculated;

}
