package com.exemplo.paymentservice.domain;

import java.util.Optional;

/**
 * Porta (Ports and Adapters): o domínio define o contrato de persistência
 * que precisa, sem saber se por trás tem JPA, MongoDB ou memória.
 * O adaptador concreto fica em infrastructure.persistence.
 */
public interface PagamentoRepository {

    void salvar(Pagamento pagamento);

    Optional<Pagamento> buscarPorId(PagamentoId id);
}
