package com.authcore.modulo.grupo.adaptador.persistencia;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "convite_participacao_grupo",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "uk_grupo_indice_email_participante",
                        columnNames = {"id_grupo_colaboracao", "chave_indice_busca_email_participante"}))
@Getter
@Setter
@NoArgsConstructor
public class ConviteParticipacaoGrupoEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_grupo_colaboracao", nullable = false)
    private GrupoColaboracaoEntity grupoColaboracao;

    @Column(name = "chave_indice_busca_email_participante", nullable = false, length = 64)
    private String chaveIndiceBuscaEmailParticipante;

    @Column(name = "material_email_participante_cifrado", nullable = false, length = 2048)
    private String materialEmailParticipanteCifrado;

    @Column(nullable = false, name = "instante_registro_convite")
    private Instant instanteRegistroConvite;
}
