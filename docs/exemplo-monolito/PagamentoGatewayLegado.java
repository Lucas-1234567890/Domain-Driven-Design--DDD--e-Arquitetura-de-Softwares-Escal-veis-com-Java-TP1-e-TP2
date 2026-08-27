// Ilustrativo — cola no monólito. Ver PagamentoGateway.java.
package com.exemplo.legado.pagamento;

import java.math.BigDecimal;

/**
 * Implementação 1: ainda delega para o PagamentoProcessador concreto que
 * já existe no monólito hoje. Mesma regra de negócio de sempre, só que
 * agora por trás de uma porta — é isso que permite trocar depois sem
 * tocar no PedidoService de novo.
 */
public class PagamentoGatewayLegado implements PagamentoGateway {

    private final PagamentoProcessador processadorLegado;

    public PagamentoGatewayLegado(PagamentoProcessador processadorLegado) {
        this.processadorLegado = processadorLegado;
    }

    @Override
    public ResultadoPagamento processar(Long pedidoId, BigDecimal valor, String numeroCartao) {
        return processadorLegado.processar(pedidoId, valor, numeroCartao);
    }
}
