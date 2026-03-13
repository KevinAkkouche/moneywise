package com.moneywise.repository;

import com.moneywise.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdOrderByExpenseDateDesc(Long userId);

    List<Expense> findByUserIdAndMonthAndYearOrderByExpenseDateDesc(Long userId, Integer month, Integer year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.month = :month AND e.year = :year")
    BigDecimal sumByUserIdAndMonthAndYear(@Param("userId") Long userId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.year = :year")
    BigDecimal sumByUserIdAndYear(@Param("userId") Long userId, @Param("year") Integer year);

    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.month = :month AND e.year = :year GROUP BY e.category")
    List<Object[]> sumByCategoryForMonth(@Param("userId") Long userId, @Param("month") Integer month, @Param("year") Integer year);
}
