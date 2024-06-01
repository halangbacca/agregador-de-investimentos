package com.halan.agregadordeinvestimentos.controller.dto;

import com.halan.agregadordeinvestimentos.entity.User;

public record CreateUserDto(
        String username,
        String email,
        String password
) {

    public User toUser() {
        return new User(username, email, password);
    }
}
