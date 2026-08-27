package com.exemplo.paymentservice.infrastructure.persistence;

import com.exemplo.paymentservice.domain.StatusPagamento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Modelo de persistência puro (sem comportamento de domínio).
 * A tradução entre isto e o Aggregate Root Pagamento fica em PagamentoMapper —
 * o domínio nunca importa nada de jakarta.persistence.
 */
@Entity
@Table(name = "pagamentos")
public class PagamentoJpaEntity {

    @Id
    private String id;

    private Long pedidoId;

    private BigDecimal valor;

    private String numeroCartao;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    protected PagamentoJpaEntity() {
        // exigido pelo JPA
    }

    public PagamentoJpaEntity(String id, Long pedidoId, BigDecimal valor, String numeroCartao, StatusPagamento status) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.numeroCartao = numeroCartao;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public StatusPagamento getStatus() {
        return status;
    }
}
