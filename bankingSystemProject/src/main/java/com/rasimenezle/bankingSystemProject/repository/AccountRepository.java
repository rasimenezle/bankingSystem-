package com.rasimenezle.bankingSystemProject.repository;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import com.rasimenezle.bankingSystemProject.entity.AccountEntity;

@Mapper
public interface AccountRepository {
	@Transactional
	@Insert("INSERT INTO Account(bankId,userId,accountNumber,isDeleted,creationDate,type,balance,lastUpdateDate) VALUES(#{bankId},#{userId},#{accountNumber},#{isDeleted},#{creationDate},#{type},#{balance},#{lastUpdateDate})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void saveAccount(AccountEntity createAccount);
	
	@Transactional
	@Select("select * from Account where accountNumber = #{accountNumber}")
	public AccountEntity findByAccountNumber(int accountNumber);
	
	@Transactional
	@Update("update account set isDeleted=#{isDeleted},lastUpdateDate=#{lastUpdateDate} where accountNumber=#{accountNumber}")
	public void deleteByAccountNumber( int accountNumber,boolean isDeleted,Timestamp lastUpdateDate);
	
	@Transactional
	@Update("update account set balance =#{balance},lastUpdateDate=#{lastUpdateDate} where accountNumber=#{accountNumber}")
	public void depositByAccountNumber(int accountNumber, double balance,Timestamp lastUpdateDate);
	
}
