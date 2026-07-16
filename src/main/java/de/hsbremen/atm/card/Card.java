package de.hsbremen.atm.card;

import de.hsbremen.atm.account.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType;

    @Column(nullable = false)
    private String pinHash;

    @Column(nullable = false)
    private LocalDate expiresOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @Column(nullable = false)
    private int failedPinAttempts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Card() {
    }

    public Card(String cardNumber, CardType cardType, String pinHash, LocalDate expiresOn,
            CardStatus status, Account account) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.pinHash = pinHash;
        this.expiresOn = expiresOn;
        this.status = status;
        this.account = account;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getPinHash() {
        return pinHash;
    }

    public LocalDate getExpiresOn() {
        return expiresOn;
    }

    public CardStatus getStatus() {
        return status;
    }

    public int getFailedPinAttempts() {
        return failedPinAttempts;
    }

    public Account getAccount() {
        return account;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setExpiresOn(LocalDate expiresOn) {
        this.expiresOn = expiresOn;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void incrementFailedPinAttempts() {
        failedPinAttempts++;
    }

    public void resetFailedPinAttempts() {
        failedPinAttempts = 0;
    }
}
