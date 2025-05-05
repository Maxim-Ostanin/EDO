package common.dto;

import edo_repository.entity.enums.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDto {
    private Long id;

    @NotNull(message = "Идентификатор обращения обязателен")
    private Long appealId;

    @NotNull(message = "Статус обязателен")
    private ApprovalStatus status;

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(max = 500, message = "Комментарий не может превышать 500 символов")
    private String comment;

    @NotNull(message = "Дата ответа обязательна")
    @PastOrPresent(message = "Дата ответа не может быть из будущего")
    private LocalDateTime responseDate;
}
