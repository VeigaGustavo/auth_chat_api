package com.authcore.modulo.grupo.adaptador.web;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.authcore.modulo.grupo.aplicacao.GrupoColaboracaoService;
import com.authcore.modulo.grupo.aplicacao.comando.DadosCriacaoGrupoColaboracao;
import com.authcore.modulo.grupo.aplicacao.comando.RespostaGrupoColaboracaoCriado;
import com.authcore.modulo.grupo.aplicacao.comando.RespostaGrupoColaboracaoDetalhe;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoColaboracaoController {

    private final GrupoColaboracaoService grupoColaboracaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaGrupoColaboracaoCriado criarGrupo(
            @Valid @RequestBody DadosCriacaoGrupoColaboracao corpo, Authentication autenticacao) {
        UUID idConta = UUID.fromString(autenticacao.getName());
        return grupoColaboracaoService.criarGrupo(corpo, idConta);
    }

    @GetMapping("/{idGrupo}")
    public RespostaGrupoColaboracaoDetalhe obterDetalhe(
            @PathVariable UUID idGrupo, Authentication autenticacao) {
        UUID idConta = UUID.fromString(autenticacao.getName());
        return grupoColaboracaoService.obterDetalheParaContaCriadora(idGrupo, idConta);
    }
}
