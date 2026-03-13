package com.moneywise.repository;

import com.moneywise.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);

    List<Subscription> findByUserIdAndIsActive(Long userId, Boolean isActive);

    @Query("SELECT COALESCE(SUM(s.monthlyAmount), 0) FROM Subscription s WHERE s.user.id = :userId AND s.isActive = true AND (s.endDate IS NULL OR s.endDate >= :date)")
    BigDecimal sumActiveMonthlyByUserId(@Param("userId") Long userId, @Param("date") LocalDate date);
}
