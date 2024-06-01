package com.halan.agregadordeinvestimentos.controller.dto;

public record CreateAccountDto(
        String description,
        String street,
        Integer number
) {
}
