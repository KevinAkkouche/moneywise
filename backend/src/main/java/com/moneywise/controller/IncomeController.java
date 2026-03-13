package com.moneywise.controller;

import com.moneywise.dto.IncomeRequest;
import com.moneywise.entity.Income;
import com.moneywise.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @PostMapping
    public ResponseEntity<Income> create(Authentication auth, @Valid @RequestBody IncomeRequest request) {
        return ResponseEntity.ok(incomeService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<Income>> getAll(Authentication auth) {
        return ResponseEntity.ok(incomeService.getAllByUser(auth.getName()));
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<Income>> getByMonth(Authentication auth,
                                                    @PathVariable int year,
                                                    @PathVariable int month) {
        return ResponseEntity.ok(incomeService.getByMonth(auth.getName(), month, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Income> update(Authentication auth, @PathVariable Long id,
                                          @Valid @RequestBody IncomeRequest request) {
        return ResponseEntity.ok(incomeService.update(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        incomeService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
