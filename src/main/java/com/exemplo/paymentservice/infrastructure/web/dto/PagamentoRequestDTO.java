package com.exemplo.paymentservice.infrastructure.web.dto;

import java.math.BigDecimal;

public record PagamentoRequestDTO(
        Long pedidoId,
        BigDecimal valor,
        String numeroCartao
) {
}
