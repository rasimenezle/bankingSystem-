package com.rasimenezle.bankingSystemProject.kafka;

import org.apache.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
	
	
	@KafkaListener(topics = "logs",
            groupId = "logs")
	

// Method
public void consume(@Payload String message)
{
		final Logger logger = Logger.getLogger(this.getClass());
	
		logger.info(message);
		
		
		
}

}