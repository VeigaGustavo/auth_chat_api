package com.authcore.modulo.autenticacao.aplicacao.comando;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosRedefinicaoSenha(
        @NotBlank String tokenRedefinicao, @NotBlank @Size(min = 6) String novaSenhaAcesso) {}
