package com.halan.agregadordeinvestimentos.controller;

import com.halan.agregadordeinvestimentos.controller.dto.AccountResponseDto;
import com.halan.agregadordeinvestimentos.controller.dto.CreateAccountDto;
import com.halan.agregadordeinvestimentos.controller.dto.CreateUserDto;
import com.halan.agregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.halan.agregadordeinvestimentos.entity.Account;
import com.halan.agregadordeinvestimentos.entity.User;
import com.halan.agregadordeinvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto dto) {
        var userId = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable String userId, @RequestBody CreateAccountDto dto) {
        userService.createAccount(userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        var user = userService.getUserById(userId);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAllAccountsByUserId(@PathVariable String userId) {
        var accounts = userService.listAccounts(userId);

        return ResponseEntity.ok(accounts);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateById(@PathVariable String userId, @RequestBody UpdateUserDto dto) {
        userService.updateById(userId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

}
