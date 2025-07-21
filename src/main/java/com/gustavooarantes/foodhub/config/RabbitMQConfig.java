package com.gustavooarantes.foodhub.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PEDIDOS_PAGAMENTO = "pedidos.pagamento";
    public static final String QUEUE_NOTIFICACOES = "pedidos.notificacoes";

    @Bean
    public Queue pedidosPagamentoQueue() {
        return new Queue(QUEUE_PEDIDOS_PAGAMENTO, true);
    }

    @Bean // <-- NOVO BEAN
    public Queue notificacoesQueue() {
        return new Queue(QUEUE_NOTIFICACOES, true);
    }
}