package com.exemplo.paymentservice.domain;

/**
 * Lançada quando uma invariante de negócio do agregado Pagamento é violada
 * (limite de valor excedido, cartão bloqueado, etc.).
 */
public class PagamentoRecusadoException extends RuntimeException {
    public PagamentoRecusadoException(String mensagem) {
        super(mensagem);
    }
}
