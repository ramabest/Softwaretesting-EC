package de.hsbremen.atm.account;

import de.hsbremen.atm.common.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account get(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new DomainException("ACCOUNT_NOT_FOUND", "Konto wurde nicht gefunden.", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Account create(String iban, String firstName, String lastName, long balanceCents,
            long overdraftLimitCents, boolean active) {
        validateMoney(balanceCents, overdraftLimitCents);
        if (accountRepository.existsByIban(iban)) {
            throw new DomainException("IBAN_EXISTS", "IBAN ist bereits vorhanden.", HttpStatus.CONFLICT);
        }
        return accountRepository.save(new Account(iban, firstName, lastName, balanceCents, overdraftLimitCents, active));
    }

    @Transactional
    public Account update(long id, String firstName, String lastName, long balanceCents,
            long overdraftLimitCents, boolean active) {
        validateMoney(balanceCents, overdraftLimitCents);
        Account account = get(id);
        account.update(firstName, lastName, balanceCents, overdraftLimitCents, active);
        return account;
    }

    @Transactional
    public Account setActive(long id, boolean active) {
        Account account = get(id);
        account.setActive(active);
        return account;
    }

    public void ensureUsable(Account account) {
        if (!account.isActive()) {
            throw new DomainException("ACCOUNT_INACTIVE", "Konto ist deaktiviert.", HttpStatus.FORBIDDEN);
        }
    }

    private static void validateMoney(long balanceCents, long overdraftLimitCents) {
        if (balanceCents > Account.MAX_CREDIT_CENTS) {
            throw new DomainException("BALANCE_TOO_HIGH", "Guthaben darf hoechstens 100.000 EUR betragen.", HttpStatus.BAD_REQUEST);
        }
        if (overdraftLimitCents < 0 || overdraftLimitCents > Account.MAX_OVERDRAFT_CENTS) {
            throw new DomainException("OVERDRAFT_INVALID", "Ueberziehungslimit darf hoechstens 50.000 EUR betragen.", HttpStatus.BAD_REQUEST);
        }
    }
}

