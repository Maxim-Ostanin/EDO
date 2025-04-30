package common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppealDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createDate;

    public AppealDto(Long id, String title, String description) {
    }
}
