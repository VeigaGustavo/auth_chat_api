package com.authcore.modulo.conversa.adaptador.persistencia;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensagemSalaGeralJpaRepositorio extends JpaRepository<MensagemSalaGeralEntity, UUID> {

    List<MensagemSalaGeralEntity> findAllByOrderByInstanteEnvioDesc(Pageable pageable);
}
