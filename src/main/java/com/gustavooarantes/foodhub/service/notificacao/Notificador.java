package com.gustavooarantes.foodhub.service.notificacao;

import com.gustavooarantes.foodhub.dto.NotificacaoDto;

public interface Notificador {
    void enviar(NotificacaoDto notificacao);
}