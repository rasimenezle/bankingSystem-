package com.rasimenezle.bankingSystemProject.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.rasimenezle.bankingSystemProject.entity.BankEntity;



@Mapper
public interface BankRepository {
	
	@Insert("Insert into banks (name) Values(#{name})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void createBank(BankEntity bank);
	@Select("SELECT * FROM banks WHERE name=#{name} ")
    BankEntity findByBankName(String bankName);

}
