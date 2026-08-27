package com.exemplo.paymentservice;

import com.exemplo.paymentservice.domain.NumeroCartao;
import com.exemplo.paymentservice.domain.Pagamento;
import com.exemplo.paymentservice.domain.PagamentoId;
import com.exemplo.paymentservice.domain.PagamentoRecusadoException;
import com.exemplo.paymentservice.domain.PedidoId;
import com.exemplo.paymentservice.domain.StatusPagamento;
import com.exemplo.paymentservice.domain.Valor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes do agregado puro, sem Spring — prova que a regra de negócio
 * não depende de banco nem de HTTP.
 */
class PagamentoTest {

    @Test
    void deveConfirmarPagamentoValido() {
        Pagamento pagamento = Pagamento.criar(
                PagamentoId.novo(),
                new PedidoId(1L),
                new Valor(new BigDecimal("250.00")),
                new NumeroCartao("4111111111111111")
        );

        pagamento.confirmar();

        assertEquals(StatusPagamento.CONFIRMADO, pagamento.getStatus());
    }

    @Test
    void deveRecusarValorAcimaDoLimite() {
        assertThrows(PagamentoRecusadoException.class, () ->
                Pagamento.criar(
                        PagamentoId.novo(),
                        new PedidoId(1L),
                        new Valor(new BigDecimal("15000.00")),
                        new NumeroCartao("4111111111111111")
                ));
    }

    @Test
    void deveRecusarCartaoBloqueado() {
        assertThrows(PagamentoRecusadoException.class, () ->
                Pagamento.criar(
                        PagamentoId.novo(),
                        new PedidoId(1L),
                        new Valor(new BigDecimal("100.00")),
                        new NumeroCartao("4111111111110000")
                ));
    }

    @Test
    void naoDeveConfirmarPagamentoJaConfirmado() {
        Pagamento pagamento = Pagamento.criar(
                PagamentoId.novo(),
                new PedidoId(1L),
                new Valor(new BigDecimal("100.00")),
                new NumeroCartao("4111111111111111")
        );
        pagamento.confirmar();

        assertThrows(IllegalStateException.class, pagamento::confirmar);
    }
}
