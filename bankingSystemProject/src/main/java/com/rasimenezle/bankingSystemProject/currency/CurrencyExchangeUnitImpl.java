package com.rasimenezle.bankingSystemProject.currency;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasimenezle.bankingSystemProject.entity.Currency;
import com.rasimenezle.bankingSystemProject.entity.CurrencyData;
import com.rasimenezle.bankingSystemProject.entity.CurrencyOns;
import com.rasimenezle.bankingSystemProject.entity.CurrencyOnsResult;

@Component
public class CurrencyExchangeUnitImpl  implements CurrencyExchangeUnit{

	@Autowired
	RestTemplate template = new RestTemplate();
	 
    @Autowired
    ModelMapper modelMapper;
    		
    @Override
	public double currencyChange(double balance, String accountType, String accountType2) {

    	
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 6e9cJyxW9it2ZTKayQmMyb:3ogpv0dxPN8BffOHja8OHl");

		HttpEntity<?> responseEntity = new HttpEntity<>(headers);
		
		String uri="https://api.collectapi.com/economy/exchange?{query}&to={query2}&base={query3}";
		
		Map<String, String> uriVariables = new HashMap<>();
		String balanceString=String.valueOf(balance);
		 
		uriVariables.put("query",balanceString );
		uriVariables.put("query2", accountType);
		uriVariables.put("query3", accountType2);
		ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, responseEntity, String.class, uriVariables);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		Currency data=new Currency();
		
		try {
			
			data=objectMapper.readValue(response.getBody().toString(), Currency.class);
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CurrencyData a = data.getResult().getData().get(0);
		return a.getCalculated();
	}

	@Override
	public double buyingGramGAU(double balance, String accountType, String accountType2) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 6e9cJyxW9it2ZTKayQmMyb:3ogpv0dxPN8BffOHja8OHl");

		HttpEntity<?> responseEntity = new HttpEntity<>(headers);
		
		String uri="https://api.collectapi.com/economy/goldPrice";
		ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, responseEntity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		CurrencyOns data=new CurrencyOns();
		try {
			data=objectMapper.readValue(response.getBody(), CurrencyOns.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CurrencyOnsResult result=data.getResult().get(0);
		return result.getBuying();
	}

	@Override
	public double sellingGramGAU(double balance, String accountType, String accountType2) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 6e9cJyxW9it2ZTKayQmMyb:3ogpv0dxPN8BffOHja8OHl");

		HttpEntity<?> responseEntity = new HttpEntity<>(headers);
		
		String uri="https://api.collectapi.com/economy/goldPrice";
		ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, responseEntity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		CurrencyOns data=new CurrencyOns();
		try {
			data=objectMapper.readValue(response.getBody(), CurrencyOns.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CurrencyOnsResult result=data.getResult().get(0);
		return result.getSelling();
	}

}
