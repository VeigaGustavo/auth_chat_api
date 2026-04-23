package com.authcore.modulo.grupo.adaptador.persistencia;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.authcore.modulo.autenticacao.adaptador.seguranca.ServicoCifradoCampoTextoUtf8;
import com.authcore.modulo.autenticacao.adaptador.seguranca.ServicoIndiceDeterministicoRepouso;
import com.authcore.modulo.grupo.dominio.ConviteParticipacaoEmail;
import com.authcore.modulo.grupo.dominio.GrupoColaboracao;
import com.authcore.modulo.grupo.dominio.GrupoColaboracaoRepositorio;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GrupoColaboracaoJpaAdapter implements GrupoColaboracaoRepositorio {

    private final GrupoColaboracaoJpaRepositorio grupoJpa;
    private final ConviteParticipacaoGrupoJpaRepositorio conviteJpa;
    private final ServicoCifradoCampoTextoUtf8 cifrado;
    private final ServicoIndiceDeterministicoRepouso indices;

    @Override
    @Transactional
    public GrupoColaboracao salvar(GrupoColaboracao grupoColaboracao) {
        var ge = new GrupoColaboracaoEntity();
        ge.setId(grupoColaboracao.getId());
        ge.setNomeTitulo(grupoColaboracao.getNomeTitulo());
        ge.setTextoDescricaoOpcional(grupoColaboracao.getTextoDescricaoOpcional());
        ge.setIdContaPropietariaCriadora(grupoColaboracao.getIdContaPropietariaCriadora());
        ge.setInstanteRegistro(grupoColaboracao.getInstanteRegistro());
        ge = grupoJpa.save(ge);

        for (ConviteParticipacaoEmail c : grupoColaboracao.getConvitesParticipacaoEmail()) {
            var ce = new ConviteParticipacaoGrupoEntity();
            ce.setId(c.getId());
            ce.setGrupoColaboracao(ge);
            String norm = c.getEmailCorporativoParaAcesso().trim().toLowerCase();
            ce.setChaveIndiceBuscaEmailParticipante(indices.indiceParaEmailNormalizado(norm));
            ce.setMaterialEmailParticipanteCifrado(cifrado.cifrar(norm));
            ce.setInstanteRegistroConvite(Instant.now());
            conviteJpa.save(ce);
        }

        return buscarPorIdentificador(ge.getId()).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GrupoColaboracao> buscarPorIdentificador(UUID idGrupo) {
        return grupoJpa.findById(idGrupo).map(this::mapearCarregandoConvites);
    }

    private GrupoColaboracao mapearCarregandoConvites(GrupoColaboracaoEntity ge) {
        var convitesEntidade = conviteJpa.findByGrupoColaboracao_Id(ge.getId());
        var lista =
                convitesEntidade.stream()
                        .map(
                                e ->
                                        ConviteParticipacaoEmail.builder()
                                                .id(e.getId())
                                                .emailCorporativoParaAcesso(
                                                        cifrado.decifrar(e.getMaterialEmailParticipanteCifrado()))
                                                .build())
                        .collect(Collectors.toCollection(ArrayList::new));
        return GrupoColaboracao.builder()
                .id(ge.getId())
                .nomeTitulo(ge.getNomeTitulo())
                .textoDescricaoOpcional(ge.getTextoDescricaoOpcional())
                .idContaPropietariaCriadora(ge.getIdContaPropietariaCriadora())
                .instanteRegistro(ge.getInstanteRegistro())
                .convitesParticipacaoEmail(lista)
                .build();
    }
}
