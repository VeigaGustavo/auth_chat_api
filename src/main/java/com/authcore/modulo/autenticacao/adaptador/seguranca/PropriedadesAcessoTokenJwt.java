package com.authcore.modulo.autenticacao.adaptador.seguranca;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "acesso.jwt")
public record PropriedadesAcessoTokenJwt(
        String chaveAssinatura, long duracaoValidadeMilis) {

    public PropriedadesAcessoTokenJwt {
        if (chaveAssinatura == null || chaveAssinatura.length() < 32) {
            throw new IllegalStateException("acesso.jwt.chaveAssinatura precisa de ao menos 32 caracteres (HS256).");
        }
    }
}
