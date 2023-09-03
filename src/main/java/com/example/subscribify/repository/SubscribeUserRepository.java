package com.example.subscribify.repository;

import com.example.subscribify.entity.SubscribeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeUserRepository extends JpaRepository<SubscribeUser, Long> {
}
