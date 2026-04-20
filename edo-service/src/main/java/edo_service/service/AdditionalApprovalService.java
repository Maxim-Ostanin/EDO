package edo_service.service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import edo_service.converter.AdditionalApprovalToAdditionalApprovalDtoConverter;
import edo_service.validator.AdditionalApprovalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor  // ← Lombok создаст конструктор со всеми final полями
public class AdditionalApprovalService {

    // Все зависимости перечисляем как final поля
    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;
    private final AdditionalApprovalValidator validator;  // ← ИНЖЕКТ ВАЛИДАТОРА

    public AdditionalApprovalService(AdditionalApprovalRepository additionalApprovalRepository, ApprovalRepository approvalRepository, AdditionalApprovalToAdditionalApprovalDtoConverter converter, AdditionalApprovalValidator validator) {
        this.additionalApprovalRepository = additionalApprovalRepository;
        this.approvalRepository = approvalRepository;
        this.converter = converter;
        this.validator = validator;
    }

    @Transactional
    public AdditionalApprovalDto create(AdditionalApprovalDto dto) {
        // ИСПОЛЬЗУЕМ ВАЛИДАТОР
        validator.validateAll(dto);

        // Конвертация
        AdditionalApproval entity = converter.toEntity(dto);

        // Установка связей
        if (dto.getApprovalId() != null) {
            Approval approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new RuntimeException("Approval not found"));
            entity.setApproval(approval);
        }

        // Сохранение
        AdditionalApproval savedEntity = additionalApprovalRepository.save(entity);

        return converter.toDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public AdditionalApprovalDto getById(Long id) {
        AdditionalApproval entity = additionalApprovalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return converter.toDto(entity);
    }
}

