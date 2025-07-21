package com.gustavooarantes.foodhub.consumer;

import com.gustavooarantes.foodhub.config.RabbitMQConfig;
import com.gustavooarantes.foodhub.dto.NotificacaoDto;
import com.gustavooarantes.foodhub.service.notificacao.Notificador;
import com.gustavooarantes.foodhub.service.notificacao.NotificadorFactory;
import com.gustavooarantes.foodhub.service.notificacao.TipoNotificacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);
    private final NotificadorFactory notificadorFactory;

    public NotificacaoConsumer(NotificadorFactory notificadorFactory) {
        this.notificadorFactory = notificadorFactory;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACOES)
    public void receberNotificacao(NotificacaoDto notificacao) {
        logger.info("Nova notificação recebida para o destinatário {}", notificacao.destinatario());

        Notificador notificador = notificadorFactory.getNotificador(TipoNotificacao.EMAIL);

        notificador.enviar(notificacao);
    }
}