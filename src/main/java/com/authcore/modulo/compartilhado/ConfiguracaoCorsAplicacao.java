package com.authcore.modulo.compartilhado;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class ConfiguracaoCorsAplicacao {

    @Bean
    public CorsConfigurationSource origemCors(PropriedadesCorsAcesso propriedades) {
        var c = new CorsConfiguration();
        c.setAllowCredentials(true);
        c.setAllowedOriginPatterns(
                Arrays.stream(propriedades.origens().split(",")).map(String::trim).toList());
        c.addAllowedHeader(CorsConfiguration.ALL);
        c.addAllowedMethod(CorsConfiguration.ALL);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }
}
