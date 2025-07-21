package com.gustavooarantes.foodhub.service;

import com.gustavooarantes.foodhub.config.RabbitMQConfig;
import com.gustavooarantes.foodhub.domain.ItemPedido;
import com.gustavooarantes.foodhub.domain.Pedido;
import com.gustavooarantes.foodhub.domain.Produto;
import com.gustavooarantes.foodhub.domain.User;
import com.gustavooarantes.foodhub.domain.enums.StatusPedido;
import com.gustavooarantes.foodhub.dto.PedidoRequestDto;
import com.gustavooarantes.foodhub.dto.PedidoResponseDto;
import com.gustavooarantes.foodhub.repository.PedidoRepository;
import com.gustavooarantes.foodhub.repository.ProdutoRepository;
import com.gustavooarantes.foodhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public PedidoResponseDto criarPedido(PedidoRequestDto pedidoRequestDto, String username) {
        User cliente = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Cliente не encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.RECEBIDO);

        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for (var itemDto : pedidoRequestDto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + itemDto.produtoId() + " não encontrado."));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDto.quantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedido.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade())));

            pedido.adicionarItem(itemPedido);

            valorTotalPedido = valorTotalPedido.add(itemPedido.getSubtotal());
        }

        pedido.setValorTotal(valorTotalPedido);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PEDIDOS_PAGAMENTO, pedidoSalvo.getId());

        return new PedidoResponseDto(
                pedidoSalvo.getId(),
                pedidoSalvo.getCliente().getUsername(),
                pedidoSalvo.getStatus(),
                pedidoSalvo.getValorTotal(),
                pedidoSalvo.getDataCriacao()
        );
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarTodosOsPedidos() {
        return pedidoRepository.findAllByOrderByDataCriacaoDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarPedidosDoCliente(String username) {
        return pedidoRepository.findByClienteUsernameOrderByDataCriacaoDesc(username).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PedidoResponseDto mapToDto(Pedido pedido) {
        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getCliente().getUsername(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getDataCriacao()
        );
    }

    @Transactional
    public PedidoResponseDto atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + pedidoId));

        pedido.setStatus(novoStatus);
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        return mapToDto(pedidoAtualizado);
    }
}