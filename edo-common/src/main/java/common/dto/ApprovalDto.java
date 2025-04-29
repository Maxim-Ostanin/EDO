package common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDto {
    private Long id;

    private AppealDto appeal;

    private String status;

    private String comment;

    private LocalDateTime responseDate;
}
