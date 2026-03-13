package com.moneywise.repository;

import com.moneywise.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(b.balance), 0) FROM BankAccount b WHERE b.user.id = :userId")
    BigDecimal sumBalanceByUserId(@Param("userId") Long userId);
}
