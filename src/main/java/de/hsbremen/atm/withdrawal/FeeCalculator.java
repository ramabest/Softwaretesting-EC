package de.hsbremen.atm.withdrawal;

import de.hsbremen.atm.card.CardType;
import org.springframework.stereotype.Component;

@Component
public class FeeCalculator {

    public long feeCents(CardType cardType, long amountCents) {
        if (cardType != CardType.FOREIGN_BANK) {
            return 0;
        }
        return Math.round((amountCents / 100) * 0.025) * 100;
    }
}

