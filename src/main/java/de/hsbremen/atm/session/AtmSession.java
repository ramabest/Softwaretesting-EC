package de.hsbremen.atm.session;

import de.hsbremen.atm.account.Account;
import de.hsbremen.atm.card.Card;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "atm_sessions")
public class AtmSession {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private boolean authenticated;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant finishedAt;

    protected AtmSession() {
    }

    public AtmSession(Card card) {
        this.id = UUID.randomUUID();
        this.card = card;
        this.account = card.getAccount();
        this.language = "de";
        this.authenticated = false;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public Account getAccount() {
        return account;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void authenticate() {
        authenticated = true;
    }

    public void finish() {
        finishedAt = Instant.now();
    }
}
