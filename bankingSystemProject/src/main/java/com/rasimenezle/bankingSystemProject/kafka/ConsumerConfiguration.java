package com.rasimenezle.bankingSystemProject.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

@Configuration
@EnableKafka
public class ConsumerConfiguration {

	@Bean
    public ConsumerFactory<String, String> consumerFactory()
    {
  
       
        Map<String, Object> config = new HashMap<>();
  
        
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                   "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                   "json");
        config.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
        config.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
  
        return new DefaultKafkaConsumerFactory<>(config);
    }
  
    
    public ConcurrentKafkaListenerContainerFactory
    concurrentKafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<
            String, String> factory
            = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }}