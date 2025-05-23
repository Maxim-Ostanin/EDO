package edo_repository.entity;

import edo_repository.entity.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appeal_id", nullable = false)
    private Appeal appeal;

    @Enumerated(EnumType.STRING)
    @Column(length = 40, nullable = false)
    private ApprovalStatus status;

    @Column(length = 500, nullable = false)
    private String comment;

    @Column(name = "response_date", nullable = false)
    private LocalDateTime responseDate;
}

