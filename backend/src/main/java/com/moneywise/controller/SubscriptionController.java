package com.moneywise.controller;

import com.moneywise.dto.SubscriptionRequest;
import com.moneywise.entity.Subscription;
import com.moneywise.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Subscription> create(Authentication auth, @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAll(Authentication auth) {
        return ResponseEntity.ok(subscriptionService.getAllByUser(auth.getName()));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Subscription>> getActive(Authentication auth) {
        return ResponseEntity.ok(subscriptionService.getActiveByUser(auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> update(Authentication auth, @PathVariable Long id,
                                                @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.update(auth.getName(), id, request));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Subscription> toggle(Authentication auth, @PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.toggleActive(auth.getName(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        subscriptionService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
