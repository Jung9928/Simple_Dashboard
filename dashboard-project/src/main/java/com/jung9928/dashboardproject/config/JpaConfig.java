package com.jung9928.dashboardproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        // Auditing시, 지정한 파라미터 값이 들어가게 됨.
        // TODO : 스프링 시큐리티로 인증 기능 추가 시, 수정 필요
        return () -> Optional.of("jung");
    }
}
