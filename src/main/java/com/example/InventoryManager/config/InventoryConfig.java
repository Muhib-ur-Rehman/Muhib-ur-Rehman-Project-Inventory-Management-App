package com.example.InventoryManager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfig {
    public static final String INVENTORY_QUEUE = "inventory_queue";
    public static final String EXCHANGE= "order_exchange";
    public static final String INVENTORY_ROUTING_KEY= "inventory_routingKey";
    public static final String ROUTING_KEY= "order_routingKey";


    @Bean
    public Queue inventoryQueue (){
        return new Queue(INVENTORY_QUEUE);
    }

    @Bean
    public TopicExchange paymentExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding inventoryBinding(TopicExchange exchange){
        return BindingBuilder.bind(inventoryQueue()).to(exchange).with(INVENTORY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter paymentConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate paymentTemplate (ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(paymentConverter());
        return rabbitTemplate;
    }
}
