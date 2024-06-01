package com.halan.agregadordeinvestimentos.service;

import com.halan.agregadordeinvestimentos.controller.dto.CreateStockDto;
import com.halan.agregadordeinvestimentos.entity.Stock;
import com.halan.agregadordeinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;


    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock createStock(CreateStockDto createStockDto) {
        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );
        return stockRepository.save(stock);
    }
}
