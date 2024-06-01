package com.halan.agregadordeinvestimentos.controller.dto;

public record AccountStockResponseDto(
        String stockId,
        int quantity,
        double total
) {
}
