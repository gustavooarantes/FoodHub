package com.gustavooarantes.foodhub.consumer;

import com.gustavooarantes.foodhub.config.RabbitMQConfig;
import com.gustavooarantes.foodhub.domain.Pedido;
import com.gustavooarantes.foodhub.domain.enums.StatusPedido;
import com.gustavooarantes.foodhub.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PedidoPagamentoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoPagamentoConsumer.class);

    private final PedidoRepository pedidoRepository;

    public PedidoPagamentoConsumer(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDOS_PAGAMENTO)
    @Transactional
    public void processarPagamento(Long pedidoId) {
        logger.info("Recebido pedido para processamento de pagamento: ID {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

        if (pedido == null) {
            logger.error("Erro: Pedido com ID {} não encontrado.", pedidoId);
            return;
        }

        if (pedido.getStatus() != StatusPedido.RECEBIDO) {
            logger.warn("Pedido {} já foi processado ou está em um estado inesperado. Status atual: {}", pedidoId, pedido.getStatus());
            return;
        }

        try {
            logger.info("Simulando chamada a API de pagamento para o pedido {}...", pedidoId);
            Thread.sleep(2000);
            logger.info("Pagamento para o pedido {} aprovado com sucesso!", pedidoId);
        } catch (InterruptedException e) {
            logger.error("Thread interrompida durante a simulação de pagamento.", e);
            Thread.currentThread().interrupt();
        }
        pedido.setStatus(StatusPedido.PAGAMENTO_APROVADO);
        pedidoRepository.save(pedido);

        logger.info("Status do pedido {} atualizado para: {}", pedidoId, pedido.getStatus());

    }
}