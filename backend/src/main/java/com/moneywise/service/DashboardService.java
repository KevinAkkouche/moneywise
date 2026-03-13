package com.moneywise.service;

import com.moneywise.dto.MonthlySummaryResponse;
import com.moneywise.entity.User;
import com.moneywise.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public MonthlySummaryResponse getMonthlySummary(String username, int month, int year) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Long userId = user.getId();

        BigDecimal totalIncome = incomeRepository.sumByUserIdAndMonthAndYear(userId, month, year);
        BigDecimal totalExpenses = expenseRepository.sumByUserIdAndMonthAndYear(userId, month, year);

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        BigDecimal totalSubscriptions = subscriptionRepository.sumActiveMonthlyByUserId(userId, firstDayOfMonth);

        BigDecimal totalDeductions = totalExpenses.add(totalSubscriptions);
        BigDecimal balance = totalIncome.subtract(totalDeductions);
        BigDecimal totalSavings = bankAccountRepository.sumBalanceByUserId(userId);

        // Expenses by category
        Map<String, BigDecimal> expensesByCategory = new HashMap<>();
        List<Object[]> categoryData = expenseRepository.sumByCategoryForMonth(userId, month, year);
        for (Object[] row : categoryData) {
            expensesByCategory.put(row[0].toString(), (BigDecimal) row[1]);
        }

        return MonthlySummaryResponse.builder()
                .month(month)
                .year(year)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .totalSubscriptions(totalSubscriptions)
                .totalDeductions(totalDeductions)
                .balance(balance)
                .totalSavings(totalSavings)
                .expensesByCategory(expensesByCategory)
                .build();
    }
}
