package common.dto.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ApprovalEventDto {
        private Long approvalId;
        private String status;
        private String comment;
        private LocalDateTime eventTime;
        private String eventType; // "CREATED", "UPDATED", "DELETED"
    }
