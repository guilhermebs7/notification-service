package br.notification_service.config;
import org.springframework.amqp.core.Queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;


@Configuration
public class RabbitMQConfig {

    public static final String NOME_FILA= "email.boasvindas";

    @Bean
    public Queue filasBoasVindas() {
        return new Queue(NOME_FILA, true);  //fila salva em um disco (não se perde)

    }
    @Bean
    public JacksonJsonMessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }
}
