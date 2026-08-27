package com.exemplo.paymentservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPagamentoRepository extends JpaRepository<PagamentoJpaEntity, String> {
}
