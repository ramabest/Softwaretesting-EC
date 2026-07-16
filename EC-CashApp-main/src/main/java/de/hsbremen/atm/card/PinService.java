package de.hsbremen.atm.card;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PinService {

    private final CardRepository cardRepository;

    public PinService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public PinResult verify(Card card, String pin) {
        if (card.getStatus() == CardStatus.BLOCKED) {
            return new PinResult(false, true, 0);
        }
        if (card.getPinHash().equals(pin)) {
            card.resetFailedPinAttempts();
            cardRepository.save(card);
            return new PinResult(true, false, 3);
        }
        card.incrementFailedPinAttempts();
        cardRepository.save(card);
        int remaining = Math.max(0, 3 - card.getFailedPinAttempts());
        return new PinResult(false, card.getFailedPinAttempts() >= 3, remaining);
    }
}

