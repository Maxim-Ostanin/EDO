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









    //FIXME: Все эти методы тоже не нужны потому что ты используешь @Data которая сгенерирует все геттеры и сеттеры сама.
    //FIXME: А так ты получается переопределила то что система сделала бы правильно, но переопределила пустыми отдающими пустоту,
    //FIXME: В итоге после конвертации ты потеряешь все данные, все поля окажутся пустыми и null. Это серьезная проблема


