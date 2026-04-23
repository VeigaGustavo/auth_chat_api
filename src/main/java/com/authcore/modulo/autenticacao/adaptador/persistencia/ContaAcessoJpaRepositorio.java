package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaAcessoJpaRepositorio extends JpaRepository<ContaAcessoEntity, UUID> {

    Optional<ContaAcessoEntity> findByEmailCorporativo(String emailCorporativo);
}
