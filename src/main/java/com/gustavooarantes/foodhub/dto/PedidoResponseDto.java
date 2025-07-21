package com.gustavooarantes.foodhub.dto;

import com.gustavooarantes.foodhub.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponseDto(
        Long id,
        String clienteUsername,
        StatusPedido status,
        BigDecimal valorTotal,
        LocalDateTime dataCriacao
) {}