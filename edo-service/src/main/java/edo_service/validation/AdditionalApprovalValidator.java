package edo_service.validation;

import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import common.dto.AdditionalApprovalDto;
import edo_service.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class AdditionalApprovalValidator {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalApprovalValidator.class);

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;

    private static final Set<String> ALLOWED_STATUSES = Set.of("APPROVED", "REJECTED", "PENDING");
    private static final int MAX_COMMENT_LENGTH = 500;

    public AdditionalApprovalValidator(AdditionalApprovalRepository additionalApprovalRepository,
                                       ApprovalRepository approvalRepository) {
        this.additionalApprovalRepository = additionalApprovalRepository;
        this.approvalRepository = approvalRepository;

        logger.info("AdditionalApprovalValidator инициализирован");
    }

    /**
     * Основной метод валидации для создания доп. согласования
     */
    public void validateForCreate(AdditionalApprovalDto dto) {
        logger.info("Начало валидации для создания AdditionalApproval: approvalId={}, type={}",
                dto.getApprovalId(), dto.getType());

        try {
            validateApprovalExists(dto.getApprovalId());
            validateForDuplicate(dto);
            validateStatus(dto.getStatus());
            validateComment(dto.getComment());
            validateResponseDate(dto);

            logger.info("Валидация успешно пройдена для approvalId={}, type={}",
                    dto.getApprovalId(), dto.getType());

        } catch (ValidationException e) {
            logger.warn("Валидация не пройдена для approvalId={}, type={}: {}",
                    dto.getApprovalId(), dto.getType(), e.getMessage());
            throw e;
        }
    }

    /**
     * Валидация на дубликат
     */
    private void validateForDuplicate(AdditionalApprovalDto dto) {
        logger.debug("Проверка на дубликат: approvalId={}, type={}",
                dto.getApprovalId(), dto.getType());

        if (dto.getApprovalId() == null || dto.getType() == null) {
            logger.debug("Пропуск проверки дубликата: approvalId или type отсутствует");
            return;
        }

        boolean duplicateExists = additionalApprovalRepository.existsByApprovalIdAndType(
                dto.getApprovalId(),
                dto.getType()
        );

        if (duplicateExists) {
            String errorMessage = String.format(
                    "Дублирующее дополнительное согласование уже существует для соглашения ID: %d и типа: %s",
                    dto.getApprovalId(), dto.getType()
            );
            logger.error("Обнаружен дубликат: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        logger.debug("Дубликат не обнаружен для approvalId={}, type={}",
                dto.getApprovalId(), dto.getType());
    }

    /**
     * Валидация на существование соглашения
     */
    private void validateApprovalExists(Long approvalId) {
        logger.debug("Проверка существования соглашения: approvalId={}", approvalId);

        if (approvalId == null) {
            String errorMessage = "ID основного соглашения не может быть пустым";
            logger.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        boolean approvalExists = approvalRepository.existsById(approvalId);

        if (!approvalExists) {
            String errorMessage = String.format("Основное соглашение с ID %d не найдено", approvalId);
            logger.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        logger.debug("Соглашение с ID {} существует", approvalId);
    }

    /**
     * Валидация статуса
     */
    private void validateStatus(String status) {
        logger.debug("Валидация статуса: {}", status);

        if (status == null) {
            String errorMessage = "Статус не может быть пустым";
            logger.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (!ALLOWED_STATUSES.contains(status)) {
            String errorMessage = String.format(
                    "Недопустимый статус: %s. Допустимые значения: %s",
                    status, ALLOWED_STATUSES
            );
            logger.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        logger.debug("Статус {} валиден", status);
    }

    /**
     * Валидация комментария
     */
    private void validateComment(String comment) {
        logger.debug("Валидация комментария: длина={}", comment != null ? comment.length() : "null");

        if (comment != null && comment.length() > MAX_COMMENT_LENGTH) {
            String errorMessage = String.format(
                    "Комментарий не может превышать %d символов. Текущая длина: %d",
                    MAX_COMMENT_LENGTH, comment.length()
            );
            logger.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        logger.debug("Комментарий прошел валидацию");
    }

    /**
     * Валидация даты ответа
     */
    private void validateResponseDate(AdditionalApprovalDto dto) {
        logger.debug("Валидация даты ответа: {}", dto.getResponseDate());

        if (dto.getResponseDate() == null) {
            logger.debug("Дата ответа не указана, пропуск валидации");
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // Проверяем, что responseDate не позже текущего момента
        if (dto.getResponseDate().isAfter(now)) {
            String errorMessage = "Дата ответа не может быть в будущем";
            logger.error("{}: {}", errorMessage, dto.getResponseDate());
            throw new ValidationException(errorMessage);
        }

        // Проверяем, что responseDate не раньше даты обращения
        if (dto.getApprovalId() != null) {
            LocalDateTime appealDate = getAppealDateByApprovalId(dto.getApprovalId());

            if (appealDate != null && dto.getResponseDate().isBefore(appealDate)) {
                String errorMessage = String.format(
                        "Дата ответа (%s) не может быть раньше даты обращения (%s)",
                        dto.getResponseDate(), appealDate
                );
                logger.error(errorMessage);
                throw new ValidationException(errorMessage);
            }
        }

        logger.debug("Дата ответа {} прошла валидацию", dto.getResponseDate());
    }

    /**
     * Вспомогательный метод для получения даты обращения
     */
    private LocalDateTime getAppealDateByApprovalId(Long approvalId) {
        logger.debug("Получение даты обращения для approvalId={}", approvalId);

        try {
            LocalDateTime appealDate = approvalRepository.findAppealDateByApprovalId(approvalId);
            if (appealDate == null) {
                logger.warn("Дата обращения не найдена для approvalId={}", approvalId);
                throw new ValidationException("Не найдена дата обращения для соглашения ID: " + approvalId);
            }

            logger.debug("Дата обращения для approvalId={}: {}", approvalId, appealDate);
            return appealDate;

        } catch (Exception e) {
            logger.error("Ошибка при получении даты обращения для approvalId={}: {}",
                    approvalId, e.getMessage());
            throw new ValidationException("Ошибка при получении даты обращения для соглашения ID: " + approvalId);
        }
    }
}