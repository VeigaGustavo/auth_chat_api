package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitacao_redefinicao_senha")
@Getter
@Setter
@NoArgsConstructor
public class SolicitacaoRedefinicaoSenhaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID idContaProprietaria;

    /** Índice opaco do token (nunca o token em claro). */
    @Column(nullable = false, unique = true, length = 64)
    private String indiceBuscaTokenRedefinicao;

    @Column(nullable = false)
    private Instant expiraEm;

    @Column(nullable = false)
    private boolean consumido;
}
