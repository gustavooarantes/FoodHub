package com.gustavooarantes.foodhub.service.notificacao;

import com.gustavooarantes.foodhub.dto.NotificacaoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificadorEmail implements Notificador {

    private static final Logger logger = LoggerFactory.getLogger(NotificadorEmail.class);

    @Override
    public void enviar(NotificacaoDto notificacao) {
        logger.info("=============================================");
        logger.info("Enviando E-MAIL (REAL)...");
        logger.info("---------------------------------------------");
        logger.info("Para: {}", notificacao.destinatario());
        logger.info("Assunto: {}", notificacao.assunto());
        logger.info("Mensagem: {}", notificacao.mensagem());
        logger.info("---------------------------------------------");
        logger.info("E-mail enviado com sucesso!");
        logger.info("=============================================");
    }
}