package com.rasimenezle.bankingSystemProject.kafka;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

public class KafkaTopicConfiguration {
	@Bean 
	public KafkaAdmin kafkaAdmin () {
		Map<String,Object> configs=new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		return new KafkaAdmin(configs);
	}
	
	@Bean
	public NewTopic createTopic() {
		return new NewTopic("logs",1,(short)1);
	}
}