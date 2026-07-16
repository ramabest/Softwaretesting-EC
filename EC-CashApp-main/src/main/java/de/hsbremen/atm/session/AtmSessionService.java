package de.hsbremen.atm.session;

import de.hsbremen.atm.card.Card;
import de.hsbremen.atm.card.CardService;
import de.hsbremen.atm.card.PinResult;
import de.hsbremen.atm.card.PinService;
import de.hsbremen.atm.common.DomainException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtmSessionService {

    private final AtmSessionRepository sessionRepository;
    private final CardService cardService;
    private final PinService pinService;

    public AtmSessionService(AtmSessionRepository sessionRepository, CardService cardService, PinService pinService) {
        this.sessionRepository = sessionRepository;
        this.cardService = cardService;
        this.pinService = pinService;
    }

    @Transactional
    public AtmSession insertCard(String cardNumber) {
        Card card = cardService.findUsableCard(cardNumber);
        return sessionRepository.save(new AtmSession(card));
    }

    public AtmSession get(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new DomainException("SESSION_NOT_FOUND", "ATM-Session wurde nicht gefunden.", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public AtmSession setLanguage(UUID sessionId, String language) {
        AtmSession session = get(sessionId);
        session.setLanguage("en".equalsIgnoreCase(language) ? "en" : "de");
        return session;
    }

    @Transactional
    public PinResult verifyPin(UUID sessionId, String pin) {
        AtmSession session = get(sessionId);
        PinResult result = pinService.verify(session.getCard(), pin);
        if (result.authenticated()) {
            session.authenticate();
        }
        return result;
    }

    public AtmSession requireAuthenticated(UUID sessionId) {
        AtmSession session = get(sessionId);
        if (!session.isAuthenticated()) {
            throw new DomainException("SESSION_NOT_AUTHENTICATED", "PIN wurde noch nicht erfolgreich geprueft.", HttpStatus.FORBIDDEN);
        }
        return session;
    }

    @Transactional
    public void finish(UUID sessionId) {
        get(sessionId).finish();
    }
}

