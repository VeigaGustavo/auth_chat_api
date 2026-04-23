package com.authcore.modulo.grupo.adaptador.persistencia;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoColaboracaoJpaRepositorio extends JpaRepository<GrupoColaboracaoEntity, UUID> {}
