package com.gustavooarantes.foodhub.dto;

import java.io.Serializable;

public record NotificacaoDto(
        String destinatario,
        String assunto,
        String mensagem
) implements Serializable {
}