package de.hsbremen.atm.withdrawal;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    @Query("""
            select coalesce(sum(w.amountCents), 0)
            from Withdrawal w
            where w.card.id = :cardId
              and w.businessDate = :businessDate
              and w.status = de.hsbremen.atm.withdrawal.WithdrawalStatus.APPROVED
            """)
    long sumApprovedAmountForCardAndBusinessDate(@Param("cardId") long cardId, @Param("businessDate") LocalDate businessDate);
}

