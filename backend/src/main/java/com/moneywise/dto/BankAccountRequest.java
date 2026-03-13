package com.moneywise.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountRequest {
    @NotBlank
    private String bankName;

    @NotBlank
    private String accountName;

    @NotBlank
    private String accountType;

    @NotNull
    private BigDecimal balance;

    private Boolean isPrimary = false;
}
