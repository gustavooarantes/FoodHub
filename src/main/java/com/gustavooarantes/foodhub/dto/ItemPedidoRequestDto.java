package com.gustavooarantes.foodhub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDto(
        @NotNull Long produtoId,
        @NotNull @Positive Integer quantidade
) {}