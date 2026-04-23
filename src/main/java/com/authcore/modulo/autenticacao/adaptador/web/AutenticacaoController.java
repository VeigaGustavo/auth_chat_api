package com.authcore.modulo.autenticacao.adaptador.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.authcore.modulo.autenticacao.aplicacao.AutenticacaoService;
import com.authcore.modulo.autenticacao.aplicacao.comando.CredenciaisEntrada;
import com.authcore.modulo.autenticacao.aplicacao.comando.DadosRedefinicaoSenha;
import com.authcore.modulo.autenticacao.aplicacao.comando.RespostaSessaoAcesso;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/entrar")
    public RespostaSessaoAcesso entrar(@Valid @RequestBody CredenciaisEntrada corpo) {
        return autenticacaoService.entrarComCredenciais(corpo);
    }

    @PostMapping("/solicitar-redefinicao-senha")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void solicitarRedefinicao(@RequestParam("emailCorporativo") String emailCorporativo) {
        autenticacaoService.solicitarRedefinicaoSenhaPeloEmail(emailCorporativo);
    }

    @PostMapping("/redefinir-senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void redefinir(@Valid @RequestBody DadosRedefinicaoSenha corpo) {
        autenticacaoService.redefinirSenhaComTokenOpaco(corpo);
    }
}
