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
    public String getStatus;

    public Long getApprovalId() {
        return 0L;
    }

    public String getComment() {
        return "";
    }

    public LocalDateTime getResponseDate() {
        return null;
    }

    public String getType() {
        return "";
    }

    public String getStatus() {
        return "";
    }

    public void setApprovalId(long l) {
    }

    public void setComment(String testComment) {
    }

    public void setStatus(String pending) {
    }

    public void setResponseDate(LocalDateTime now) {
    }

    public void setType(String postApproval) {
    }
}
