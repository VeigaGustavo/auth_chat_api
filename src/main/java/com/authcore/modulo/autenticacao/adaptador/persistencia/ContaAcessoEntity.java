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

    /** Índice opaco p/ login por e-mail sem guardar o e-mail em claro. */
    @Column(name = "chave_indice_busca_email", nullable = false, unique = true, length = 64)
    private String chaveIndiceBuscaEmailCorporativo;

    @Column(name = "material_email_cifrado", nullable = false, length = 2048)
    private String materialEmailCorporativoCifrado;

    @Column(name = "material_nome_completo_cifrado", nullable = false, length = 4096)
    private String materialNomeCompletoCifrado;

    /** BCrypt da senha, ainda cifrado em repouso (camada extra). */
    @Column(name = "material_hash_senha_cifrado", nullable = false, length = 1024)
    private String materialHashSenhaBcryptCifrado;

    @Column(nullable = false)
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PapelAcessoSistema nivelPapelAcesso;
}
