package common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AdditionalApprovalDto {

    private Long id;

    @NotNull (message = "ID согласования не может быть пустым")
    private Long approvalId;

    @Size(max = 10, message = "Тип не должен превышать 10 символов")
    private String type;

    @Size(max = 40, message = "Тип не должен превышать 40 символов")
    private String status;

    @Size(max = 500, message = "Тип не должен превышать 500 символов")
    private String comment;

    @NotNull
    private LocalDateTime responseDate;
}
