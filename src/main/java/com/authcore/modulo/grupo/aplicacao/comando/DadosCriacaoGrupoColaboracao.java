package com.authcore.modulo.grupo.aplicacao.comando;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCriacaoGrupoColaboracao(
        @NotBlank @Size(max = 200) String nomeGrupo,
        @Size(max = 2000) String descricaoOpcional,
        @JsonProperty("emailsConvidados") List<@Email String> emailsConvidados) {

    public DadosCriacaoGrupoColaboracao {
        emailsConvidados = emailsConvidados == null ? List.of() : emailsConvidados;
    }
}
