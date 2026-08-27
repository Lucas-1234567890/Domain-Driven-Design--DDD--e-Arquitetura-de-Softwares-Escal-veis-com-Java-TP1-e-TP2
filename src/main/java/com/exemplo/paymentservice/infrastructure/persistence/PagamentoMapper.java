package com.exemplo.paymentservice.infrastructure.persistence;

import com.exemplo.paymentservice.domain.NumeroCartao;
import com.exemplo.paymentservice.domain.Pagamento;
import com.exemplo.paymentservice.domain.PagamentoId;
import com.exemplo.paymentservice.domain.PedidoId;
import com.exemplo.paymentservice.domain.Valor;

public final class PagamentoMapper {

    private PagamentoMapper() {
    }

    public static PagamentoJpaEntity paraEntity(Pagamento pagamento) {
        return new PagamentoJpaEntity(
                pagamento.getId().getValor().toString(),
                pagamento.getPedidoId().getValor(),
                pagamento.getValor().getQuantia(),
                pagamento.getNumeroCartao().getUltimosDigitos(),
                pagamento.getStatus()
        );
    }

    public static Pagamento paraDominio(PagamentoJpaEntity entity) {
        return Pagamento.reidratar(
                PagamentoId.de(entity.getId()),
                new PedidoId(entity.getPedidoId()),
                new Valor(entity.getValor()),
                new NumeroCartao(entity.getNumeroCartao()),
                entity.getStatus()
        );
    }
}
