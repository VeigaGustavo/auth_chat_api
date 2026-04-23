package com.authcore.modulo.autenticacao.dominio;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta (interface de domínio) de persistência da conta.
 */
public interface ContaAcessoRepositorio {

    Optional<ContaAcesso> buscarPorEmailCorporativo(String emailCorporativo);

    Optional<ContaAcesso> buscarPorId(UUID id);

    ContaAcesso salvar(ContaAcesso contaAcesso);
}
