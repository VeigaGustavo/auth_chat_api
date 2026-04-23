package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "solicitacao_redefinicao_senha",
        indexes = @Index(name = "idx_solicitacao_token", columnList = "tokenOpacoUsoUnico", unique = true))
@Getter
@Setter
@NoArgsConstructor
public class SolicitacaoRedefinicaoSenhaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID idContaProprietaria;

    @Column(nullable = false, unique = true, length = 100)
    private String tokenOpacoUsoUnico;

    @Column(nullable = false)
    private Instant expiraEm;

    @Column(nullable = false)
    private boolean consumido;
}
