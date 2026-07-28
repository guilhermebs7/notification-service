package br.notification_service.controller;

import br.notification_service.config.RabbitMQConfig;
import br.notification_service.dto.UsuarioCriadoEvent;
import br.notification_service.dto.UsuarioRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioRequest request){

        Long idGerado=1L;       //simula salvamento no banco de dados

        UsuarioCriadoEvent event= new UsuarioCriadoEvent(idGerado, request.nome(), request.email());

        rabbitTemplate.convertAndSend(RabbitMQConfig.NOME_FILA,event);    //envia a mensagem para a fila "email.boasvindas"

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário cadastrado! o e-mail de boas-vindas está sendo enviado em segundo plano");



    }
}
