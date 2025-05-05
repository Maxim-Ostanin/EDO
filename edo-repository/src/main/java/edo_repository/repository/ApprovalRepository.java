package edo_repository.repository;

import edo_repository.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    Optional<Approval> findByAppealIdAndStatusAndResponseDate
            (Long appealId, String status, LocalDateTime responseDate);

}
