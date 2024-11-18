package data;

import models.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalsRepository extends JpaRepository<Proposal, Integer> {
    boolean existsByCustomerIdAndPending(Integer customerId, boolean pending);
}
