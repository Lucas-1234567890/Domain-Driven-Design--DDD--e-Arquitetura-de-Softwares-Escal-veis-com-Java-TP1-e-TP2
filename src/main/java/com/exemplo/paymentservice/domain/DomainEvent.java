package com.exemplo.paymentservice.domain;

import java.time.Instant;

/**
 * Abstração de um evento de domínio: um fato imutável que já aconteceu
 * dentro de um agregado e que pode interessar a outras partes do sistema.
 */
public interface DomainEvent {

    Instant ocorridoEm();
}
