package com.authcore.modulo.autenticacao.adaptador.persistencia;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.authcore.modulo.autenticacao.dominio.ContaAcesso;
import com.authcore.modulo.autenticacao.dominio.ContaAcessoRepositorio;
import com.authcore.modulo.autenticacao.adaptador.seguranca.ServicoIndiceDeterministicoRepouso;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContaAcessoJpaAdapter implements ContaAcessoRepositorio {

    private final ContaAcessoJpaRepositorio jpa;
    private final MapeadorContaAcessoJpa mapeador;
    private final ServicoIndiceDeterministicoRepouso indices;

    @Override
    public Optional<ContaAcesso> buscarPorEmailCorporativo(String emailCorporativo) {
        String norm = emailCorporativo == null ? "" : emailCorporativo.trim().toLowerCase();
        String chave = indices.indiceParaEmailNormalizado(norm);
        return jpa.findByChaveIndiceBuscaEmailCorporativo(chave).map(mapeador::paraDominio);
    }

    @Override
    public Optional<ContaAcesso> buscarPorId(UUID id) {
        return jpa.findById(id).map(mapeador::paraDominio);
    }

    @Override
    public ContaAcesso salvar(ContaAcesso contaAcesso) {
        var e = jpa
                .findById(contaAcesso.getId())
                .orElseGet(ContaAcessoEntity::new);
        mapeador.popularEntidade(contaAcesso, e);
        return mapeador.paraDominio(jpa.save(e));
    }
}
