package com.authcore.modulo.grupo.adaptador.persistencia;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConviteParticipacaoGrupoJpaRepositorio
        extends JpaRepository<ConviteParticipacaoGrupoEntity, UUID> {

    List<ConviteParticipacaoGrupoEntity> findByGrupoColaboracao_Id(UUID idGrupoColaboracao);
}
