package com.halan.agregadordeinvestimentos.controller;

import com.halan.agregadordeinvestimentos.controller.dto.AccountStockDto;
import com.halan.agregadordeinvestimentos.controller.dto.AccountStockResponseDto;
import com.halan.agregadordeinvestimentos.entity.AccountStock;
import com.halan.agregadordeinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<AccountStock> associateStock(@PathVariable String accountId, @RequestBody AccountStockDto accountStockDto) {
        accountService.associateStock(accountId, accountStockDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> associateStock(@PathVariable String accountId) {
        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
