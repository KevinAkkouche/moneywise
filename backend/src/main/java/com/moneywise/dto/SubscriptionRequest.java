package com.moneywise.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SubscriptionRequest {
    @NotBlank
    private String name;

    @NotNull
    private BigDecimal monthlyAmount;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private Integer durationMonths;
}
