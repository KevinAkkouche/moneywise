package com.moneywise.repository;

import com.moneywise.entity.Income;
import com.moneywise.entity.Income.IncomeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUserIdOrderByIncomeDateDesc(Long userId);

    List<Income> findByUserIdAndMonthAndYearOrderByIncomeDateDesc(Long userId, Integer month, Integer year);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user.id = :userId AND i.month = :month AND i.year = :year")
    BigDecimal sumByUserIdAndMonthAndYear(@Param("userId") Long userId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user.id = :userId AND i.year = :year")
    BigDecimal sumByUserIdAndYear(@Param("userId") Long userId, @Param("year") Integer year);

    List<Income> findByUserIdAndType(Long userId, IncomeType type);
}
