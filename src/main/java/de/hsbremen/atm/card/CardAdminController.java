package de.hsbremen.atm.card;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cards")
public class CardAdminController {

    private final CardRepository cardRepository;
    private final CardService cardService;

    public CardAdminController(CardRepository cardRepository, CardService cardService) {
        this.cardRepository = cardRepository;
        this.cardService = cardService;
    }

    @GetMapping
    List<CardResponse> cards() {
        return cardRepository.findAll().stream().map(CardResponse::from).toList();
    }

    @PutMapping("/{cardId}")
    CardResponse update(@PathVariable long cardId, @RequestBody CardRequest request) {
        return CardResponse.from(cardService.update(cardId, request.cardType(), request.status(), request.expiresOn(), request.accountId()));
    }

    @PostMapping("/{cardId}/pin")
    CardResponse setPin(@PathVariable long cardId, @RequestBody PinRequest request) {
        return CardResponse.from(cardService.setPin(cardId, request.pin()));
    }

    @PostMapping("/{cardId}/reset-attempts")
    CardResponse resetAttempts(@PathVariable long cardId) {
        return CardResponse.from(cardService.resetFailedAttempts(cardId));
    }

    public record CardRequest(CardType cardType, CardStatus status, LocalDate expiresOn, Long accountId) {
    }

    public record PinRequest(String pin) {
    }

    public record CardResponse(long id, String cardNumber, CardType cardType, CardStatus status,
                               LocalDate expiresOn, int failedPinAttempts, Long accountId) {
        static CardResponse from(Card card) {
            Long accountId = card.getAccount() == null ? null : card.getAccount().getId();
            return new CardResponse(card.getId(), card.getCardNumber(), card.getCardType(), card.getStatus(),
                    card.getExpiresOn(), card.getFailedPinAttempts(), accountId);
        }
    }
}

