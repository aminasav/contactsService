package com.contactsservice.usecasses.impl;

import com.byAmina.OutboxEventDTO;
import com.contactsservice.persistance.model.OutboxEvent;
import com.contactsservice.persistance.repository.OutboxEventRepository;
import com.contactsservice.usecasses.OutboxService;
import com.contactsservice.usecasses.mapper.OutboxEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventMapper outboxEventMapper;
    private final KafkaTemplate<String, OutboxEventDTO> kafkaTemplate;

    @Value("${topic.name}")
    private String topicName;

    @Scheduled(fixedRate = 5000)
    @Override
    public void processOutbox() {
        List<OutboxEvent> events = outboxEventRepository.findAll();
        for (OutboxEvent event : events) {

            OutboxEventDTO eventDTO = outboxEventMapper.toDto(event);

            ProducerRecord<String,OutboxEventDTO> record = new ProducerRecord<>(
                    topicName,
                    event.getId().toString(),
                    eventDTO
            );

            record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
            SendResult<String, OutboxEventDTO> result = null;

            try {
                result = kafkaTemplate
                        .send(record).get();

                log.info("Processed outbox event: {}", eventDTO);
                log.info("Topic: {}", result.getRecordMetadata().topic());
                log.info("Partition: {}", result.getRecordMetadata().partition());
                log.info("Offset: {}", result.getRecordMetadata().offset());

                outboxEventRepository.delete(event);
                log.info("Deleted outbox event: {}", eventDTO);

            } catch (ExecutionException | InterruptedException e) {
                log.error(e.getMessage(), e);

            }
        }
    }
}
