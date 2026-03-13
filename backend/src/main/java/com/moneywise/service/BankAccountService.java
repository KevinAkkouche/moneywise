package com.moneywise.service;

import com.moneywise.dto.BankAccountRequest;
import com.moneywise.entity.BankAccount;
import com.moneywise.entity.User;
import com.moneywise.repository.BankAccountRepository;
import com.moneywise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public BankAccount create(String username, BankAccountRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setBankName(request.getBankName());
        account.setAccountName(request.getAccountName());
        account.setAccountType(BankAccount.AccountType.valueOf(request.getAccountType().toUpperCase()));
        account.setBalance(request.getBalance());
        account.setIsPrimary(request.getIsPrimary());

        return bankAccountRepository.save(account);
    }

    public List<BankAccount> getAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return bankAccountRepository.findByUserId(user.getId());
    }

    public BankAccount update(String username, Long id, BankAccountRequest request) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        account.setBankName(request.getBankName());
        account.setAccountName(request.getAccountName());
        account.setAccountType(BankAccount.AccountType.valueOf(request.getAccountType().toUpperCase()));
        account.setBalance(request.getBalance());
        account.setIsPrimary(request.getIsPrimary());
        account.setUpdatedAt(LocalDateTime.now());
        return bankAccountRepository.save(account);
    }

    public void delete(String username, Long id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        bankAccountRepository.delete(account);
    }
}
