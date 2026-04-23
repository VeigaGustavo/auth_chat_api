package com.authcore.modulo.grupo.dominio;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/**
 * Grupo de trabalho; cada convite referencia o e-mail com que a pessoa entra (hoje = login).
 * Futuro: encaminhar notificações / caixa de mensagens para esse e-mail.
 */
@Getter
@Builder
public class GrupoColaboracao {

    private final UUID id;
    private final String nomeTitulo;
    private final String textoDescricaoOpcional;
    private final UUID idContaPropietariaCriadora;
    private final Instant instanteRegistro;
    private final List<ConviteParticipacaoEmail> convitesParticipacaoEmail;
}
