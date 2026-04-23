package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.authcore.modulo.autenticacao.dominio.SolicitacaoRedefinicaoSenhaPendente;
import com.authcore.modulo.autenticacao.dominio.SolicitacaoRedefinicaoSenhaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SolicitacaoRedefinicaoJpaAdapter implements SolicitacaoRedefinicaoSenhaRepositorio {

    private final SolicitacaoRedefinicaoJpaRepositorio jpa;

    @Override
    public SolicitacaoRedefinicaoSenhaPendente salvar(SolicitacaoRedefinicaoSenhaPendente d) {
        var e = new SolicitacaoRedefinicaoSenhaEntity();
        e.setId(d.getId());
        e.setIdContaProprietaria(d.getIdContaProprietaria());
        e.setTokenOpacoUsoUnico(d.getTokenOpacoUsoUnico());
        e.setExpiraEm(d.getExpiraEm());
        e.setConsumido(d.isConsumido());
        e = jpa.save(e);
        return paraDominio(e);
    }

    @Override
    public Optional<SolicitacaoRedefinicaoSenhaPendente> buscarPorTokenAtivoNaoVencido(String token) {
        return jpa.findAtivoPorToken(token, Instant.now()).map(this::paraDominio);
    }

    @Override
    @Transactional
    public void marcarComoConsumidoPorId(UUID id) {
        jpa.marcarConsumido(id);
    }

    private SolicitacaoRedefinicaoSenhaPendente paraDominio(SolicitacaoRedefinicaoSenhaEntity e) {
        return SolicitacaoRedefinicaoSenhaPendente.builder()
                .id(e.getId())
                .idContaProprietaria(e.getIdContaProprietaria())
                .tokenOpacoUsoUnico(e.getTokenOpacoUsoUnico())
                .expiraEm(e.getExpiraEm())
                .consumido(e.isConsumido())
                .build();
    }
}
