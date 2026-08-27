package com.exemplo.paymentservice.infrastructure.web.dto;

import java.math.BigDecimal;

public record PagamentoResponseDTO(
        String id,
        Long pedidoId,
        BigDecimal valor,
        String status,
        String ultimosDigitosCartao
) {
}
