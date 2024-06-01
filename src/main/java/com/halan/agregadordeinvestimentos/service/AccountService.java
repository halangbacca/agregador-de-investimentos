package com.halan.agregadordeinvestimentos.service;

import com.halan.agregadordeinvestimentos.client.BrapiClient;
import com.halan.agregadordeinvestimentos.controller.dto.AccountStockDto;
import com.halan.agregadordeinvestimentos.controller.dto.AccountStockResponseDto;
import com.halan.agregadordeinvestimentos.entity.AccountStock;
import com.halan.agregadordeinvestimentos.entity.AccountStockId;
import com.halan.agregadordeinvestimentos.repository.AccountRepository;
import com.halan.agregadordeinvestimentos.repository.AccountStockRepository;
import com.halan.agregadordeinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public AccountStock associateStock(String accountId, AccountStockDto accountStockDto) {

        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(accountStockDto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var id = new AccountStockId(
                account.getAccountId(),
                stock.getStockId());

        var entity = new AccountStock(
                id,
                account,
                stock,
                accountStockDto.quantity()
        );

        return accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStocks(String accountId) {

        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as -> new AccountStockResponseDto(
                        as.getStock().getStockId(),
                        as.getQuantity(),
                        getTotal(as.getQuantity(), as.getStock().getStockId())
                )).toList();
    }

    private double getTotal(Integer quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return price * quantity;
    }
}
