package com.example.subscribify.config;

import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 정기 결제를 담당하는 Quartz 설정
 * 우선 Spring Batch 를 사용하지 않고, 서비스가 커지면 Spring Batch 를 사용하는 것을 고려
 */
@Configuration
public class QuartzConfig {

    // TODO 결제날이 도래한 구독을 찾아서 결제를 진행하는 Job 을 생성
    // TODO 해당 로직은 결제날 이면서, SubscriptionStatus 가 ACTIVE 인 구독을 찾아서 결제를 진행


}
