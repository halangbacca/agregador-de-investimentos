package com.halan.agregadordeinvestimentos.repository;

import com.halan.agregadordeinvestimentos.entity.AccountStock;
import com.halan.agregadordeinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
