package edo_service.kafka;

import common.dto.event.AdditionalApprovalEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalApprovalEventConsumer {

    @KafkaListener(
            topics = "${kafka.topic.additional-approval-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAdditionalApprovalEvent(AdditionalApprovalEventDto event) {
        log.info("Получено событие из Kafka (AdditionalApproval): {}", event);

        // Здесь добавьте бизнес-логику обработки события
        switch (event.getEventType()) {
            case "CREATED":
                log.info("Новое дополнительное согласование создано с id: {}", event.getAdditionalApprovalId());
                log.info("   - approvalId: {}", event.getApprovalId());
                log.info("   - type: {}", event.getType());
                log.info("   - status: {}", event.getStatus());
                break;
            case "UPDATED":
                log.info("Дополнительное согласование обновлено с id: {}", event.getAdditionalApprovalId());
                log.info("   - новый status: {}", event.getStatus());
                break;
            case "DELETED":
                log.info(" Дополнительное согласование удалено с id: {}", event.getAdditionalApprovalId());
                break;
            default:
                log.warn("Неизвестный тип события: {}", event.getEventType());
        }
    }
}
