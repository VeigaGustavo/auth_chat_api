package com.authcore.modulo.conversa.adaptador.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Carga mínima enviada pelo cliente STOMP.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosMensagemChatModel(String conteudoMensagem) {}
