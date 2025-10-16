package edo_repository.repository;

import edo_repository.entity.AdditionalApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdditionalApprovalRepository extends JpaRepository<AdditionalApproval, Long> {

    Optional<AdditionalApproval> findByApprovalId(Long approvalId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM AdditionalApproval a WHERE a.approval.id = :approvalId AND a.type = :type")
    boolean existsByApprovalIdAndType(@Param("approvalId") Long approvalId, @Param("type") String type);
}