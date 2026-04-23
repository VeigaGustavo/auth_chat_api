package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolicitacaoRedefinicaoJpaRepositorio extends JpaRepository<SolicitacaoRedefinicaoSenhaEntity, UUID> {

    @Query(
            """
            SELECT s FROM SolicitacaoRedefinicaoSenhaEntity s
            WHERE s.tokenOpacoUsoUnico = :token
            AND s.consumido = false
            AND s.expiraEm > :agora
            """)
    Optional<SolicitacaoRedefinicaoSenhaEntity> findAtivoPorToken(
            @Param("token") String token, @Param("agora") Instant agora);

    @Modifying
    @Query("UPDATE SolicitacaoRedefinicaoSenhaEntity s SET s.consumido = true WHERE s.id = :id")
    int marcarConsumido(@Param("id") UUID id);
}
