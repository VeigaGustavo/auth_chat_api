package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conta_acesso")
@Getter
@Setter
@NoArgsConstructor
public class ContaAcessoEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String emailCorporativo;

    @Column(nullable = false, length = 200)
    private String hashSenha;

    @Column(nullable = false, length = 120)
    private String nomeApresentacao;

    @Column(nullable = false)
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PapelAcessoSistema nivelPapelAcesso;
}
