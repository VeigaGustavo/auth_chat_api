package com.authcore.modulo.conversa.adaptador.web;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import com.authcore.comum.excecao.RegraNegocioException;
import com.authcore.modulo.compartilhado.websocket.AtributoSessaoWebsocket;
import com.authcore.modulo.conversa.aplicacao.MensagemPublicaSalaModel;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
public class ConversacaoStompController {

    @MessageMapping("/conversa.enviar")
    @SendTo("/topic/mensagens-geral")
    public MensagemPublicaSalaModel enviarMensagemSalaGeral(
            @Payload DadosMensagemChatModel dados, SimpMessageHeaderAccessor cabecalho) {
        Map<String, Object> s = cabecalho.getSessionAttributes();
        if (s == null) {
            throw new RegraNegocioException("Sessão WebSocket inválida.");
        }
        var idStr = (String) s.get(AtributoSessaoWebsocket.MAPA_IDENTIFICADOR_CONTA);
        var nome = (String) s.get(AtributoSessaoWebsocket.MAPA_NOME_EXIBICAO);
        if (idStr == null || nome == null) {
            throw new RegraNegocioException("Acesso anônimo negado. Conecte com tokenAcesso na URL.");
        }
        if (dados.conteudoMensagem() == null || dados.conteudoMensagem().isBlank()) {
            throw new RegraNegocioException("Mensagem vazia.");
        }
        return new MensagemPublicaSalaModel(
                UUID.fromString(idStr), nome, dados.conteudoMensagem().trim(), Instant.now());
    }
}
