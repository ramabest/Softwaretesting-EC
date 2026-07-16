package de.hsbremen.atm.cash;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cash_cassette_slots")
public class CashCassetteSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int denominationCents;

    @Column(nullable = false)
    private int noteCount;

    protected CashCassetteSlot() {
    }

    public CashCassetteSlot(int denominationCents, int noteCount) {
        this.denominationCents = denominationCents;
        this.noteCount = noteCount;
    }

    public Long getId() {
        return id;
    }

    public int getDenominationCents() {
        return denominationCents;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }
}

