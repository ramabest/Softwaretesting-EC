package de.hsbremen.atm.card;

public record PinResult(boolean authenticated, boolean locked, int remainingAttempts) {
}

