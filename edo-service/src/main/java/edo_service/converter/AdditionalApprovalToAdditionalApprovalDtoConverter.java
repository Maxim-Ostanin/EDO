package edo_service.converter;

import edo_repository.entity.AdditionalApproval;
import common.dto.AdditionalApprovalDto;
import org.springframework.stereotype.Component;

@Component
public class AdditionalApprovalToAdditionalApprovalDtoConverter {


    public AdditionalApprovalDto toDto(AdditionalApproval entity) {
        if (entity == null) {
            return null;
        }

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setId(entity.getId());
        dto.setApprovalId(entity.getApproval() != null ? entity.getApproval().getId() : null);
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setComment(entity.getComment());
        dto.setResponseDate(entity.getResponseDate());

        return dto;
    }


    public AdditionalApproval toEntity(AdditionalApprovalDto dto) {
        if (dto == null) {
            return null;
        }

        AdditionalApproval entity = new AdditionalApproval();
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setComment(dto.getComment());
        entity.setResponseDate(dto.getResponseDate());

        return entity;
    }


    public AdditionalApproval updateEntityFromDto(AdditionalApprovalDto dto, AdditionalApproval entity) {
        if (dto == null || entity == null) {
            return entity;
        }

        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setComment(dto.getComment());
        entity.setResponseDate(dto.getResponseDate());

        return entity;
    }
}