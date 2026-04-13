
package common.dto;

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
}

