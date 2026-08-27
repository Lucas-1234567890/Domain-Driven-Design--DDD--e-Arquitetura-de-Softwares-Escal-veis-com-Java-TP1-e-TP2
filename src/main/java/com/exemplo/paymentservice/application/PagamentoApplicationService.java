package com.exemplo.paymentservice.application;

import com.exemplo.paymentservice.domain.NumeroCartao;
import com.exemplo.paymentservice.domain.Pagamento;
import com.exemplo.paymentservice.domain.PagamentoId;
import com.exemplo.paymentservice.domain.PagamentoNaoEncontradoException;
import com.exemplo.paymentservice.domain.PagamentoRepository;
import com.exemplo.paymentservice.domain.PedidoId;
import com.exemplo.paymentservice.domain.Valor;

import java.math.BigDecimal;

/**
 * Orquestra o caso de uso "processar pagamento". Não contém regra de
 * negócio — a regra vive inteira dentro do Aggregate Root Pagamento.
 * Esta classe é framework-agnostic de propósito: não tem anotação do
 * Spring, para ficar fácil de testar isoladamente.
 */
public class PagamentoApplicationService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoApplicationService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public PagamentoId processar(Long pedidoIdValor, BigDecimal quantia, String numeroCartao) {
        PagamentoId id = PagamentoId.novo();

        Pagamento pagamento = Pagamento.criar(
                id,
                new PedidoId(pedidoIdValor),
                new Valor(quantia),
                new NumeroCartao(numeroCartao)
        );

        pagamento.confirmar();
        pagamentoRepository.salvar(pagamento);

        return id;
    }

    public Pagamento buscar(String id) {
        PagamentoId pagamentoId = PagamentoId.de(id);
        return pagamentoRepository.buscarPorId(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontradoException(id));
    }
}
