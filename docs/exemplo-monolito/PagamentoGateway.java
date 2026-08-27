// ATENÇÃO: este arquivo é ilustrativo. Ele mostra como ficaria a mudança
// dentro do monólito ecommerce-legado-ddd (Branch by Abstraction), e não
// faz parte do build do payment-service. Cole-o no projeto do monólito,
// no pacote onde hoje vive o PedidoService.

package com.exemplo.legado.pagamento;

import java.math.BigDecimal;

/**
 * Porta: o PedidoService do monólito passa a conhecer só esta interface,
 * nunca mais o PagamentoProcessador concreto diretamente.
 */
public interface PagamentoGateway {
    ResultadoPagamento processar(Long pedidoId, BigDecimal valor, String numeroCartao);
}
