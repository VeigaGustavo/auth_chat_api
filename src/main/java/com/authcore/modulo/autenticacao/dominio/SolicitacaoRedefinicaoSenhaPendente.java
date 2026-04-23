package com.authcore.modulo.autenticacao.dominio;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolicitacaoRedefinicaoSenhaPendente {

    private final UUID id;
    private final UUID idContaProprietaria;
    private final String tokenOpacoUsoUnico;
    private final Instant expiraEm;
    private final boolean consumido;
}
