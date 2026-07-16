package de.hsbremen.atm.account;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/accounts")
public class AccountAdminController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AccountAdminController(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping
    List<AccountResponse> accounts() {
        return accountRepository.findAll().stream().map(AccountResponse::from).toList();
    }

    @PostMapping
    AccountResponse create(@RequestBody AccountRequest request) {
        return AccountResponse.from(accountService.create(request.iban(), request.ownerFirstName(), request.ownerLastName(),
                request.balanceCents(), request.overdraftLimitCents(), request.active()));
    }

    @PutMapping("/{accountId}")
    AccountResponse update(@PathVariable long accountId, @RequestBody AccountRequest request) {
        return AccountResponse.from(accountService.update(accountId, request.ownerFirstName(), request.ownerLastName(),
                request.balanceCents(), request.overdraftLimitCents(), request.active()));
    }

    @PostMapping("/{accountId}/deactivate")
    AccountResponse deactivate(@PathVariable long accountId) {
        return AccountResponse.from(accountService.setActive(accountId, false));
    }

    @PostMapping("/{accountId}/activate")
    AccountResponse activate(@PathVariable long accountId) {
        return AccountResponse.from(accountService.setActive(accountId, true));
    }

    public record AccountRequest(String iban, String ownerFirstName, String ownerLastName,
                                 long balanceCents, long overdraftLimitCents, boolean active) {
    }

    public record AccountResponse(long id, String iban, String ownerFirstName, String ownerLastName,
                                  long balanceCents, long overdraftLimitCents, boolean active) {
        static AccountResponse from(Account account) {
            return new AccountResponse(account.getId(), account.getIban(), account.getOwnerFirstName(),
                    account.getOwnerLastName(), account.getBalanceCents(), account.getOverdraftLimitCents(), account.isActive());
        }
    }
}

