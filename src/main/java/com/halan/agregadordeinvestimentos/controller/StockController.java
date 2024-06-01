package com.halan.agregadordeinvestimentos.controller;

import com.halan.agregadordeinvestimentos.controller.dto.CreateStockDto;
import com.halan.agregadordeinvestimentos.entity.Stock;
import com.halan.agregadordeinvestimentos.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody CreateStockDto createStockDto) {
        var stock = stockService.createStock(createStockDto);
        return ResponseEntity.ok(stock);
    }
}
