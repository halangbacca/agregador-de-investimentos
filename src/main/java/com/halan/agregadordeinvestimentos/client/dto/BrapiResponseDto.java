package com.halan.agregadordeinvestimentos.client.dto;

import java.util.List;

public record BrapiResponseDto(
        List<StockDto> results
) {
}
