package edo_service.mapper;

import common.dto.AdditionalApprovalDto;

import edo_repository.entity.Appeal;
import edo_repository.entity.Approval;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApprovalMapper {
    ApprovalMapper INSTANCE = Mappers.getMapper(ApprovalMapper.class);

    @Mapping(source = "appealId", target = "appeal.id")
    Approval toEntity(AdditionalApprovalDto dto);
    AdditionalApprovalDto toDto(Approval entity);
}
