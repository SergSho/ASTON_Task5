package ru.shokhinsergey.consumer.handler;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.shokhinsergey.consumer.service.MessageService;
import ru.shokhinsergey.message.Message;

import java.util.Map;

//Пересоздание тестового контекста в случае изменения его состояния в тестовом методе (изоляция тестов)
@DirtiesContext
//Повторное использование объекта тестового класса для всех входящих тестовых методов (BeforeAll - можно не static)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Перезаписать properties из основного конфига тестовыми
@ActiveProfiles("test")
@EmbeddedKafka(topics = "user-event", partitions = 1, controlledShutdown = true)
//@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class HandlerWithKafkaIntegrationTest {

    private  final static String EMAIL = "example@mail.ru";
    private  final static String TOPIC = "user-event";
    private  final static Integer ID = 1;
    private  final static Message CREATE_MESSAGE = Message.instanceOfMessageOnCreate(EMAIL);
    private  final static Message DELETE_MESSAGE = Message.instanceOfMessageOnDelete(EMAIL);

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockitoBean
    private MessageService service;

    @MockitoSpyBean
    private MessageHandler handler;

    private Producer<Integer, Message> producer;

    @BeforeAll
    public void init (){
        Map<String, Object> props = KafkaTestUtils.producerProps(embeddedKafkaBroker.getBrokersAsString());
        producer = new KafkaProducer<>(props,new IntegerSerializer(), new JsonSerializer<>());
    }

    @Test
    @DisplayName("Получение сообщения из \"Kafka\" при создании нового \"User\".")
    void receiveMessageFromKafkaOnCreate_Ok_MethodSendMessage () throws InterruptedException {

        producer.send(new ProducerRecord<>(TOPIC, ID, CREATE_MESSAGE));

        Thread.sleep(2000);

        Mockito.verify(handler, Mockito.times(1)).sendMessage(CREATE_MESSAGE);
        Mockito.verify(service, Mockito.times(1)).sendEmailWhenUserCreate(CREATE_MESSAGE);
    }

    @Test
    @DisplayName("Получение сообщения из \"Kafka\" при удалении \"User\".")
    void receiveMessageFromKafkaOnDelete_Ok_MethodSendMessage () throws InterruptedException {

        producer.send(new ProducerRecord<>(TOPIC, ID, DELETE_MESSAGE));

        Thread.sleep(2000);

        Mockito.verify(handler, Mockito.times(1)).sendMessage(DELETE_MESSAGE);
        Mockito.verify(service, Mockito.times(1)).sendEmailWhenUserDelete(DELETE_MESSAGE);
    }
}

