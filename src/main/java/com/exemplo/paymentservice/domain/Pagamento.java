package com.exemplo.paymentservice.domain;

import java.math.BigDecimal;

/**
 * Aggregate Root do contexto de Pagamento.
 *
 * Concentra as invariantes de negócio que, no monólito legado, estavam
 * "distribuídas em services" (ver README do projeto-base ecommerce-legado-ddd):
 *   - valor acima de R$ 10.000,00 é recusado por limite;
 *   - cartão terminado em "0000" é bloqueado.
 *
 * Nenhum código externo cria um Pagamento em estado inválido: toda a
 * validação acontece dentro do próprio construtor/fábrica.
 */
public class Pagamento {

    private static final BigDecimal LIMITE_MAXIMO = new BigDecimal("10000.00");

    private final PagamentoId id;
    private final PedidoId pedidoId;
    private final Valor valor;
    private final NumeroCartao numeroCartao;
    private StatusPagamento status;

    private Pagamento(PagamentoId id, PedidoId pedidoId, Valor valor,
                       NumeroCartao numeroCartao, StatusPagamento status) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.numeroCartao = numeroCartao;
        this.status = status;
    }

    /**
     * Fábrica usada para CRIAR um novo pagamento. Valida as invariantes
     * de negócio e recusa a operação antes de qualquer persistência.
     */
    public static Pagamento criar(PagamentoId id, PedidoId pedidoId, Valor valor, NumeroCartao numeroCartao) {
        if (valor.getQuantia().compareTo(LIMITE_MAXIMO) > 0) {
            throw new PagamentoRecusadoException("Valor acima do limite permitido (R$ 10.000,00)");
        }
        if (numeroCartao.estaBloqueado()) {
            throw new PagamentoRecusadoException("Cartão bloqueado");
        }
        return new Pagamento(id, pedidoId, valor, numeroCartao, StatusPagamento.PENDENTE);
    }

    /**
     * Fábrica usada apenas pela camada de persistência para RECONSTRUIR
     * um agregado já existente e válido a partir do banco. Não reaplica
     * as regras de criação.
     */
    public static Pagamento reidratar(PagamentoId id, PedidoId pedidoId, Valor valor,
                                       NumeroCartao numeroCartao, StatusPagamento status) {
        return new Pagamento(id, pedidoId, valor, numeroCartao, status);
    }

    public void confirmar() {
        if (this.status == StatusPagamento.CONFIRMADO) {
            throw new IllegalStateException("Pagamento já confirmado");
        }
        this.status = StatusPagamento.CONFIRMADO;
        // Aqui entraria o registro de um evento de domínio: PagamentoConfirmadoEvent,
        // publicado para o monólito (ex.: via mensageria) atualizar o status do Pedido.
    }

    public void estornar() {
        if (this.status != StatusPagamento.CONFIRMADO) {
            throw new IllegalStateException("Só é possível estornar pagamento confirmado");
        }
        this.status = StatusPagamento.ESTORNADO;
    }

    public PagamentoId getId() {
        return id;
    }

    public PedidoId getPedidoId() {
        return pedidoId;
    }

    public Valor getValor() {
        return valor;
    }

    public NumeroCartao getNumeroCartao() {
        return numeroCartao;
    }

    public StatusPagamento getStatus() {
        return status;
    }
}
