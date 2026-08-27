package com.exemplo.paymentservice.domain;

public class PagamentoNaoEncontradoException extends RuntimeException {
    public PagamentoNaoEncontradoException(String id) {
        super("Pagamento não encontrado: " + id);
    }
}
