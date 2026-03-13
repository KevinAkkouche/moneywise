package com.moneywise.service;

import com.moneywise.dto.IncomeRequest;
import com.moneywise.entity.Income;
import com.moneywise.entity.User;
import com.moneywise.repository.IncomeRepository;
import com.moneywise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    public Income create(String username, IncomeRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Income income = new Income();
        income.setUser(user);
        income.setAmount(request.getAmount());
        income.setType(Income.IncomeType.valueOf(request.getType().toUpperCase()));
        income.setDescription(request.getDescription());
        income.setIncomeDate(request.getIncomeDate());
        income.setIsFixedSalary(request.getIsFixedSalary());
        income.setMonth(request.getIncomeDate().getMonthValue());
        income.setYear(request.getIncomeDate().getYear());

        return incomeRepository.save(income);
    }

    public List<Income> getAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return incomeRepository.findByUserIdOrderByIncomeDateDesc(user.getId());
    }

    public List<Income> getByMonth(String username, int month, int year) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return incomeRepository.findByUserIdAndMonthAndYearOrderByIncomeDateDesc(user.getId(), month, year);
    }

    public void delete(String username, Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revenu non trouvé"));
        if (!income.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        incomeRepository.delete(income);
    }

    public Income update(String username, Long id, IncomeRequest request) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revenu non trouvé"));
        if (!income.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        income.setAmount(request.getAmount());
        income.setType(Income.IncomeType.valueOf(request.getType().toUpperCase()));
        income.setDescription(request.getDescription());
        income.setIncomeDate(request.getIncomeDate());
        income.setIsFixedSalary(request.getIsFixedSalary());
        income.setMonth(request.getIncomeDate().getMonthValue());
        income.setYear(request.getIncomeDate().getYear());
        return incomeRepository.save(income);
    }
}
