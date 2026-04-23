package com.authcore.modulo.grupo.aplicacao.comando;

import java.time.Instant;
import java.util.UUID;

public record RespostaGrupoColaboracaoCriado(
        UUID idGrupo,
        String nomeGrupo,
        int quantidadeEmailsConvidados,
        Instant instanteCriacao) {}
