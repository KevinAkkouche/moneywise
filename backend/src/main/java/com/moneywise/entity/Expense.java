package com.moneywise.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer month;
    private Integer year;

    public enum ExpenseCategory {
        FOOD,           // Alimentation
        CLOTHING,       // Vêtements
        ELECTRONICS,    // Électronique
        TRANSPORT,      // Transport
        HEALTH,         // Santé
        ENTERTAINMENT,  // Loisirs
        EDUCATION,      // Éducation
        HOME,           // Maison
        OTHER           // Autre
    }
}
