package com.authcore.modulo.conversa.adaptador.web;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.authcore.modulo.conversa.aplicacao.ConversaSalaGeralHistoricoService;
import com.authcore.modulo.conversa.aplicacao.MensagemPublicaSalaModel;
import lombok.RequiredArgsConstructor;

/**
 * Histórico HTTP da sala geral (requer o mesmo JWT das outras APIs protegidas).
 */
@RestController
@RequestMapping("/api/conversa/sala-geral")
@RequiredArgsConstructor
public class HistoricoSalaGeralRestController {

    private final ConversaSalaGeralHistoricoService historicoSalaGeralService;

    @GetMapping("/mensagens")
    public List<MensagemPublicaSalaModel> listarUltimasMensagens(
            @RequestParam(name = "limite", required = false, defaultValue = "50") int limite,
            @SuppressWarnings("unused") Authentication autenticacao) {
        return historicoSalaGeralService.listarUltimasOrdenadoCronologicamente(limite);
    }
}
