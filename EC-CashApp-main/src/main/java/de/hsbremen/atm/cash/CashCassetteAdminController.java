package de.hsbremen.atm.cash;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cassette")
public class CashCassetteAdminController {

    private final CashDispenserService cashDispenserService;

    public CashCassetteAdminController(CashDispenserService cashDispenserService) {
        this.cashDispenserService = cashDispenserService;
    }

    @GetMapping
    CassetteResponse cassette() {
        return response(cashDispenserService.slots());
    }

    @PutMapping
    CassetteResponse update(@RequestBody Map<Integer, Integer> noteCounts) {
        return response(cashDispenserService.update(noteCounts));
    }

    private CassetteResponse response(List<CashCassetteSlot> slots) {
        return new CassetteResponse(slots.stream().map(SlotResponse::from).toList(), cashDispenserService.totalCents());
    }

    public record CassetteResponse(List<SlotResponse> slots, long totalCents) {
    }

    public record SlotResponse(int denominationCents, int noteCount) {
        static SlotResponse from(CashCassetteSlot slot) {
            return new SlotResponse(slot.getDenominationCents(), slot.getNoteCount());
        }
    }
}

