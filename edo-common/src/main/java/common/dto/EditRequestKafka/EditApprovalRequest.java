package common.dto.EditRequestKafka;

import common.dto.ApprovalDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditApprovalRequest {
        private Long id;
        private ApprovalDto dto;
    }

