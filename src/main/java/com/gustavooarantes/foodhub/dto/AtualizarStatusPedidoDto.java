package com.gustavooarantes.foodhub.dto;

import com.gustavooarantes.foodhub.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoDto(
        @NotNull(message = "O novo status não pode ser nulo")
        StatusPedido status
) {}