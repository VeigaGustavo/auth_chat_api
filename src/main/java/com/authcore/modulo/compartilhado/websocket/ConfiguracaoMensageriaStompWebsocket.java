package com.authcore.modulo.compartilhado.websocket;

import java.util.Arrays;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.authcore.modulo.compartilhado.PropriedadesCorsAcesso;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ConfiguracaoMensageriaStompWebsocket implements WebSocketMessageBrokerConfigurer {

    private final InterceptadorHandshakeTokenJwt interceptadorHandshakeTokenJwt;
    private final PropriedadesCorsAcesso propriedadesCorsAcesso;

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registro) {
        registro.enableSimpleBroker("/topic", "/queue");
        registro.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registro) {
        var origens = Arrays.stream(propriedadesCorsAcesso.origens().split(","))
                .map(String::trim)
                .toList()
                .toArray(new String[0]);
        registro.addEndpoint("/ws")
                .addInterceptors(interceptadorHandshakeTokenJwt)
                .setAllowedOriginPatterns(origens)
                .withSockJS();
    }
}
