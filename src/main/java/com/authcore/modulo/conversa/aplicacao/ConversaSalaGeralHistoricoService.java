package com.authcore.modulo.conversa.aplicacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.authcore.modulo.conversa.adaptador.persistencia.MensagemSalaGeralEntity;
import com.authcore.modulo.conversa.adaptador.persistencia.MensagemSalaGeralJpaRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversaSalaGeralHistoricoService {

    private static final int LIMITE_MAXIMO = 200;
    private static final int LIMITE_PADRAO = 50;

    private final MensagemSalaGeralJpaRepositorio repositorio;

    @Transactional
    public void persistirEnvio(MensagemPublicaSalaModel mensagem) {
        var e = new MensagemSalaGeralEntity();
        e.setId(UUID.randomUUID());
        e.setIdContaRemetente(mensagem.idContaRemetente());
        e.setNomeExibicaoRemetente(mensagem.nomeExibicaoRemetente());
        e.setConteudoMensagem(mensagem.conteudoMensagem());
        e.setInstanteEnvio(mensagem.instanteEnvio());
        repositorio.save(e);
    }

    @Transactional(readOnly = true)
    public List<MensagemPublicaSalaModel> listarUltimasOrdenadoCronologicamente(int limiteSolicitado) {
        int n = limiteSolicitado <= 0 ? LIMITE_PADRAO : Math.min(limiteSolicitado, LIMITE_MAXIMO);
        var pagina = PageRequest.of(0, n);
        var linhas = repositorio.buscarUltimasPaginado(pagina);
        var saida = new ArrayList<MensagemPublicaSalaModel>();
        for (var e : linhas) {
            saida.add(
                    new MensagemPublicaSalaModel(
                            e.getIdContaRemetente(),
                            e.getNomeExibicaoRemetente(),
                            e.getConteudoMensagem(),
                            e.getInstanteEnvio()));
        }
        Collections.reverse(saida);
        return saida;
    }
}
