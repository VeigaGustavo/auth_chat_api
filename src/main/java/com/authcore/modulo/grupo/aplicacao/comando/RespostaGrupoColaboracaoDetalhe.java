package com.authcore.modulo.grupo.aplicacao.comando;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RespostaGrupoColaboracaoDetalhe(
        UUID idGrupo,
        String nomeGrupo,
        String descricaoOpcional,
        UUID idContaCriadora,
        Instant instanteCriacao,
        List<String> emailsConvidadosParaAcessoFuturo) {}
