package com.exemplo.paymentservice.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Objeto de Valor: representa uma quantia monetária.
 * Imutável, sem identidade própria, igualdade por valor.
 */
public final class Valor {

    private final BigDecimal quantia;

    public Valor(BigDecimal quantia) {
        if (quantia == null || quantia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de pagamento deve ser positivo");
        }
        this.quantia = quantia;
    }

    public BigDecimal getQuantia() {
        return quantia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Valor)) return false;
        Valor valor1 = (Valor) o;
        return quantia.compareTo(valor1.quantia) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantia.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return quantia.toPlainString();
    }
}
