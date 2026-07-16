package de.hsbremen.atm.withdrawal;

import de.hsbremen.atm.account.Account;
import de.hsbremen.atm.card.Card;
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
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "withdrawals")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private long amountCents;

    @Column(nullable = false)
    private long feeCents;

    @Column(nullable = false)
    private long totalDebitCents;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private LocalDate businessDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalStatus status;

    private String declineReason;

    protected Withdrawal() {
    }

    private Withdrawal(Card card, Account account, long amountCents, long feeCents,
            long totalDebitCents, LocalDate businessDate, WithdrawalStatus status, String declineReason) {
        this.card = card;
        this.account = account;
        this.amountCents = amountCents;
        this.feeCents = feeCents;
        this.totalDebitCents = totalDebitCents;
        this.businessDate = businessDate;
        this.status = status;
        this.declineReason = declineReason;
        this.createdAt = Instant.now();
    }

    public static Withdrawal approved(Card card, Account account, long amountCents, long feeCents,
            long totalDebitCents, LocalDate businessDate) {
        return new Withdrawal(card, account, amountCents, feeCents, totalDebitCents, businessDate, WithdrawalStatus.APPROVED, null);
    }

    public static Withdrawal declined(Card card, Account account, long amountCents, LocalDate businessDate, String reason) {
        return new Withdrawal(card, account, amountCents, 0, 0, businessDate, WithdrawalStatus.DECLINED, reason);
    }

    public Long getId() {
        return id;
    }

    public long getAmountCents() {
        return amountCents;
    }

    public long getFeeCents() {
        return feeCents;
    }

    public long getTotalDebitCents() {
        return totalDebitCents;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public String getDeclineReason() {
        return declineReason;
    }
}

