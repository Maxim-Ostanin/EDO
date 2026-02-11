package edo_repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="additional_approval")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "approval_id", nullable = false)
    private Approval approval;

    private String type;
    private String status;
    private String comment;

    @Column(name = "response_date")
    private LocalDateTime responseDate;













}
