package de.hsbremen.atm.cash;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashCassetteRepository extends JpaRepository<CashCassetteSlot, Long> {

    Optional<CashCassetteSlot> findByDenominationCents(int denominationCents);
}

