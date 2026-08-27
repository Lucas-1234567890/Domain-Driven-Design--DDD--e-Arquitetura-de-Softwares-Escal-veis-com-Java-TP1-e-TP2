package com.exemplo.paymentservice.infrastructure.web;

import com.exemplo.paymentservice.application.PagamentoApplicationService;
import com.exemplo.paymentservice.domain.Pagamento;
import com.exemplo.paymentservice.domain.PagamentoId;
import com.exemplo.paymentservice.infrastructure.web.dto.PagamentoRequestDTO;
import com.exemplo.paymentservice.infrastructure.web.dto.PagamentoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoApplicationService pagamentoApplicationService;

    public PagamentoController(PagamentoApplicationService pagamentoApplicationService) {
        this.pagamentoApplicationService = pagamentoApplicationService;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> processar(@RequestBody PagamentoRequestDTO request) {
        PagamentoId id = pagamentoApplicationService.processar(
                request.pedidoId(), request.valor(), request.numeroCartao());

        Pagamento pagamento = pagamentoApplicationService.buscar(id.getValor().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(pagamento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscar(@PathVariable String id) {
        Pagamento pagamento = pagamentoApplicationService.buscar(id);
        return ResponseEntity.ok(toResponse(pagamento));
    }

    private PagamentoResponseDTO toResponse(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId().getValor().toString(),
                pagamento.getPedidoId().getValor(),
                pagamento.getValor().getQuantia(),
                pagamento.getStatus().name(),
                pagamento.getNumeroCartao().getUltimosDigitos()
        );
    }
}
