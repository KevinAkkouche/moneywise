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
@Table(name = "incomes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Income {

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
    private IncomeType type;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate incomeDate;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // For salary: indicates if fixed or variable
    private Boolean isFixedSalary = false;

    // Month/Year for monthly grouping
    private Integer month;
    private Integer year;

    public enum IncomeType {
        SALARY,        // Salaire
        SALE,          // Vente
        GAMBLING,      // Jeux
        FREELANCE,     // Freelance
        INVESTMENT,    // Investissement
        OTHER          // Autre
    }
}
