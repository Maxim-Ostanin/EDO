package edo_service.mapper;

import common.dto.ApprovalDto;
import edo_repository.entity.Appeal;
import edo_repository.entity.Approval;

public class ApprovalMapper {

    public static Approval toEntity(ApprovalDto dto, Appeal appeal) {
        return new Approval(
                dto.getId(),
                appeal,
                dto.getStatus(),
                dto.getComment(),
                dto.getResponseDate()
        );
    }
    public static ApprovalDto toDto(Approval entityApproval) {
        return new ApprovalDto(
                entityApproval.getId(),
                entityApproval.getAppeal().getId(),
                entityApproval.getStatus(),
                entityApproval.getComment(),
                entityApproval.getResponseDate()
        );
    }
}
