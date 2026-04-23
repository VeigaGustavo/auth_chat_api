package com.authcore.modulo.compartilhado.seguranca;

import java.io.IOException;
import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.authcore.modulo.autenticacao.dominio.PortaProvedorTokenAcesso;

/**
 * Propaga o contexto de segurança a partir do JWT Bearer nas APIs REST.
 */
@Component
public class FiltroPortadorTokenJwtHttp extends OncePerRequestFilter {

    private final PortaProvedorTokenAcesso provedorToken;

    public FiltroPortadorTokenJwtHttp(PortaProvedorTokenAcesso provedorToken) {
        this.provedorToken = provedorToken;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String cabecalho = request.getHeader("Authorization");
        if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
            String material = cabecalho.substring(7);
            provedorToken
                    .resumoSeValido(material)
                    .ifPresent(
                            resumo -> {
                                var auth =
                                        new UsernamePasswordAuthenticationToken(
                                                resumo.idConta().toString(),
                                                material,
                                                Collections.singletonList(
                                                        new SimpleGrantedAuthority("ROLE_CONTA_ATIVA")));
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            });
        }
        filterChain.doFilter(request, response);
    }
}
