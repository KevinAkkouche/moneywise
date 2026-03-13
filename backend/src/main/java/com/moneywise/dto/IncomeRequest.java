package com.moneywise.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IncomeRequest {
    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String type;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate incomeDate;

    private Boolean isFixedSalary = false;
}
