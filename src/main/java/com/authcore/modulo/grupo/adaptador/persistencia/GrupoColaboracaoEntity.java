package com.authcore.modulo.grupo.adaptador.persistencia;

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
@Table(name = "grupo_colaboracao")
@Getter
@Setter
@NoArgsConstructor
public class GrupoColaboracaoEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String nomeTitulo;

    @Column(length = 2000)
    private String textoDescricaoOpcional;

    @Column(nullable = false, name = "id_conta_propietaria_criadora")
    private UUID idContaPropietariaCriadora;

    @Column(nullable = false, name = "instante_registro")
    private Instant instanteRegistro;
}
