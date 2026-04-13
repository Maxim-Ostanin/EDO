
package edo_repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalApprovalRepository<AdditionalApproval>
        extends JpaRepository<AdditionalApproval, Long> {
}