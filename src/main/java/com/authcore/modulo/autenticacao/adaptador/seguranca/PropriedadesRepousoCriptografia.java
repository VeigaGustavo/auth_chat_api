package com.authcore.modulo.autenticacao.adaptador.seguranca;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Segredo único p/ derivar índices determinísticos (HMAC) e chave AES de campos cifrados no repouso.
 */
@ConfigurationProperties(prefix = "acesso.repouso")
public record PropriedadesRepousoCriptografia(String segredoMestreArmazenamento) {

    public PropriedadesRepousoCriptografia {
        if (segredoMestreArmazenamento == null || segredoMestreArmazenamento.length() < 32) {
            throw new IllegalStateException(
                    "acesso.repouso.segredoMestreArmazenamento precisa de ao menos 32 caracteres.");
        }
    }
}
