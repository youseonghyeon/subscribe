package com.example.subscribify.repository;

import com.example.subscribify.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long userId);
    Page<Application> findByUserId(Long userId, Pageable pageable);

    @Query("select a from Application a left join fetch a.subscriptionPlans where a.id = :applicationId")
    Optional<Application> findByIdWithSubscriptionPlans(Long applicationId);

    Optional<Application> findByApiKey(String authorization);
}
