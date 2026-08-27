package com.exemplo.paymentservice.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identidade do Aggregate Root Pagamento.
 * Gerada pelo próprio domínio (client-generated id), não pelo banco.
 */
public final class PagamentoId {

    private final UUID valor;

    private PagamentoId(UUID valor) {
        this.valor = valor;
    }

    public static PagamentoId novo() {
        return new PagamentoId(UUID.randomUUID());
    }

    public static PagamentoId de(String valor) {
        return new PagamentoId(UUID.fromString(valor));
    }

    public UUID getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagamentoId)) return false;
        PagamentoId that = (PagamentoId) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
