package de.hsbremen.atm.withdrawal;

import de.hsbremen.atm.account.Account;
import de.hsbremen.atm.common.MoneyFormatter;
import de.hsbremen.atm.session.AtmSession;
import de.hsbremen.atm.session.AtmSessionService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/atm/sessions/{sessionId}")
public class WithdrawalController {

    private final AtmSessionService sessionService;
    private final WithdrawalService withdrawalService;

    public WithdrawalController(AtmSessionService sessionService, WithdrawalService withdrawalService) {
        this.sessionService = sessionService;
        this.withdrawalService = withdrawalService;
    }

    @GetMapping("/balance")
    BalanceResponse balance(@PathVariable UUID sessionId) {
        AtmSession session = sessionService.requireAuthenticated(sessionId);
        Account account = session.getAccount();
        if (!account.isActive()) {
            throw new de.hsbremen.atm.common.DomainException("ACCOUNT_INACTIVE", "Konto ist deaktiviert.", org.springframework.http.HttpStatus.FORBIDDEN);
        }
        return new BalanceResponse(account.getBalanceCents(), MoneyFormatter.format(account.getBalanceCents()));
    }

    @PostMapping("/withdrawals")
    WithdrawalResult withdraw(@PathVariable UUID sessionId, @RequestBody WithdrawalRequest request) {
        return withdrawalService.withdraw(sessionId, request.amountCents());
    }

    public record BalanceResponse(long balanceCents, String formattedBalance) {
    }

    public record WithdrawalRequest(long amountCents) {
    }
}

