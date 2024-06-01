package com.halan.agregadordeinvestimentos.service;

import com.halan.agregadordeinvestimentos.controller.dto.AccountResponseDto;
import com.halan.agregadordeinvestimentos.controller.dto.CreateAccountDto;
import com.halan.agregadordeinvestimentos.controller.dto.CreateUserDto;
import com.halan.agregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.halan.agregadordeinvestimentos.entity.Account;
import com.halan.agregadordeinvestimentos.entity.BillingAddress;
import com.halan.agregadordeinvestimentos.entity.User;
import com.halan.agregadordeinvestimentos.repository.AccountRepository;
import com.halan.agregadordeinvestimentos.repository.BillingAddressRepository;
import com.halan.agregadordeinvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDto dto) {
        var userSaved = userRepository.save(dto.toUser());
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateById(String id, UpdateUserDto dto) {
        var userEntity = userRepository.findById(UUID.fromString(id));

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (dto.username() != null) {
                user.setUsername(dto.username());
            }

            if (dto.password() != null) {
                user.setPassword(dto.password());
            }
            userRepository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto dto) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account(
                UUID.randomUUID(),
                dto.description(),
                new ArrayList<>(),
                user,
                null
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                dto.street(),
                dto.number()
        );

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts()
                .stream()
                .map(account -> new AccountResponseDto(account.getAccountId().toString(), account.getDescription()))
                .toList();
    }
}
