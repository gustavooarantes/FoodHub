package com.gustavooarantes.foodhub.controller;

import com.gustavooarantes.foodhub.dto.PedidoRequestDto;
import com.gustavooarantes.foodhub.dto.PedidoResponseDto;
import com.gustavooarantes.foodhub.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PedidoResponseDto> criarPedido(
            @Valid @RequestBody PedidoRequestDto pedidoRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        PedidoResponseDto pedidoCriado = pedidoService.criarPedido(pedidoRequestDto, userDetails.getUsername());
        return new ResponseEntity<>(pedidoCriado, HttpStatus.CREATED);
    }
}