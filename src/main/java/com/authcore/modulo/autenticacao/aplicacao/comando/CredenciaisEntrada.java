package com.authcore.modulo.autenticacao.aplicacao.comando;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CredenciaisEntrada(
        @NotBlank @Email String emailCorporativo,
        @NotBlank String senhaAcesso) {}
