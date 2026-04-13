package common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApprovalCreateDto {
    private Long approvalId;
    private String type;
    private String status;
    private String comment;
}