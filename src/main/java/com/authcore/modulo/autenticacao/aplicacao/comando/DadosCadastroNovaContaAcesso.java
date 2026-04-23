package com.authcore.modulo.autenticacao.aplicacao.comando;

import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cadastro público de conta. Papel elevado não é aceito por este fluxo (apenas convidado).
 */
public record DadosCadastroNovaContaAcesso(
        @NotBlank @Email String emailCorporativo,
        @NotBlank @Size(min = 6) String senhaAcesso,
        @NotBlank @Size(min = 3, max = 200) String nomeCompletoTitular,
        @JsonProperty("nivelPapelAcesso") PapelAcessoSistema nivelPapelAcessoOpcional) {}
