package com.moneywise.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {
    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String category;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate expenseDate;
}
