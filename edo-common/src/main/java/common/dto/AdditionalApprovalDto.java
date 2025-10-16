package common.dto;

import jakarta.validation.constraints.Size;
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

    @Size(max = 10, message = "Type must not exceed 10 characters")
    private String type;

    @Size(max = 40, message = "Status must not exceed 40 characters")
    private String status;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;

    private LocalDateTime responseDate;
}