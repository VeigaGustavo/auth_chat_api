package com.authcore.modulo.compartilhado;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "acesso.cors")
public record PropriedadesCorsAcesso(String origens) {
    public PropriedadesCorsAcesso {
        if (origens == null || origens.isBlank()) {
            throw new IllegalStateException("acesso.cors.origens precisa ser definida.");
        }
    }
}
