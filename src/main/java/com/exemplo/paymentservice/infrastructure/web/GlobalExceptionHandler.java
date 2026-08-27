package com.exemplo.paymentservice.infrastructure.web;

import com.exemplo.paymentservice.domain.PagamentoNaoEncontradoException;
import com.exemplo.paymentservice.domain.PagamentoRecusadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PagamentoRecusadoException.class)
    public ResponseEntity<Map<String, String>> handleRecusado(PagamentoRecusadoException ex) {
        return ResponseEntity.unprocessableEntity().body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(PagamentoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNaoEncontrado(PagamentoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }
}
