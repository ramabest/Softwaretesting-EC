package de.hsbremen.atm.session;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtmSessionRepository extends JpaRepository<AtmSession, UUID> {
}

