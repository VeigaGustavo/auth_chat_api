package com.authcore.modulo.compartilhado.websocket;

/**
 * Chaves alinhadas ao handshake; mantidas estáveis p/ o cliente/observabilidade.
 */
public final class AtributoSessaoWebsocket {

    public static final String MAPA_IDENTIFICADOR_CONTA = "identificadorContaInterna";
    public static final String MAPA_NOME_EXIBICAO = "nomeExibicaoPublico";

    private AtributoSessaoWebsocket() {}
}
