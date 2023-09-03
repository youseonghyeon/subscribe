package com.example.subscribify.repository;

import com.example.subscribify.entity.SubscriptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfo, Long> {
}
