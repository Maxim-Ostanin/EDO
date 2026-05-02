package edo_service.converter;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import org.springframework.stereotype.Component;

@Component
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


        if (entity.getApproval() != null) {
            dto.setApprovalId(entity.getApproval().getId());  // ← setApprovalId, а не getApproval
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
}

