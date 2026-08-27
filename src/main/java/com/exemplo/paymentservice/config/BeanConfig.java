package com.exemplo.paymentservice.config;

import com.exemplo.paymentservice.application.PagamentoApplicationService;
import com.exemplo.paymentservice.domain.PagamentoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PagamentoApplicationService pagamentoApplicationService(PagamentoRepository pagamentoRepository) {
        return new PagamentoApplicationService(pagamentoRepository);
    }
}
