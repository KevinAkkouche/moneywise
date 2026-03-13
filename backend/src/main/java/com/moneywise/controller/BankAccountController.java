package com.moneywise.controller;

import com.moneywise.dto.BankAccountRequest;
import com.moneywise.entity.BankAccount;
import com.moneywise.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<BankAccount> create(Authentication auth, @Valid @RequestBody BankAccountRequest request) {
        return ResponseEntity.ok(bankAccountService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<BankAccount>> getAll(Authentication auth) {
        return ResponseEntity.ok(bankAccountService.getAllByUser(auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> update(Authentication auth, @PathVariable Long id,
                                               @Valid @RequestBody BankAccountRequest request) {
        return ResponseEntity.ok(bankAccountService.update(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        bankAccountService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
