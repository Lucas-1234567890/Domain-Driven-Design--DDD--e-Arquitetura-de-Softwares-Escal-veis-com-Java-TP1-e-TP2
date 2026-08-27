package com.exemplo.paymentservice.infrastructure.persistence;

import com.exemplo.paymentservice.domain.Pagamento;
import com.exemplo.paymentservice.domain.PagamentoId;
import com.exemplo.paymentservice.domain.PagamentoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adaptador (Ports and Adapters): implementa a porta PagamentoRepository
 * do domínio usando Spring Data JPA. É a única classe que "sabe" que
 * existe um banco relacional por trás.
 */
@Repository
public class PagamentoRepositoryAdapter implements PagamentoRepository {

    private final SpringDataPagamentoRepository springDataRepository;

    public PagamentoRepositoryAdapter(SpringDataPagamentoRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public void salvar(Pagamento pagamento) {
        springDataRepository.save(PagamentoMapper.paraEntity(pagamento));
    }

    @Override
    public Optional<Pagamento> buscarPorId(PagamentoId id) {
        return springDataRepository.findById(id.getValor().toString())
                .map(PagamentoMapper::paraDominio);
    }
}
