package de.hsbremen.atm.card;

import de.hsbremen.atm.account.Account;
import de.hsbremen.atm.account.AccountService;
import de.hsbremen.atm.common.BusinessClock;
import de.hsbremen.atm.common.DomainException;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final BusinessClock businessClock;

    public CardService(CardRepository cardRepository, AccountService accountService, BusinessClock businessClock) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.businessClock = businessClock;
    }

    public Card get(long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new DomainException("CARD_NOT_FOUND", "Karte wurde nicht gefunden.", HttpStatus.NOT_FOUND));
    }

    public Card findUsableCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new DomainException("CARD_UNKNOWN", "Karte ist unbekannt.", HttpStatus.NOT_FOUND));
        ensureUsable(card);
        return card;
    }

    public void ensureUsable(Card card) {
        if (card.getCardType() == CardType.INVALID || card.getStatus() == CardStatus.INVALID) {
            throw new DomainException("CARD_INVALID", "Karte ist ungueltig.", HttpStatus.FORBIDDEN);
        }
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new DomainException("CARD_BLOCKED", "Karte ist gesperrt.", HttpStatus.FORBIDDEN);
        }
        LocalDate today = businessClock.now().atZone(BusinessClock.BUSINESS_ZONE).toLocalDate();
        if (card.getStatus() == CardStatus.EXPIRED || card.getExpiresOn().isBefore(today)) {
            throw new DomainException("CARD_EXPIRED", "Karte ist abgelaufen.", HttpStatus.FORBIDDEN);
        }
        Account account = card.getAccount();
        if (account == null) {
            throw new DomainException("CARD_WITHOUT_ACCOUNT", "Karte ist keinem Konto zugeordnet.", HttpStatus.FORBIDDEN);
        }
        accountService.ensureUsable(account);
    }

    @Transactional
    public Card update(long id, CardType cardType, CardStatus status, LocalDate expiresOn, Long accountId) {
        Card card = get(id);
        card.setCardType(cardType);
        card.setStatus(status);
        card.setExpiresOn(expiresOn);
        if (accountId != null) {
            card.setAccount(accountService.get(accountId));
        }
        return card;
    }

    @Transactional
    public Card setPin(long id, String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new DomainException("PIN_INVALID", "PIN muss vierstellig numerisch sein.", HttpStatus.BAD_REQUEST);
        }
        Card card = get(id);
        card.setPinHash(pin);
        card.resetFailedPinAttempts();
        return card;
    }

    @Transactional
    public Card resetFailedAttempts(long id) {
        Card card = get(id);
        card.resetFailedPinAttempts();
        return card;
    }
}

