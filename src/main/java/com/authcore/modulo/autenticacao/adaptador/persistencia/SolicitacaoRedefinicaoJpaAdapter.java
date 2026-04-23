package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.authcore.modulo.autenticacao.dominio.SolicitacaoRedefinicaoSenhaPendente;
import com.authcore.modulo.autenticacao.dominio.SolicitacaoRedefinicaoSenhaRepositorio;
import com.authcore.modulo.autenticacao.adaptador.seguranca.ServicoIndiceDeterministicoRepouso;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SolicitacaoRedefinicaoJpaAdapter implements SolicitacaoRedefinicaoSenhaRepositorio {

    private final SolicitacaoRedefinicaoJpaRepositorio jpa;
    private final ServicoIndiceDeterministicoRepouso indices;

    @Override
    public SolicitacaoRedefinicaoSenhaPendente salvar(SolicitacaoRedefinicaoSenhaPendente d) {
        var e = new SolicitacaoRedefinicaoSenhaEntity();
        e.setId(d.getId());
        e.setIdContaProprietaria(d.getIdContaProprietaria());
        e.setIndiceBuscaTokenRedefinicao(indices.indiceParaTokenRedefinicaoSenha(d.getTokenOpacoUsoUnico()));
        e.setExpiraEm(d.getExpiraEm());
        e.setConsumido(d.isConsumido());
        e = jpa.save(e);
        return paraDominio(e, d.getTokenOpacoUsoUnico());
    }

    @Override
    public Optional<SolicitacaoRedefinicaoSenhaPendente> buscarPorTokenAtivoNaoVencido(String token) {
        String indice = indices.indiceParaTokenRedefinicaoSenha(token);
        return jpa.findAtivoPorIndiceToken(indice, Instant.now()).map(e -> paraDominio(e, token));
    }

    @Override
    @Transactional
    public void marcarComoConsumidoPorId(UUID id) {
        jpa.marcarConsumido(id);
    }

    private SolicitacaoRedefinicaoSenhaPendente paraDominio(SolicitacaoRedefinicaoSenhaEntity e, String tokenOpaco) {
        return SolicitacaoRedefinicaoSenhaPendente.builder()
                .id(e.getId())
                .idContaProprietaria(e.getIdContaProprietaria())
                .tokenOpacoUsoUnico(tokenOpaco)
                .expiraEm(e.getExpiraEm())
                .consumido(e.isConsumido())
                .build();
    }
}
