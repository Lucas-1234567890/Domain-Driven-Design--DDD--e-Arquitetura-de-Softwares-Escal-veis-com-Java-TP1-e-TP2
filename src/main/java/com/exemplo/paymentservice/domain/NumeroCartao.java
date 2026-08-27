package com.exemplo.paymentservice.domain;

import java.util.Objects;

/**
 * Objeto de Valor: encapsula o número do cartão e a regra de bloqueio
 * que, no monólito legado, ficava solta dentro do service.
 *
 * Regra de negócio (igual ao simulador do monólito legado):
 * - Cartão terminado em "0000": bloqueado.
 * - Demais cartões: liberados (a aprovação final ainda depende do limite de valor).
 */
public final class NumeroCartao {

    private final String numero;

    public NumeroCartao(String numero) {
        if (numero == null || numero.length() < 4) {
            throw new IllegalArgumentException("Número de cartão inválido");
        }
        this.numero = numero;
    }

    public boolean estaBloqueado() {
        return numero.endsWith("0000");
    }

    public String getUltimosDigitos() {
        return numero.substring(numero.length() - 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumeroCartao)) return false;
        NumeroCartao that = (NumeroCartao) o;
        return numero.equals(that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        // nunca expor o número completo em logs
        return "**** **** **** " + getUltimosDigitos();
    }
}
