package common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApprovalCreateDto {

    @NotNull(message = "approvalId не может быть null")
    private Long approvalId;

    @Size(max = 10, message = "type не может быть длиннее 10 символов")
    private String type;

    @Size(max = 40, message = "status не может быть длиннее 40 символов")
    private String status;

    @Size(max = 500, message = "comment не может быть длиннее 500 символов")
    private String comment;
}