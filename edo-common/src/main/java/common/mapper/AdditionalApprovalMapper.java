package common.mapper;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;

public class AdditionalApprovalMapper {

    public static AdditionalApprovalDto toDto(AdditionalApproval entity) {
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

    public static AdditionalApproval toEntity(AdditionalApprovalDto dto) {
        if (dto == null) {
            return null;
        }

        AdditionalApproval entity = new AdditionalApproval();
        entity.setId(dto.getId());
        if (dto.getApprovalId() != null) {
            Approval approval = new Approval();
            approval.setId(dto.getApprovalId());
            entity.setApproval(approval);
        }
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setComment(dto.getComment());
        entity.setResponseDate(dto.getResponseDate());

        return entity;
    }
}