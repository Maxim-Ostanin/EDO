package edo_repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "additional_approval")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "approval_id", nullable = false, unique = true)
    private Approval approval;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "status", length = 40)
    private String status;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "response_date")
    private LocalDateTime responseDate;
}
