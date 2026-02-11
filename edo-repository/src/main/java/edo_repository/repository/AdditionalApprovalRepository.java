package edo_repository.repository;


import edo_repository.entity.AdditionalApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalApprovalRepository extends JpaRepository <AdditionalApproval, Long> {

 boolean existsByApprovalIdAndType (Long approvalId, String type);



}
