package com.exemplo.paymentservice.domain;

import java.util.Objects;

/**
 * Referência ao agregado Pedido, que pertence a outro Bounded Context
 * (o monólito legado). O payment-service NUNCA carrega a entidade Pedido
 * inteira — apenas o ID, evitando acoplamento entre contextos.
 */
public final class PedidoId {

    private final Long valor;

    public PedidoId(Long valor) {
        if (valor == null) {
            throw new IllegalArgumentException("PedidoId não pode ser nulo");
        }
        this.valor = valor;
    }

    public Long getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedidoId)) return false;
        PedidoId pedidoId = (PedidoId) o;
        return valor.equals(pedidoId.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
