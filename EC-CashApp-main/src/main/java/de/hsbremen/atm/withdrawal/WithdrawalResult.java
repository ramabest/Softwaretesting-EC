package de.hsbremen.atm.withdrawal;

import java.util.Map;

public record WithdrawalResult(long amountCents, long feeCents, long totalDebitCents,
                               long newBalanceCents, Map<Integer, Integer> notes) {
}

