package com.gustavooarantes.foodhub.service.notificacao;

import org.springframework.stereotype.Component;

@Component
public class NotificadorFactory {

    private final NotificadorEmail notificadorEmail;
    public NotificadorFactory(NotificadorEmail notificadorEmail) {
        this.notificadorEmail = notificadorEmail;
    }

    public Notificador getNotificador(TipoNotificacao tipoNotificacao) {
        if (tipoNotificacao == TipoNotificacao.EMAIL) {
            return this.notificadorEmail;
        }
        throw new IllegalArgumentException("Tipo de notificação inválido ou não suportado.");
    }
}