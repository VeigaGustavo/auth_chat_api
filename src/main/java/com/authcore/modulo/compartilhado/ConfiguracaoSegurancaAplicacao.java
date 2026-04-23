package com.authcore.modulo.compartilhado;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSegurancaAplicacao {

    @Bean
    public SecurityFilterChain cadeiaFiltros(
            HttpSecurity http, CorsConfigurationSource origemCors) throws Exception {
        return http.cors(c -> c.configurationSource(origemCors))
                .csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        a -> a.requestMatchers(
                                        "/api/autenticacao/entrar",
                                        "/api/autenticacao/solicitar-redefinicao-senha",
                                        "/api/autenticacao/redefinir-senha",
                                        "/error",
                                        "/ws/**",
                                        "/ws**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                // Sem filtro JWT em REST ainda: rotas além de auth (fora) devolvem 401 até integrar
                .build();
    }

    @Bean
    public PasswordEncoder codificadorPadraoSenha() {
        return new BCryptPasswordEncoder();
    }
}
