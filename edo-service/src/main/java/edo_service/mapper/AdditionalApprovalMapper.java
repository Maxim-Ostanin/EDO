package edo_service.mapper;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



    @Mapper(componentModel = "spring")
    public interface AdditionalApprovalMapper {

        @Mapping(source = "approval.id", target = "approvalId")
        AdditionalApprovalDto toDto(AdditionalApproval entity);
    }

