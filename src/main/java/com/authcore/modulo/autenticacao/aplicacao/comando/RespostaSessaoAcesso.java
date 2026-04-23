package com.authcore.modulo.autenticacao.aplicacao.comando;

import java.util.List;

public record RespostaSessaoAcesso(
        String tokenAcesso,
        String esquemaAutorizacao,
        int expiraAproximadaEmSegundos,
        String nivelPapelAcesso,
        List<String> permissoesAcessoEfetivas) {}
