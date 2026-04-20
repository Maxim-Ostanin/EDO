package edo_repository.repository;

import edo_repository.entity.AdditionalApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalApprovalRepository extends JpaRepository<AdditionalApproval, Long> {
    boolean existsByApprovalIdAndTypeAndStatus(Long approvalId, String type, String status);
}