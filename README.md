# bankingSystem
# Banking System

Used Technologies:

- Java
- Spring Boot
- MyBatis
- Kafka
- Spring Security
- MySQL
- Log4J
- Collect API

## Bank

### For Create Bank;

You can `POST` request to URL = `.../banking/bank`
``` json
{
    "name" : "[bank_name]"
}
```

## User

### For Register (Create User);

You can `POST` request to URL = `.../register`
```json
{
    "username" : "[username]",
    "email" : " [email]",
    "password" : "[password]"
}
```

### For Enable/Disable User;

You can `PATCH` request to URL = `.../user/{id}`
```json
{
    "enabled" : "[true or false]"
}
```

Users with `ACTIVATE_DEACTIVATE_USER` authorization in this service; will be able to enable or disable users on the system.

## Login

You can "POST" request to URL = ".../auth"

```json
{
    "username" : "[username]",
    "password" : "[password]"
}
```

This is our web service where users can log into the system. Thanks to the JWT Token obtained in the response received at the end of the request, we will not need to constantly send requests to the system with username and password.

## Account

### For Create Account;

You can `PATCH` request to URL = `.../createAccounts`
```json
{
    "bankId" : [bank_id]
    "type": [TL, Dolar, Altın]
}
```

The id of the bank and the type of the account will be taken. Other columns of the account
- balance = 0
- user_id = the id of the authenticated user trying to open the account
- creation_date = timestamp of the moment of the transaction
- last_update_date = same date as creation_date
- is_deleted = false

### For Get Detail of Account;

You can `GET` request to URL = `.../accounts/{accountNumber}`  
This is our service that brings the details of the account. Everyone will only be able to see the details of one of their accounts. Here, if the authenticated user wants to see the details of an account belonging to someone else, it will not be allowed.

### For Remove Account;

You can `DELETE` request to URL = `.../accounts/{accountNumber}`  
An account can be deleted in this service. Only users with REMOVE_ACCOUNT rights will be able to do this. Here Account will not be hard-delete from database; instead the value of the is_deleted column of the account will be updated to true.

### For Increase Balance;

You can `PATCH` request to URL = `.../accounts/{accountNumber}`
```json
{
    "amount" : [amount]
}
```

Each user will ONLY be able to deposit funds into their own account.

### For Transfer Balance;

You can `PATCH` request to URL = `...accounts/transfer/{accountNumber}`
```json
{
    "amount" : [amount],
    "receiverAccountId" : [receiver account_id]
}
```

AccountNumber in URL is owner account id. First of all, anyone can transfer from their own account to another account. So the senderAccountId authenticated user must have an account. If the sending account does not have sufficient balance, it will receive the message "insufficient balance". If the account types of the sending account and the receiving account are different, the transfer will be made by making conversions between the types. Collect Api > Economy > Gold, Currency and Stock Exchange API will be used while performing these conversions. If the sending account and the receiving account belong to other banks; If the sending account is in TL, 3 TL will be charged, and if it is in Dolar, an additional 1 Dolar EFT fee will be charged. If the sending account is Altın, there will be no deductions.
 
## Database Design
### For Bank;
    {  
        int id PRIMARY KEY AUTO_INCREMENT,  
        string name NOT NULL UNIQUE  
    }  
### For User;
    {
        int id PRIMARY KEY AUTO_INCREMENT,  
        String username NOT NULL UNIQUE,  
        String email NOT NULL UNIQUE,  
        String password NOT NULL,  
        boolean enabled DEFAULT true,  
        String authorities  
    }  
### For Account;
    {
        int id PRIMARY KEY AUTO_INCREMENT,  
        user_id FOREING KEY(user.id),  
        bank_id FOREIGN_KEY(bank.id),  
        number int(10),  
        String type(TL,ALTIN,DOLAR)  
        double balance DEFAULT 0,  
        timestamp creation_date,  
        timestamp last_update_date,  
        boolean is_deleted DEFAULT false  
    }

## Kafka
Kafka's version kafka_2.12-3.2.0 was used. To run Kafka; in the file you downloaded kafka,  
first cmd directory => .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties   
in the second cmd directory => .\bin\windows\kafka-server-start.bat .\config\server.properties  
