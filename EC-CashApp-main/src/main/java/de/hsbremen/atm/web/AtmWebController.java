package de.hsbremen.atm.web;

import de.hsbremen.atm.card.Card;
import de.hsbremen.atm.card.CardRepository;
import de.hsbremen.atm.card.PinResult;
import de.hsbremen.atm.common.DomainException;
import de.hsbremen.atm.common.MoneyFormatter;
import de.hsbremen.atm.session.AtmSession;
import de.hsbremen.atm.session.AtmSessionService;
import de.hsbremen.atm.withdrawal.WithdrawalResult;
import de.hsbremen.atm.withdrawal.WithdrawalService;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AtmWebController {

    private final AtmSessionService sessionService;
    private final WithdrawalService withdrawalService;
    private final CardRepository cardRepository;

    public AtmWebController(AtmSessionService sessionService, WithdrawalService withdrawalService,
            CardRepository cardRepository) {
        this.sessionService = sessionService;
        this.withdrawalService = withdrawalService;
        this.cardRepository = cardRepository;
    }

    @GetMapping("/atm")
    String insertCard(Model model) {
        model.addAttribute("cardOptions", cardRepository.findAll().stream()
                .sorted(Comparator.comparing(Card::getCardNumber))
                .map(CardOption::from)
                .toList());
        return "atm/insert-card";
    }

    @PostMapping("/atm/insert")
    String insert(@RequestParam String cardNumber, Model model) {
        try {
            AtmSession session = sessionService.insertCard(cardNumber);
            return "redirect:/atm/" + session.getId() + "/language";
        } catch (DomainException exception) {
            model.addAttribute("error", exception.getMessage());
            return insertCard(model);
        }
    }

    @GetMapping("/atm/{sessionId}/language")
    String language(@PathVariable UUID sessionId, Model model) {
        model.addAttribute("sessionId", sessionId);
        return "atm/language";
    }

    @PostMapping("/atm/{sessionId}/language")
    String setLanguage(@PathVariable UUID sessionId, @RequestParam String language) {
        sessionService.setLanguage(sessionId, language);
        return "redirect:/atm/" + sessionId + "/pin";
    }

    @GetMapping("/atm/{sessionId}/pin")
    String pin(@PathVariable UUID sessionId, Model model) {
        model.addAttribute("sessionId", sessionId);
        return "atm/pin";
    }

    @PostMapping("/atm/{sessionId}/pin")
    String verifyPin(@PathVariable UUID sessionId, @RequestParam String pin, Model model) {
        PinResult result = sessionService.verifyPin(sessionId, pin);
        if (result.authenticated()) {
            return "redirect:/atm/" + sessionId + "/menu";
        }
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("error", result.locked()
                ? "Karte gesperrt."
                : "Falsche PIN. Verbleibende Versuche: " + result.remainingAttempts());
        return "atm/pin";
    }

    @GetMapping("/atm/{sessionId}/menu")
    String menu(@PathVariable UUID sessionId, Model model) {
        sessionService.requireAuthenticated(sessionId);
        model.addAttribute("sessionId", sessionId);
        return "atm/menu";
    }

    @GetMapping("/atm/{sessionId}/balance")
    String balance(@PathVariable UUID sessionId, Model model) {
        AtmSession session = sessionService.requireAuthenticated(sessionId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("balance", MoneyFormatter.format(session.getAccount().getBalanceCents()));
        return "atm/balance";
    }

    @GetMapping("/atm/{sessionId}/withdraw")
    String withdraw(@PathVariable UUID sessionId, Model model) {
        sessionService.requireAuthenticated(sessionId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("amounts", List.of(2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000));
        return "atm/withdraw";
    }

    @PostMapping("/atm/{sessionId}/withdraw")
    String doWithdraw(@PathVariable UUID sessionId, @RequestParam long amountCents, Model model) {
        try {
            WithdrawalResult result = withdrawalService.withdraw(sessionId, amountCents);
            model.addAttribute("sessionId", sessionId);
            model.addAttribute("result", result);
            model.addAttribute("amount", MoneyFormatter.format(result.amountCents()));
            model.addAttribute("fee", MoneyFormatter.format(result.feeCents()));
            model.addAttribute("balance", MoneyFormatter.format(result.newBalanceCents()));
            return "atm/result";
        } catch (DomainException exception) {
            model.addAttribute("error", exception.getMessage());
            return withdraw(sessionId, model);
        }
    }

    record CardOption(String cardNumber, String label) {
        static CardOption from(Card card) {
            String owner = card.getAccount() == null
                    ? "kein Konto"
                    : card.getAccount().getOwnerFirstName() + " " + card.getAccount().getOwnerLastName();
            return new CardOption(card.getCardNumber(),
                    card.getCardNumber() + " - " + owner + " - " + card.getCardType() + " - " + card.getStatus());
        }
    }
}
