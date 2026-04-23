package com.authcore.modulo.autenticacao.dominio;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/**
 * Conta vinculada a uma pessoa no sistema, usada para login e exibição no chat.
 */
@Getter
@Builder
public class ContaAcesso {

    private final UUID id;
    private final String emailCorporativo;
    private final String hashSenha;
    /** Nome civil completo do titular (valor em claro só em memória durante o caso de uso). */
    private final String nomeCompletoTitular;
    private final boolean ativo;
    /** Papel da conta; define o fechamento das {@link PermissaoAcessoSistema} efetivas. */
    private final PapelAcessoSistema nivelPapelAcesso;

    public void garantirPodeAcessar() {
        if (!ativo) {
            throw new com.authcore.comum.excecao.RegraNegocioException("Conta inativa. Entre em contato com o suporte.");
        }
    }

    /** Rótulo curto p/ JWT, WebSocket e UI (derivado do nome completo). */
    public String nomeParaExibicaoPublica() {
        if (nomeCompletoTitular == null || nomeCompletoTitular.isBlank()) {
            return "—";
        }
        String t = nomeCompletoTitular.trim();
        return t.length() > 120 ? t.substring(0, 117) + "…" : t;
    }
}
