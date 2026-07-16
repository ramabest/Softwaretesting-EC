package de.hsbremen.atm.withdrawal;

import de.hsbremen.atm.card.Card;
import de.hsbremen.atm.common.BusinessClock;
import de.hsbremen.atm.common.DomainException;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DailyLimitService {

    public static final long DAILY_LIMIT_CENTS = 500_000L;

    private final WithdrawalRepository withdrawalRepository;
    private final BusinessClock businessClock;

    public DailyLimitService(WithdrawalRepository withdrawalRepository, BusinessClock businessClock) {
        this.withdrawalRepository = withdrawalRepository;
        this.businessClock = businessClock;
    }

    public LocalDate businessDate() {
        return businessClock.businessDate();
    }

    public void ensureWithinDailyLimit(Card card, long amountCents) {
        long alreadyWithdrawn = withdrawalRepository.sumApprovedAmountForCardAndBusinessDate(card.getId(), businessDate());
        if (alreadyWithdrawn + amountCents > DAILY_LIMIT_CENTS) {
            throw new DomainException("DAILY_LIMIT_EXCEEDED", "Tageslimit von 5.000 EUR wurde ueberschritten.", HttpStatus.CONFLICT);
        }
    }
}

