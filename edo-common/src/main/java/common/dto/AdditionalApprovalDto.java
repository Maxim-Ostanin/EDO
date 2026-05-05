package common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApprovalDto {

    public Long id;
    private Long approvalId;


    @Size(max = 10)
    private String type;

    @Pattern(regexp = "^(APPROVED|REJECTED|PENDING)$")
    private String status;  // ← одна аннотация над полем

    @Size(max = 500)
    private String comment;

    private LocalDateTime responseDate;
}











