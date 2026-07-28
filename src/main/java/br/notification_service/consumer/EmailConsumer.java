package br.notification_service.consumer;


import br.notification_service.config.RabbitMQConfig;
import br.notification_service.dto.UsuarioCriadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailConsumer {

    private static final Logger log= LoggerFactory.getLogger(EmailConsumer.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetenteEmail;

    @RabbitListener(queues = RabbitMQConfig.NOME_FILA)
    public void processarBoasVindas(UsuarioCriadoEvent event){
        log.info("Mensagem recebida da fila! Enviado e-mail real para : {}",event.email());

        try{
            SimpleMailMessage message= new SimpleMailMessage();
            message.setFrom(remetenteEmail);
            message.setTo(event.email());
            message.setSubject("Seja bem-vindo(a), "+ event.nome() + "!");
            message.setText("Olá "+ event.nome() + ",\n\nSeu cadastro foi realizado com sucesso!\n\nAtenciosamente, \nSua Aplicação");

            mailSender.send(message);

            log.info("E-mail REAL enviado com sucesso para: {}",event.email());
        }catch (Exception e){
            log.error("Erro ao enviar e-mail: {}", e.getMessage());
        }
    }
}
