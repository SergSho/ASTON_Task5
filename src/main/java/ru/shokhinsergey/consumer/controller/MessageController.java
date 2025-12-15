package ru.shokhinsergey.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ru.shokhinsergey.consumer.service.MessageService;
import ru.shokhinsergey.message.Message;

@RestController
public class MessageController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private MessageService service;

    @Autowired
    public MessageController(MessageService service) {
        this.service = service;
    }

    @RequestMapping("/message")
    public ResponseEntity<Void> sendMail (@RequestBody Message message) {
        String operation = message.getOperation();
        String email = message.getEmail();
        LOG.info("KafkaConsumer received Message. Операция: {}, email: {}", operation, email);

        if (operation.equalsIgnoreCase("create")) service.sendEmailWhenUserCreate(
                Message.instanceOfMessageOnCreate(email));
        else if (operation.equalsIgnoreCase("delete")) service.sendEmailWhenUserDelete(
                Message.instanceOfMessageOnDelete(email));
        else return ResponseEntity.status(500).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
