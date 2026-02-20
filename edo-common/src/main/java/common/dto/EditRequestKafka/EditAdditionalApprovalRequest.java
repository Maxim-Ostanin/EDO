package common.dto.EditRequestKafka;

import common.dto.AdditionalApprovalDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditAdditionalApprovalRequest {
        private Long id;
        private AdditionalApprovalDto dto;
    }

