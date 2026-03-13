package com.moneywise.controller;

import com.moneywise.dto.ExpenseRequest;
import com.moneywise.entity.Expense;
import com.moneywise.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> create(Authentication auth, @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAll(Authentication auth) {
        return ResponseEntity.ok(expenseService.getAllByUser(auth.getName()));
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<Expense>> getByMonth(Authentication auth,
                                                     @PathVariable int year,
                                                     @PathVariable int month) {
        return ResponseEntity.ok(expenseService.getByMonth(auth.getName(), month, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(Authentication auth, @PathVariable Long id,
                                           @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        expenseService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
