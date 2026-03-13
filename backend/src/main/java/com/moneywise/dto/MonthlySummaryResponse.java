package com.moneywise.dto;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class MonthlySummaryResponse {
    private Integer month;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal totalSubscriptions;
    private BigDecimal totalDeductions;  // expenses + subscriptions
    private BigDecimal balance;           // income - deductions
    private BigDecimal totalSavings;      // total épargne tous comptes
    private Map<String, BigDecimal> expensesByCategory;
    private Map<String, BigDecimal> incomeByType;
}
