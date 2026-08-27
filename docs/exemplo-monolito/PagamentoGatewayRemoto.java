// Ilustrativo — cola no monólito. Ver PagamentoGateway.java.
package com.exemplo.legado.pagamento;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Implementação 2: chama o payment-service novo via HTTP.
 * Faz o papel de Anti-Corruption Layer — traduz o modelo do
 * payment-service (PagamentoResponseDTO) para o modelo interno
 * do monólito (ResultadoPagamento), então nada do domínio novo
 * "vaza" para dentro do código legado.
 */
public class PagamentoGatewayRemoto implements PagamentoGateway {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl; // ex.: http://localhost:8081

    public PagamentoGatewayRemoto(RestTemplate restTemplate, String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    @Override
    public ResultadoPagamento processar(Long pedidoId, BigDecimal valor, String numeroCartao) {
        PagamentoRequestDTO request = new PagamentoRequestDTO(pedidoId, valor, numeroCartao);

        PagamentoResponseDTO response = restTemplate.postForObject(
                paymentServiceUrl + "/pagamentos", request, PagamentoResponseDTO.class);

        return new ResultadoPagamento(response.status(), response.id());
    }
}

/**
 * DTOs espelhando o contrato exposto pelo payment-service — mantidos
 * aqui no lado do monólito para não criar dependência de compilação
 * entre os dois projetos (cada um com seu próprio ciclo de deploy).
 */
record PagamentoRequestDTO(Long pedidoId, BigDecimal valor, String numeroCartao) {
}

record PagamentoResponseDTO(String id, Long pedidoId, BigDecimal valor, String status, String ultimosDigitosCartao) {
}
