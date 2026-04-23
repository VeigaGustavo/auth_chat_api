package com.authcore.modulo.compartilhado.websocket;

import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;
import com.authcore.modulo.autenticacao.dominio.PortaProvedorTokenAcesso;
import lombok.RequiredArgsConstructor;

/**
 * Garante que apenas clientes com JWT válido concluem o handshake; copia o contexto p/ a sessão.
 */
@Component
@RequiredArgsConstructor
public class InterceptadorHandshakeTokenJwt implements HandshakeInterceptor {

    private final PortaProvedorTokenAcesso provedorToken;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> atributos) {
        var params = UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams();
        String bruto = params.getFirst("tokenAcesso");
        if (!StringUtils.hasText(bruto)) {
            bruto = params.getFirst("token");
        }
        if (!StringUtils.hasText(bruto)) {
            return false;
        }
        return provedorToken
                .resumoSeValido(bruto)
                .map(resumo -> {
                    atributos.put(
                            AtributoSessaoWebsocket.MAPA_IDENTIFICADOR_CONTA, resumo.idConta().toString());
                    atributos.put(AtributoSessaoWebsocket.MAPA_NOME_EXIBICAO, resumo.nomeExibicao());
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // nada
    }
}
