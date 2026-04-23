package com.authcore.modulo.autenticacao.adaptador.persistencia;

import com.authcore.modulo.autenticacao.dominio.ContaAcesso;
import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import com.authcore.modulo.autenticacao.adaptador.seguranca.ServicoCifradoCampoTextoUtf8;
import com.authcore.modulo.autenticacao.adaptador.seguranca.ServicoIndiceDeterministicoRepouso;
import org.springframework.stereotype.Component;

@Component
public class MapeadorContaAcessoJpa {

    private final ServicoCifradoCampoTextoUtf8 cifrado;
    private final ServicoIndiceDeterministicoRepouso indices;

    public MapeadorContaAcessoJpa(ServicoCifradoCampoTextoUtf8 cifrado, ServicoIndiceDeterministicoRepouso indices) {
        this.cifrado = cifrado;
        this.indices = indices;
    }

    public ContaAcesso paraDominio(ContaAcessoEntity e) {
        if (e == null) {
            return null;
        }
        var papel = e.getNivelPapelAcesso() != null ? e.getNivelPapelAcesso() : PapelAcessoSistema.USUARIO_CONVIDADO;
        return ContaAcesso.builder()
                .id(e.getId())
                .emailCorporativo(cifrado.decifrar(e.getMaterialEmailCorporativoCifrado()))
                .hashSenha(cifrado.decifrar(e.getMaterialHashSenhaBcryptCifrado()))
                .nomeCompletoTitular(cifrado.decifrar(e.getMaterialNomeCompletoCifrado()))
                .ativo(e.isAtivo())
                .nivelPapelAcesso(papel)
                .build();
    }

    public void popularEntidade(ContaAcesso dominio, ContaAcessoEntity e) {
        String emailNorm = dominio.getEmailCorporativo().trim().toLowerCase();
        e.setId(dominio.getId());
        e.setChaveIndiceBuscaEmailCorporativo(indices.indiceParaEmailNormalizado(emailNorm));
        e.setMaterialEmailCorporativoCifrado(cifrado.cifrar(emailNorm));
        e.setMaterialNomeCompletoCifrado(cifrado.cifrar(dominio.getNomeCompletoTitular()));
        e.setMaterialHashSenhaBcryptCifrado(cifrado.cifrar(dominio.getHashSenha()));
        e.setAtivo(dominio.isAtivo());
        e.setNivelPapelAcesso(dominio.getNivelPapelAcesso());
    }
}
