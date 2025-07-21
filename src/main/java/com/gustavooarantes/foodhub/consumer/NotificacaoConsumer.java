package com.gustavooarantes.foodhub.consumer;

import com.gustavooarantes.foodhub.config.RabbitMQConfig;
import com.gustavooarantes.foodhub.dto.NotificacaoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACOES)
    public void receberNotificacao(NotificacaoDto notificacao) {
        logger.info("=============================================");
        logger.info("Nova notificação recebida para processamento!");
        logger.info("Enviando e-mail (SIMULADO)...");
        logger.info("---------------------------------------------");
        logger.info("Para: {}", notificacao.destinatario());
        logger.info("Assunto: {}", notificacao.assunto());
        logger.info("Mensagem: {}", notificacao.mensagem());
        logger.info("---------------------------------------------");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info("Simulação de envio de e-mail concluída.");
        logger.info("=============================================");
    }
}