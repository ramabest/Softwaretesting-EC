package de.hsbremen.atm.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    public static final long MAX_CREDIT_CENTS = 10_000_000L;
    public static final long MAX_OVERDRAFT_CENTS = 5_000_000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private String ownerFirstName;

    @Column(nullable = false)
    private String ownerLastName;

    @Column(nullable = false)
    private long balanceCents;

    @Column(nullable = false)
    private long overdraftLimitCents;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Account() {
    }

    public Account(String iban, String ownerFirstName, String ownerLastName, long balanceCents,
            long overdraftLimitCents, boolean active) {
        this.iban = iban;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.balanceCents = balanceCents;
        this.overdraftLimitCents = overdraftLimitCents;
        this.active = active;
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

    public String getIban() {
        return iban;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public long getBalanceCents() {
        return balanceCents;
    }

    public long getOverdraftLimitCents() {
        return overdraftLimitCents;
    }

    public boolean isActive() {
        return active;
    }

    public void update(String ownerFirstName, String ownerLastName, long balanceCents,
            long overdraftLimitCents, boolean active) {
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.balanceCents = balanceCents;
        this.overdraftLimitCents = overdraftLimitCents;
        this.active = active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void debit(long cents) {
        this.balanceCents -= cents;
    }
}

