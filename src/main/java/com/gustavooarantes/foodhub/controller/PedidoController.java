package com.gustavooarantes.foodhub.controller;

import com.gustavooarantes.foodhub.dto.AtualizarStatusPedidoDto;
import com.gustavooarantes.foodhub.dto.PedidoRequestDto;
import com.gustavooarantes.foodhub.dto.PedidoResponseDto;
import com.gustavooarantes.foodhub.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CHEF')")
    public ResponseEntity<List<PedidoResponseDto>> listarTodos() {
        List<PedidoResponseDto> pedidos = pedidoService.listarTodosOsPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/meus-pedidos")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<PedidoResponseDto>> listarMeusPedidos(@AuthenticationPrincipal UserDetails userDetails) {
        List<PedidoResponseDto> pedidos = pedidoService.listarPedidosDoCliente(userDetails.getUsername());
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PedidoResponseDto> criarPedido(
            @Valid @RequestBody PedidoRequestDto pedidoRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        PedidoResponseDto pedidoCriado = pedidoService.criarPedido(pedidoRequestDto, userDetails.getUsername());
        return new ResponseEntity<>(pedidoCriado, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHEF')")
    public ResponseEntity<PedidoResponseDto> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusPedidoDto dto) {

        PedidoResponseDto pedidoAtualizado = pedidoService.atualizarStatus(id, dto.status());
        return ResponseEntity.ok(pedidoAtualizado);
    }
}