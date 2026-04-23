package com.authcore.modulo.autenticacao.dominio;

import java.util.Optional;
import java.util.UUID;

/**
 * Emissão e validação de token JWT para acesso a APIs e conexão WebSocket.
 */
public interface PortaProvedorTokenAcesso {

    String emitirToken(ContaAcesso contaAcesso);

    boolean tokenEstaValido(String tokenBearerOuPuro);

    long validadeAproximadaEmSegundos();

    /**
     * Extrai o identificador interno (UUID) e o nome para exibição, se o token for válido.
     */
    Optional<ResumoAcessoContaToken> resumoSeValido(String token);

    /**
     * Registro mínimo necessário fora de HTTP (p.ex. atributos de sessão no handshake).
     */
    record ResumoAcessoContaToken(UUID idConta, String nomeExibicao) {}
}
