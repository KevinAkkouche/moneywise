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
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionCategory category;

    @Column(nullable = false)
    private LocalDate startDate;

    // null = indefinite
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Duration in months (null = unlimited)
    private Integer durationMonths;

    public enum SubscriptionCategory {
        ELECTRICITY,    // Électricité
        INTERNET,       // Internet
        MOBILE,         // Mobile
        STREAMING,      // Streaming (Netflix, Spotify...)
        RENT,           // Loyer
        INSURANCE,      // Assurance
        GYM,            // Sport
        NEWSPAPER,      // Presse
        CLOUD,          // Cloud
        OTHER           // Autre
    }
}
