package com.contactsservice;

import com.byAmina.OutboxEventDTO;
import com.contactsservice.persistance.model.Contact;
import com.contactsservice.persistance.model.OutboxEvent;
import com.contactsservice.persistance.repository.OutboxEventRepository;
import com.contactsservice.usecasses.OutboxService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class OutboxServiceIntegrationTest {

    private static final long POLL_TIMEOUT = 3000;
    private static final String CONTACT_CREATED_EVENT = "ContactCreated";
    private static final String PHONE_NUMBER = "9999999999";
    private static final long CV_ID = 1L;
    private static final long PHONE_CODE_ID = 1L;

    @Autowired
    private OutboxService outboxService;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaMessageListenerContainer<String,OutboxEventDTO> container;

    private BlockingQueue<ConsumerRecord<String, OutboxEventDTO>> records;

    @BeforeEach
    void setup() {
        DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());
        ContainerProperties containerProperties = new ContainerProperties(environment.getProperty("topic.name"));
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, OutboxEventDTO>) record -> records.add(record));
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @Test
    void testCreateProduct_whenAllIsValid_ContactDetails_successfullySendKafkaMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        // Arrange
        Contact contact = Contact.builder()
                .withCvId(CV_ID)
                .withPhoneCodeId(PHONE_CODE_ID)
                .withPhoneNumber(PHONE_NUMBER)
                .build();

        OutboxEvent event = new OutboxEvent();
        event.setEventType(CONTACT_CREATED_EVENT);
        event.setPayload(new ObjectMapper().writeValueAsString(contact));
        outboxEventRepository.save(event);

        // Act
        outboxService.processOutbox();

        // Assert
        ConsumerRecord<String, OutboxEventDTO> message = records.poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
        assertNotNull(message);
        assertEquals(CONTACT_CREATED_EVENT, message.value().getEventType());
        assertTrue(message.value().getPayload().contains(PHONE_NUMBER));

        // Проверяем что событие удалено из очереди
        assertTrue(outboxEventRepository.findAll().isEmpty());
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("spring.kafka.consumer.auto-offset-reset"),
                JsonDeserializer.TRUSTED_PACKAGES, environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
    }

    @AfterEach
    void tearDown() {
        container.stop();
        records.clear();
        outboxEventRepository.deleteAll();
    }
}
