package common.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApprovalEventDto {
    private Long additionalApprovalId;
    private Long approvalId;
    private String type;
    private String status;
    private LocalDateTime eventTime;
    private String eventType;
}