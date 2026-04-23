package com.authcore.modulo.autenticacao.dominio;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Papel lógico da conta. O {@link #PROPRIETARIO_PLATAFORMA} concentra todas as permissões.
 */
public enum PapelAcessoSistema {

    /**
     * Acesso amplo, equivalente a "todas as permissões" conhecidas.
     */
    PROPRIETARIO_PLATAFORMA,
    /**
     * Conta padrão, só o necessário p/ o dia a dia.
     */
    USUARIO_CONVIDADO;

    public Set<PermissaoAcessoSistema> resolverPermissoesEfetivas() {
        if (this == PROPRIETARIO_PLATAFORMA) {
            return EnumSet.allOf(PermissaoAcessoSistema.class);
        }
        return EnumSet.of(PermissaoAcessoSistema.USAR_CONVERSA_SALAS);
    }

    public List<String> listarNomesChavePermissoes() {
        return resolverPermissoesEfetivas().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
