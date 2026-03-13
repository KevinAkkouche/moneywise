package com.moneywise.controller;

import com.moneywise.dto.MonthlySummaryResponse;
import com.moneywise.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryResponse> getCurrentSummary(Authentication auth) {
        LocalDate now = LocalDate.now();
        return ResponseEntity.ok(dashboardService.getMonthlySummary(auth.getName(), now.getMonthValue(), now.getYear()));
    }

    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<MonthlySummaryResponse> getSummary(Authentication auth,
                                                              @PathVariable int year,
                                                              @PathVariable int month) {
        return ResponseEntity.ok(dashboardService.getMonthlySummary(auth.getName(), month, year));
    }
}
