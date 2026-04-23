package com.authcore.modulo.autenticacao.dominio;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta de persistência para o fluxo de redefinição de senha.
 */
public interface SolicitacaoRedefinicaoSenhaRepositorio {

    SolicitacaoRedefinicaoSenhaPendente salvar(SolicitacaoRedefinicaoSenhaPendente entidade);

    Optional<SolicitacaoRedefinicaoSenhaPendente> buscarPorTokenAtivoNaoVencido(String token);

    void marcarComoConsumidoPorId(UUID id);
}
