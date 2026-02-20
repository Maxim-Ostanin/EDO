package edo_service.converter;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AdditionalApprovalConverterDtoAndAddApp {

    public AdditionalApprovalDto toDto(AdditionalApproval addApp) {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setId(addApp.getId());
        dto.setType(addApp.getType());
        dto.setResponseDate(addApp.getResponseDate());
        dto.setComment(addApp.getComment());
        dto.setStatus(addApp.getStatus());

        Approval approval = addApp.getApproval();
        if (approval != null) {
            dto.setApprovalId(approval.getId());
        }
        return dto;
    }


    public AdditionalApproval toAddApp(AdditionalApprovalDto dto) {
        AdditionalApproval addApp = new AdditionalApproval();
        addApp.setId(dto.getId());
        addApp.setType(dto.getType());
        addApp.setResponseDate(dto.getResponseDate());
        addApp.setComment(dto.getComment());
        addApp.setStatus(dto.getStatus());

        if (dto.getApprovalId() != null) {
            Approval approval = new Approval();
            approval.setId(dto.getApprovalId());
            addApp.setApproval(approval);
        }
        return addApp;
    }
}
