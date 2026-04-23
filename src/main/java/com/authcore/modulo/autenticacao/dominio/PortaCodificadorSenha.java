package com.authcore.modulo.autenticacao.dominio;

/**
 * Abstração de codificação/validação de senha (p.ex. BCrypt).
 */
public interface PortaCodificadorSenha {

    String codificar(String senhaPura);

    boolean corresponde(String senhaPura, String hashArmazenado);
}
