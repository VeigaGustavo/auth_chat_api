package com.authcore.modulo.conversa.aplicacao;

import java.time.Instant;
import java.util.UUID;

/**
 * Carga de saída normalizada do chat em sala (broadcast).
 */
public record MensagemPublicaSalaModel(
        UUID idContaRemetente, String nomeExibicaoRemetente, String conteudoMensagem, Instant instanteEnvio) {}
