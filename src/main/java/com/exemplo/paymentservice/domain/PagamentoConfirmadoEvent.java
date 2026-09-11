package com.exemplo.paymentservice.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Evento de domínio concreto: representa o fato "um pagamento foi
 * confirmado". É quem o monólito legado (ou qualquer outro consumidor)
 * escuta para atualizar o status do Pedido sem que o payment-service
 * precise conhecer o agregado Pedido inteiro.
 */
public record PagamentoConfirmadoEvent(
        PagamentoId pagamentoId,
        PedidoId pedidoId,
        BigDecimal valorConfirmado,
        Instant ocorridoEm
) implements DomainEvent {
}
