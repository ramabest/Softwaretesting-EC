package de.hsbremen.atm.session;

import de.hsbremen.atm.card.PinResult;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/atm")
public class AtmSessionController {

    private final AtmSessionService sessionService;

    public AtmSessionController(AtmSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/cards/insert")
    InsertCardResponse insertCard(@RequestBody InsertCardRequest request) {
        AtmSession session = sessionService.insertCard(request.cardNumber());
        return new InsertCardResponse(session.getId(), session.getCard().getCardType().name());
    }

    @PostMapping("/sessions/{sessionId}/language")
    void setLanguage(@PathVariable UUID sessionId, @RequestBody LanguageRequest request) {
        sessionService.setLanguage(sessionId, request.language());
    }

    @PostMapping("/sessions/{sessionId}/pin")
    PinResult verifyPin(@PathVariable UUID sessionId, @RequestBody PinRequest request) {
        return sessionService.verifyPin(sessionId, request.pin());
    }

    @PostMapping("/sessions/{sessionId}/finish")
    void finish(@PathVariable UUID sessionId) {
        sessionService.finish(sessionId);
    }

    public record InsertCardRequest(String cardNumber) {
    }

    public record InsertCardResponse(UUID sessionId, String cardType) {
    }

    public record LanguageRequest(String language) {
    }

    public record PinRequest(String pin) {
    }
}

