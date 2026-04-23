package com.authcore.modulo.grupo.dominio;

import java.util.Optional;
import java.util.UUID;

public interface GrupoColaboracaoRepositorio {

    GrupoColaboracao salvar(GrupoColaboracao grupoColaboracao);

    Optional<GrupoColaboracao> buscarPorIdentificador(UUID idGrupo);
}
