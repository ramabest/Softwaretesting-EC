package de.hsbremen.atm.cash;

import de.hsbremen.atm.common.DomainException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashDispenserService {

    private final CashCassetteRepository repository;

    public CashDispenserService(CashCassetteRepository repository) {
        this.repository = repository;
    }

    public List<CashCassetteSlot> slots() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(CashCassetteSlot::getDenominationCents).reversed())
                .toList();
    }

    public long totalCents() {
        return slots().stream()
                .mapToLong(slot -> (long) slot.getDenominationCents() * slot.getNoteCount())
                .sum();
    }

    @Transactional
    public Map<Integer, Integer> dispense(long amountCents) {
        Map<Integer, Integer> plan = plan(amountCents);
        for (CashCassetteSlot slot : repository.findAll()) {
            Integer used = plan.get(slot.getDenominationCents());
            if (used != null) {
                slot.setNoteCount(slot.getNoteCount() - used);
            }
        }
        return plan;
    }

    public Map<Integer, Integer> plan(long amountCents) {
        long remaining = amountCents;
        Map<Integer, Integer> plan = new LinkedHashMap<>();
        List<CashCassetteSlot> sorted = new ArrayList<>(slots());
        for (CashCassetteSlot slot : sorted) {
            int notes = (int) Math.min(slot.getNoteCount(), remaining / slot.getDenominationCents());
            if (notes > 0) {
                plan.put(slot.getDenominationCents(), notes);
                remaining -= (long) notes * slot.getDenominationCents();
            }
        }
        if (remaining != 0) {
            throw new DomainException("CASH_NOT_AVAILABLE", "Betrag kann mit vorhandenen Scheinen nicht ausgezahlt werden.", HttpStatus.CONFLICT);
        }
        return plan;
    }

    @Transactional
    public List<CashCassetteSlot> update(Map<Integer, Integer> noteCounts) {
        for (Map.Entry<Integer, Integer> entry : noteCounts.entrySet()) {
            if (entry.getValue() < 0) {
                throw new DomainException("NEGATIVE_NOTES", "Anzahl der Scheine darf nicht negativ sein.", HttpStatus.BAD_REQUEST);
            }
            CashCassetteSlot slot = repository.findByDenominationCents(entry.getKey())
                    .orElseThrow(() -> new DomainException("DENOMINATION_UNKNOWN", "Scheinart ist unbekannt.", HttpStatus.BAD_REQUEST));
            slot.setNoteCount(entry.getValue());
        }
        return slots();
    }
}

