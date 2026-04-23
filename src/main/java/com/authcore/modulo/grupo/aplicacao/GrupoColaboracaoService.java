package com.authcore.modulo.grupo.aplicacao;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.authcore.comum.excecao.NaoEncontradoException;
import com.authcore.comum.excecao.OperacaoNaoPermitidaException;
import com.authcore.modulo.grupo.dominio.ConviteParticipacaoEmail;
import com.authcore.modulo.grupo.dominio.GrupoColaboracao;
import com.authcore.modulo.grupo.dominio.GrupoColaboracaoRepositorio;
import com.authcore.modulo.grupo.aplicacao.comando.DadosCriacaoGrupoColaboracao;
import com.authcore.modulo.grupo.aplicacao.comando.RespostaGrupoColaboracaoCriado;
import com.authcore.modulo.grupo.aplicacao.comando.RespostaGrupoColaboracaoDetalhe;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GrupoColaboracaoService {

    private final GrupoColaboracaoRepositorio grupoColaboracaoRepositorio;

    @Transactional
    public RespostaGrupoColaboracaoCriado criarGrupo(
            DadosCriacaoGrupoColaboracao dados, UUID idContaAutenticadaPropietaria) {
        var emailsDistintos =
                dados.emailsConvidados().stream()
                        .map(e -> e.trim().toLowerCase())
                        .filter(e -> !e.isEmpty())
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        var convites =
                emailsDistintos.stream()
                        .map(
                                email ->
                                        ConviteParticipacaoEmail.builder()
                                                .id(UUID.randomUUID())
                                                .emailCorporativoParaAcesso(email)
                                                .build())
                        .toList();
        Instant agora = Instant.now();
        var grupo =
                GrupoColaboracao.builder()
                        .id(UUID.randomUUID())
                        .nomeTitulo(dados.nomeGrupo().trim())
                        .textoDescricaoOpcional(
                                dados.descricaoOpcional() != null
                                        ? dados.descricaoOpcional().trim()
                                        : null)
                        .idContaPropietariaCriadora(idContaAutenticadaPropietaria)
                        .instanteRegistro(agora)
                        .convitesParticipacaoEmail(convites)
                        .build();
        var salvo = grupoColaboracaoRepositorio.salvar(grupo);
        return new RespostaGrupoColaboracaoCriado(
                salvo.getId(),
                salvo.getNomeTitulo(),
                salvo.getConvitesParticipacaoEmail().size(),
                salvo.getInstanteRegistro());
    }

    @Transactional(readOnly = true)
    public RespostaGrupoColaboracaoDetalhe obterDetalheParaContaCriadora(
            UUID idGrupo, UUID idContaAutenticada) {
        var g =
                grupoColaboracaoRepositorio
                        .buscarPorIdentificador(idGrupo)
                        .orElseThrow(() -> new NaoEncontradoException("Grupo não encontrado."));
        if (!g.getIdContaPropietariaCriadora().equals(idContaAutenticada)) {
            throw new OperacaoNaoPermitidaException("Apenas quem criou o grupo pode ver este detalhe.");
        }
        var emails =
                g.getConvitesParticipacaoEmail().stream()
                        .map(ConviteParticipacaoEmail::getEmailCorporativoParaAcesso)
                        .toList();
        return new RespostaGrupoColaboracaoDetalhe(
                g.getId(),
                g.getNomeTitulo(),
                g.getTextoDescricaoOpcional(),
                g.getIdContaPropietariaCriadora(),
                g.getInstanteRegistro(),
                emails);
    }
}
