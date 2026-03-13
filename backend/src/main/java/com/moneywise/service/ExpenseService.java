package com.moneywise.service;

import com.moneywise.dto.ExpenseRequest;
import com.moneywise.entity.Expense;
import com.moneywise.entity.User;
import com.moneywise.repository.ExpenseRepository;
import com.moneywise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public Expense create(String username, ExpenseRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(request.getAmount());
        expense.setCategory(Expense.ExpenseCategory.valueOf(request.getCategory().toUpperCase()));
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setMonth(request.getExpenseDate().getMonthValue());
        expense.setYear(request.getExpenseDate().getYear());

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return expenseRepository.findByUserIdOrderByExpenseDateDesc(user.getId());
    }

    public List<Expense> getByMonth(String username, int month, int year) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return expenseRepository.findByUserIdAndMonthAndYearOrderByExpenseDateDesc(user.getId(), month, year);
    }

    public void delete(String username, Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dépense non trouvée"));
        if (!expense.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        expenseRepository.delete(expense);
    }

    public Expense update(String username, Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dépense non trouvée"));
        if (!expense.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        expense.setAmount(request.getAmount());
        expense.setCategory(Expense.ExpenseCategory.valueOf(request.getCategory().toUpperCase()));
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setMonth(request.getExpenseDate().getMonthValue());
        expense.setYear(request.getExpenseDate().getYear());
        return expenseRepository.save(expense);
    }
}
