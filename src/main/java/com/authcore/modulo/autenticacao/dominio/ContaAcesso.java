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
    private final String nomeApresentacao;
    private final boolean ativo;
    /** Papel da conta; define o fechamento das {@link PermissaoAcessoSistema} efetivas. */
    private final PapelAcessoSistema nivelPapelAcesso;

    public void garantirPodeAcessar() {
        if (!ativo) {
            throw new com.authcore.comum.excecao.RegraNegocioException("Conta inativa. Entre em contato com o suporte.");
        }
    }
}
