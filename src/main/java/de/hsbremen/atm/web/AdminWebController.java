package de.hsbremen.atm.web;

import de.hsbremen.atm.account.AccountRepository;
import de.hsbremen.atm.account.AccountService;
import de.hsbremen.atm.card.CardRepository;
import de.hsbremen.atm.card.CardService;
import de.hsbremen.atm.card.CardStatus;
import de.hsbremen.atm.card.CardType;
import de.hsbremen.atm.cash.CashDispenserService;
import de.hsbremen.atm.common.MoneyFormatter;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminWebController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CardRepository cardRepository;
    private final CardService cardService;
    private final CashDispenserService cashDispenserService;

    public AdminWebController(AccountRepository accountRepository, AccountService accountService,
            CardRepository cardRepository, CardService cardService, CashDispenserService cashDispenserService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.cardRepository = cardRepository;
        this.cardService = cardService;
        this.cashDispenserService = cashDispenserService;
    }

    @GetMapping("/admin")
    String dashboard(Model model) {
        model.addAttribute("accountCount", accountRepository.count());
        model.addAttribute("cardCount", cardRepository.count());
        model.addAttribute("cashTotal", MoneyFormatter.format(cashDispenserService.totalCents()));
        return "admin/dashboard";
    }

    @GetMapping("/admin/accounts")
    String accounts(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "admin/accounts";
    }

    @PostMapping("/admin/accounts")
    String createAccount(@RequestParam String iban, @RequestParam String ownerFirstName,
            @RequestParam String ownerLastName, @RequestParam long balanceCents,
            @RequestParam long overdraftLimitCents, @RequestParam(defaultValue = "false") boolean active) {
        accountService.create(iban, ownerFirstName, ownerLastName, balanceCents, overdraftLimitCents, active);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/admin/accounts/{accountId}/status")
    String setAccountStatus(@PathVariable long accountId, @RequestParam boolean active) {
        accountService.setActive(accountId, active);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/cards")
    String cards(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        model.addAttribute("statuses", CardStatus.values());
        model.addAttribute("types", CardType.values());
        return "admin/cards";
    }

    @PostMapping("/admin/cards/{cardId}/pin")
    String setPin(@PathVariable long cardId, @RequestParam String pin) {
        cardService.setPin(cardId, pin);
        return "redirect:/admin/cards";
    }

    @PostMapping("/admin/cards/{cardId}")
    String updateCard(@PathVariable long cardId, @RequestParam CardType cardType,
            @RequestParam CardStatus status, @RequestParam LocalDate expiresOn) {
        cardService.update(cardId, cardType, status, expiresOn, null);
        return "redirect:/admin/cards";
    }

    @GetMapping("/admin/cassette")
    String cassette(Model model) {
        model.addAttribute("slots", cashDispenserService.slots());
        model.addAttribute("cashTotal", MoneyFormatter.format(cashDispenserService.totalCents()));
        return "admin/cassette";
    }

    @PostMapping("/admin/cassette")
    String updateCassette(@RequestParam Map<String, String> params) {
        Map<Integer, Integer> counts = params.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("denomination_"))
                .collect(java.util.stream.Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey().substring("denomination_".length())),
                        entry -> Integer.parseInt(entry.getValue())));
        cashDispenserService.update(counts);
        return "redirect:/admin/cassette";
    }
}