@Service
@RequiredArgsConstructor
public class AdditionalApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalApprovalService.class);

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;
    private final AdditionalApprovalValidator validator;

    // ============================================================
    // CREATE (POST) - создание
    // ============================================================

    @Transactional
    public AdditionalApprovalDto create(AdditionalApprovalDto dto) {
        logger.info("CREATE: Начало создания дополнительного согласования. Данные: approvalId={}, status={}, type={}",
                dto.getApprovalId(), dto.getStatus(), dto.getType());

        try {
            // Валидация
            validator.validateAll(dto);
            logger.debug("CREATE: Валидация пройдена успешно");

            // Конвертация
            AdditionalApproval entity = converter.toEntity(dto);

            // Установка связи
            if (dto.getApprovalId() != null) {
                Approval approval = approvalRepository.findById(dto.getApprovalId())
                        .orElseThrow(() -> new RuntimeException("Approval not found"));
                entity.setApproval(approval);
                logger.debug("CREATE: Установлена связь с approval id={}", dto.getApprovalId());
            }

            // Сохранение
            AdditionalApproval savedEntity = additionalApprovalRepository.save(entity);
            AdditionalApprovalDto result = converter.toDto(savedEntity);

            // Лог УСПЕХА
            logger.info("CREATE: УСПЕШНО создано дополнительное согласование. id={}, approvalId={}, status={}",
                    result.getId(), result.getApprovalId(), result.getStatus());

            return result;

        } catch (Exception e) {
            // Лог ОШИБКИ
            logger.error("CREATE: ОШИБКА при создании дополнительного согласования. Данные: approvalId={}, status={}. Причина: {}",
                    dto.getApprovalId(), dto.getStatus(), e.getMessage(), e);
            throw e;
        }
    }

    // ============================================================
    // READ (GET) - чтение по ID
    // ============================================================

    @Transactional(readOnly = true)
    public AdditionalApprovalDto getById(Long id) {
        logger.info("READ: Запрос на получение дополнительного согласования с id={}", id);

        try {
            AdditionalApproval entity = additionalApprovalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("AdditionalApproval not found with id: " + id));

            AdditionalApprovalDto result = converter.toDto(entity);

            // Лог УСПЕХА
            logger.info("READ: УСПЕШНО получено дополнительное согласование. id={}, approvalId={}, status={}",
                    result.getId(), result.getApprovalId(), result.getStatus());

            return result;

        } catch (Exception e) {
            // Лог ОШИБКИ
            logger.error("READ: ОШИБКА при получении дополнительного согласования. id={}. Причина: {}",
                    id, e.getMessage(), e);
            throw e;
        }
    }

    // ============================================================
    // READ (GET ALL) - получение всех записей
    // ============================================================

    @Transactional(readOnly = true)
    public List<AdditionalApprovalDto> getAll() {
        logger.info("READ ALL: Запрос на получение всех дополнительных согласований");

        try {
            List<AdditionalApproval> entities = additionalApprovalRepository.findAll();
            List<AdditionalApprovalDto> results = entities.stream()
                    .map(converter::toDto)
                    .collect(Collectors.toList());

            // Лог УСПЕХА
            logger.info("READ ALL: УСПЕШНО получено {} записей", results.size());

            return results;

        } catch (Exception e) {
            // Лог ОШИБКИ
            logger.error("READ ALL: ОШИБКА при получении всех дополнительных согласований. Причина: {}",
                    e.getMessage(), e);
            throw e;
        }
    }

    // ============================================================
    // UPDATE (PUT/PATCH) - обновление
    // ============================================================

    @Transactional
    public AdditionalApprovalDto update(Long id, AdditionalApprovalDto dto) {
        logger.info("UPDATE: Начало обновления дополнительного согласования. id={}, новые данные: approvalId={}, status={}",
                id, dto.getApprovalId(), dto.getStatus());

        try {
            // Проверяем существование
            AdditionalApproval existingEntity = additionalApprovalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("AdditionalApproval not found with id: " + id));

            logger.debug("UPDATE: Найдена существующая запись с id={}, текущий status={}",
                    id, existingEntity.getStatus());

            // Валидация новых данных
            validator.validateAll(dto);

            // Обновляем поля
            converter.updateEntity(dto, existingEntity);

            // Обновляем связь если нужно
            if (dto.getApprovalId() != null &&
                    (existingEntity.getApproval() == null || !dto.getApprovalId().equals(existingEntity.getApproval().getId()))) {
                Approval approval = approvalRepository.findById(dto.getApprovalId())
                        .orElseThrow(() -> new RuntimeException("Approval not found with id: " + dto.getApprovalId()));
                existingEntity.setApproval(approval);
                logger.debug("UPDATE: Обновлена связь с approval id={}", dto.getApprovalId());
            }

            // Сохраняем
            AdditionalApproval updatedEntity = additionalApprovalRepository.save(existingEntity);
            AdditionalApprovalDto result = converter.toDto(updatedEntity);

            // Лог УСПЕХА
            logger.info("UPDATE: УСПЕШНО обновлено дополнительное согласование. id={}, новый status={}",
                    result.getId(), result.getStatus());

            return result;

        } catch (Exception e) {
            // Лог ОШИБКИ
            logger.error("UPDATE: ОШИБКА при обновлении дополнительного согласования. id={}, данные: approvalId={}, status={}. Причина: {}",
                    id, dto.getApprovalId(), dto.getStatus(), e.getMessage(), e);
            throw e;
        }
    }

    // ============================================================
    // DELETE - удаление
    // ============================================================

    @Transactional
    public void delete(Long id) {
        logger.info("DELETE: Начало удаления дополнительного согласования с id={}", id);

        try {
            // Проверяем существование перед удалением
            if (!additionalApprovalRepository.existsById(id)) {
                throw new RuntimeException("AdditionalApproval not found with id: " + id);
            }

            logger.debug("DELETE: Запись с id={} найдена, выполняем удаление", id);

            // Удаляем
            additionalApprovalRepository.deleteById(id);

            // Лог УСПЕХА
            logger.info("DELETE: УСПЕШНО удалено дополнительное согласование с id={}", id);

        } catch (Exception e) {
            // Лог ОШИБКИ
            logger.error("DELETE: ОШИБКА при удалении дополнительного согласования. id={}. Причина: {}",
                    id, e.getMessage(), e);
            throw e;
        }
    }
}