package common.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApprovalDto {

    private Long id;
    private Long approvalId;
    private String type;
    private String status;
    private String comment;
    private LocalDateTime responseDate;

    @Pattern(regexp = "^(APPROVED|REJECTED|PENDING)$",
            message = "status может быть только: APPROVED, REJECTED, PENDING")
    private String getStatus;
}
