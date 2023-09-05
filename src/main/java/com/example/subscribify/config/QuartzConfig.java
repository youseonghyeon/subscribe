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


}
