package edo_service.service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import edo_service.converter.AdditionalApprovalToAdditionalApprovalDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdditionalApprovalService {

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;

    /**
     * Получение DTO по ID
     */
    @Transactional(readOnly = true)
    public AdditionalApprovalDto getById(Long id) {
        AdditionalApproval entity = additionalApprovalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AdditionalApproval not found with id: " + id));

        return converter.toDto(entity);
    }

    /**
     * Создание новой записи из DTO
     */
    @Transactional
    public AdditionalApprovalDto create(AdditionalApprovalDto dto) {
        // Конвертируем DTO -> Entity
        AdditionalApproval entity = converter.toEntity(dto);

        // Устанавливаем связь с Approval отдельно
        if (dto.getApprovalId() != null) {
            Approval approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new RuntimeException("Approval not found with id: " + dto.getApprovalId()));
            entity.setApproval(approval);
        }

        // Сохраняем
        AdditionalApproval savedEntity = additionalApprovalRepository.save(entity);

        return converter.toDto(savedEntity);
    }

    /**
     * Обновление существующей записи
     */
    @Transactional
    public AdditionalApprovalDto update(Long id, AdditionalApprovalDto dto) {
        AdditionalApproval existingEntity = additionalApprovalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AdditionalApproval not found with id: " + id));

        // Обновляем поля
        converter.updateEntity(dto, existingEntity);

        // Обновляем связь с Approval если нужно
        if (dto.getApprovalId() != null &&
                (existingEntity.getApproval() == null || !dto.getApprovalId().equals(existingEntity.getApproval().getId()))) {
            Approval approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new RuntimeException("Approval not found with id: " + dto.getApprovalId()));
            existingEntity.setApproval(approval);
        }

        // Сохраняем
        AdditionalApproval updatedEntity = (AdditionalApproval) additionalApprovalRepository.save(existingEntity);

        return converter.toDto(updatedEntity);
    }

    /**
     * Удаление записи
     */
    @Transactional
    public void delete(Long id) {
        additionalApprovalRepository.deleteById(id);
    }
}