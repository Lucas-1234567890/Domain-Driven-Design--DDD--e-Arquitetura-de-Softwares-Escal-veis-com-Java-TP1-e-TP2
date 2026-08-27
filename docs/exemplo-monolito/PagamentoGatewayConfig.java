// Ilustrativo — cola no monólito. Ver PagamentoGateway.java.
package com.exemplo.legado.pagamento;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Ponto único de decisão de roteamento. Trocar de "false" para "true"
 * no application.properties do monólito é o suficiente para migrar o
 * tráfego de pagamento para o novo serviço — sem redeploy de emergência
 * se algo der errado, basta voltar a flag.
 */
@Configuration
public class PagamentoGatewayConfig {

    @Bean
    public PagamentoGateway pagamentoGateway(
            @Value("${pagamento.usar-novo-servico:false}") boolean usarNovoServico,
            PagamentoProcessador processadorLegado,
            RestTemplate restTemplate,
            @Value("${pagamento.service-url:http://localhost:8081}") String url) {

        return usarNovoServico
                ? new PagamentoGatewayRemoto(restTemplate, url)
                : new PagamentoGatewayLegado(processadorLegado);
    }
}
