package com.authcore.modulo.grupo.dominio;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/** Convite de participação num grupo; o e-mail é o mesmo usado no login (por agora). */
@Getter
@Builder
public class ConviteParticipacaoEmail {

    private final UUID id;
    private final String emailCorporativoParaAcesso;
}
