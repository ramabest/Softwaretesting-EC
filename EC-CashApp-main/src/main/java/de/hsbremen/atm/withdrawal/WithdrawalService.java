package de.hsbremen.atm.withdrawal;

import de.hsbremen.atm.account.Account;
import de.hsbremen.atm.cash.CashDispenserService;
import de.hsbremen.atm.common.DomainException;
import de.hsbremen.atm.session.AtmSession;
import de.hsbremen.atm.session.AtmSessionService;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawalService {

    private static final Set<Long> ALLOWED_AMOUNTS = Set.of(
            2_000L, 5_000L, 10_000L, 20_000L, 50_000L, 100_000L, 200_000L, 500_000L
    );

    private final AtmSessionService sessionService;
    private final FeeCalculator feeCalculator;
    private final DailyLimitService dailyLimitService;
    private final CashDispenserService cashDispenserService;
    private final WithdrawalRepository withdrawalRepository;

    public WithdrawalService(AtmSessionService sessionService, FeeCalculator feeCalculator,
            DailyLimitService dailyLimitService, CashDispenserService cashDispenserService,
            WithdrawalRepository withdrawalRepository) {
        this.sessionService = sessionService;
        this.feeCalculator = feeCalculator;
        this.dailyLimitService = dailyLimitService;
        this.cashDispenserService = cashDispenserService;
        this.withdrawalRepository = withdrawalRepository;
    }

    @Transactional
    public WithdrawalResult withdraw(java.util.UUID sessionId, long amountCents) {
        if (!ALLOWED_AMOUNTS.contains(amountCents)) {
            throw new DomainException("AMOUNT_NOT_ALLOWED", "Auszahlungsbetrag ist nicht erlaubt.", HttpStatus.BAD_REQUEST);
        }
        AtmSession session = sessionService.requireAuthenticated(sessionId);
        Account account = session.getAccount();
        long feeCents = feeCalculator.feeCents(session.getCard().getCardType(), amountCents);
        long totalDebitCents = amountCents + feeCents;

        dailyLimitService.ensureWithinDailyLimit(session.getCard(), amountCents);
        if (account.getBalanceCents() - totalDebitCents < -account.getOverdraftLimitCents()) {
            throw new DomainException("INSUFFICIENT_FUNDS", "Kontolimit reicht fuer diese Auszahlung nicht aus.", HttpStatus.CONFLICT);
        }

        Map<Integer, Integer> notes = cashDispenserService.dispense(amountCents);
        account.debit(totalDebitCents);
        withdrawalRepository.save(Withdrawal.approved(
                session.getCard(), account, amountCents, feeCents, totalDebitCents, dailyLimitService.businessDate()));
        return new WithdrawalResult(amountCents, feeCents, totalDebitCents, account.getBalanceCents(), notes);
    }
}

