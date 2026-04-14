package edo_service.converter;

package edo_service.converter;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdditionalApprovalToAdditionalApprovalDtoConverter {

    /**
     * Конвертирует Entity -> DTO (перед валидацией или отправкой клиенту)
     */
    public AdditionalApprovalDto toDto(AdditionalApproval entity) {
        if (entity == null) {
            return null;
        }

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setId(entity.getId());

        // Извлекаем ID из связанной сущности Approval
        if (entity.getApproval() != null) {
            dto.setApprovalId(entity.getApproval().getId());
        } else {
            dto.setApprovalId(null);
        }

        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setComment(entity.getComment());
        dto.setResponseDate(entity.getResponseDate());

        return dto;
    }

    /**
     * Конвертирует DTO -> Entity (перед обращением к репозиторию)
     * ВНИМАНИЕ: Связь с Approval нужно будет установить отдельно в сервисе
     */
    public AdditionalApproval toEntity(AdditionalApprovalDto dto) {
        if (dto == null) {
            return null;
        }

        AdditionalApproval entity = new AdditionalApproval();
        entity.setId(dto.getId());

        // Связь с Approval НЕ устанавливаем здесь, чтобы избежать циклической зависимости
        // Установите approval в сервисе через approvalRepository.findById(dto.getApprovalId())

        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setComment(dto.getComment());
        entity.setResponseDate(dto.getResponseDate());

        return entity;
    }

    /**
     * Обновляет существующую Entity из DTO (для частичного обновления)
     */
    public void updateEntity(AdditionalApprovalDto dto, AdditionalApproval entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getApprovalId() != null) {
            // Только сохраняем ID, саму связь устанавливайте в сервисе
            // entity.setApproval(approval);
        }

        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }

        if (dto.getResponseDate() != null) {
            entity.setResponseDate(dto.getResponseDate());
        }
    }
}