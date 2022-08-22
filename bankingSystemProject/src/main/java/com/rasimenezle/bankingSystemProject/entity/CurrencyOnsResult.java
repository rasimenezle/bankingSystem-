package com.rasimenezle.bankingSystemProject.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyOnsResult {

	public String name;
	public double buying;
	public String buyingstr;
	private double selling;
	private String sellingstr;
	private String time;
	private String date;
	private String datetime;
	private String rate;}
