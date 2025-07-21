package com.gustavooarantes.foodhub.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PedidoRequestDto(
        @NotEmpty(message = "A lista de itens não pode estar vazia")
        List<@Valid ItemPedidoRequestDto> itens
) {}