package com.authcore.modulo.conversa.adaptador.persistencia;

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
@Table(name = "mensagem_sala_geral")
@Getter
@Setter
@NoArgsConstructor
public class MensagemSalaGeralEntity {

    @Id
    private UUID id;

    @Column(nullable = false, name = "id_conta_remetente")
    private UUID idContaRemetente;

    @Column(nullable = false, length = 120, name = "nome_exibicao_remetente")
    private String nomeExibicaoRemetente;

    @Column(nullable = false, length = 4000, name = "conteudo_mensagem")
    private String conteudoMensagem;

    @Column(nullable = false, name = "instante_envio")
    private Instant instanteEnvio;
}
