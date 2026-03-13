package com.moneywise.service;

import com.moneywise.dto.SubscriptionRequest;
import com.moneywise.entity.Subscription;
import com.moneywise.entity.User;
import com.moneywise.repository.SubscriptionRepository;
import com.moneywise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public Subscription create(String username, SubscriptionRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setName(request.getName());
        subscription.setMonthlyAmount(request.getMonthlyAmount());
        subscription.setCategory(Subscription.SubscriptionCategory.valueOf(request.getCategory().toUpperCase()));
        subscription.setStartDate(request.getStartDate());
        subscription.setEndDate(request.getEndDate());
        subscription.setDurationMonths(request.getDurationMonths());

        // Compute endDate from durationMonths if provided
        if (request.getDurationMonths() != null && request.getEndDate() == null) {
            subscription.setEndDate(request.getStartDate().plusMonths(request.getDurationMonths()));
        }

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return subscriptionRepository.findByUserId(user.getId());
    }

    public List<Subscription> getActiveByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return subscriptionRepository.findByUserIdAndIsActive(user.getId(), true);
    }

    public Subscription toggleActive(String username, Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));
        if (!subscription.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        subscription.setIsActive(!subscription.getIsActive());
        return subscriptionRepository.save(subscription);
    }

    public void delete(String username, Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));
        if (!subscription.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        subscriptionRepository.delete(subscription);
    }

    public Subscription update(String username, Long id, SubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));
        if (!subscription.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }
        subscription.setName(request.getName());
        subscription.setMonthlyAmount(request.getMonthlyAmount());
        subscription.setCategory(Subscription.SubscriptionCategory.valueOf(request.getCategory().toUpperCase()));
        subscription.setStartDate(request.getStartDate());
        subscription.setDurationMonths(request.getDurationMonths());
        if (request.getDurationMonths() != null) {
            subscription.setEndDate(request.getStartDate().plusMonths(request.getDurationMonths()));
        } else {
            subscription.setEndDate(request.getEndDate());
        }
        return subscriptionRepository.save(subscription);
    }
}
