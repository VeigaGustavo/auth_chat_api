package com.authcore.modulo.compartilhado;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import com.authcore.modulo.compartilhado.seguranca.FiltroPortadorTokenJwtHttp;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSegurancaAplicacao {

    @Bean
    public SecurityFilterChain cadeiaFiltros(
            HttpSecurity http,
            CorsConfigurationSource origemCors,
            FiltroPortadorTokenJwtHttp filtroJwtHttp)
            throws Exception {
        return http.cors(c -> c.configurationSource(origemCors))
                .csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filtroJwtHttp, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        a -> a.requestMatchers(
                                        "/api/autenticacao/entrar",
                                        "/api/autenticacao/registrar",
                                        "/api/autenticacao/solicitar-redefinicao-senha",
                                        "/api/autenticacao/redefinir-senha",
                                        "/error",
                                        "/ws/**",
                                        "/ws**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .exceptionHandling(
                        e ->
                                e.authenticationEntryPoint(
                                        (request, response, ex) -> {
                                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                            response.setCharacterEncoding("UTF-8");
                                            response.getWriter().write(
                                                    "{\"mensagem\":\"Autenticação necessária. Envie o cabeçalho Authorization: Bearer <tokenAcesso> obtido em /api/autenticacao/entrar ou /registrar.\"}");
                                        }))
                .build();
    }

    @Bean
    public PasswordEncoder codificadorPadraoSenha() {
        return new BCryptPasswordEncoder();
    }
}
