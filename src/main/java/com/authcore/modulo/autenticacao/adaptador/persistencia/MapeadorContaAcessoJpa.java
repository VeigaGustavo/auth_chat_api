package com.authcore.modulo.autenticacao.adaptador.persistencia;

import com.authcore.modulo.autenticacao.dominio.ContaAcesso;
import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import org.springframework.stereotype.Component;

@Component
public class MapeadorContaAcessoJpa {

    public ContaAcesso paraDominio(ContaAcessoEntity e) {
        if (e == null) {
            return null;
        }
        var papel = e.getNivelPapelAcesso() != null ? e.getNivelPapelAcesso() : PapelAcessoSistema.USUARIO_CONVIDADO;
        return ContaAcesso.builder()
                .id(e.getId())
                .emailCorporativo(e.getEmailCorporativo())
                .hashSenha(e.getHashSenha())
                .nomeApresentacao(e.getNomeApresentacao())
                .ativo(e.isAtivo())
                .nivelPapelAcesso(papel)
                .build();
    }

    public void popularEntidade(ContaAcesso dominio, ContaAcessoEntity e) {
        e.setId(dominio.getId());
        e.setEmailCorporativo(dominio.getEmailCorporativo());
        e.setHashSenha(dominio.getHashSenha());
        e.setNomeApresentacao(dominio.getNomeApresentacao());
        e.setAtivo(dominio.isAtivo());
        e.setNivelPapelAcesso(dominio.getNivelPapelAcesso());
    }
}
