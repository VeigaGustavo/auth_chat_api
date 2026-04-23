package com.authcore.modulo.autenticacao.aplicacao;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.authcore.comum.excecao.CredenciaisInvalidasException;
import com.authcore.comum.excecao.NaoEncontradoException;
import com.authcore.comum.excecao.RegraNegocioException;
import com.authcore.modulo.autenticacao.dominio.ContaAcesso;
import com.authcore.modulo.autenticacao.dominio.ContaAcessoRepositorio;
import com.authcore.modulo.autenticacao.dominio.PortaCodificadorSenha;
import com.authcore.modulo.autenticacao.dominio.PortaProvedorTokenAcesso;
import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import com.authcore.modulo.autenticacao.dominio.SolicitacaoRedefinicaoSenhaPendente;
import com.authcore.modulo.autenticacao.dominio.SolicitacaoRedefinicaoSenhaRepositorio;
import com.authcore.modulo.autenticacao.aplicacao.comando.CredenciaisEntrada;
import com.authcore.modulo.autenticacao.aplicacao.comando.DadosRedefinicaoSenha;
import com.authcore.modulo.autenticacao.aplicacao.comando.RespostaSessaoAcesso;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Orquestra casos de uso de autenticação e redefinição de senha.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoService {

    private static final int HORAS_VALIDADE_REDEFINICAO = 1;

    private final ContaAcessoRepositorio contaAcessoRepositorio;
    private final SolicitacaoRedefinicaoSenhaRepositorio solicitacaoRedefinicaoSenhaRepositorio;
    private final PortaCodificadorSenha portaCodificadorSenha;
    private final PortaProvedorTokenAcesso portaProvedorTokenAcesso;

    @Transactional(readOnly = true)
    public RespostaSessaoAcesso entrarComCredenciais(CredenciaisEntrada credenciais) {
        var conta = contaAcessoRepositorio
                .buscarPorEmailCorporativo(credenciais.emailCorporativo().trim().toLowerCase())
                .orElseThrow(CredenciaisInvalidasException::new);
        conta.garantirPodeAcessar();
        if (!portaCodificadorSenha.corresponde(credenciais.senhaAcesso(), conta.getHashSenha())) {
            throw new CredenciaisInvalidasException();
        }
        String token = portaProvedorTokenAcesso.emitirToken(conta);
        int segundos = Math.max(1, Math.toIntExact(portaProvedorTokenAcesso.validadeAproximadaEmSegundos()));
        var papeis = conta.getNivelPapelAcesso() != null
                ? conta.getNivelPapelAcesso()
                : PapelAcessoSistema.USUARIO_CONVIDADO;
        return new RespostaSessaoAcesso(
                token, "Bearer", segundos, papeis.name(), papeis.listarNomesChavePermissoes());
    }

    // Evitar acoplamento: extrair duração via propriedades se necessário; valor fixo documentado
    @Transactional
    public void solicitarRedefinicaoSenhaPeloEmail(String emailCorporativo) {
        var opt = contaAcessoRepositorio.buscarPorEmailCorporativo(
                emailCorporativo == null ? "" : emailCorporativo.trim().toLowerCase());
        if (opt.isEmpty()) {
            // Não vaza existência de e-mail; apenas loga em nível de debug.
            log.debug("Redefinição pedida para e-mail ausente: {}", emailCorporativo);
            return;
        }
        var conta = opt.get();
        var tokenOpaco = UUID.randomUUID().toString();
        var solicitacao = SolicitacaoRedefinicaoSenhaPendente.builder()
                .id(UUID.randomUUID())
                .idContaProprietaria(conta.getId())
                .tokenOpacoUsoUnico(tokenOpaco)
                .expiraEm(Instant.now().plus(HORAS_VALIDADE_REDEFINICAO, ChronoUnit.HOURS))
                .consumido(false)
                .build();
        solicitacaoRedefinicaoSenhaRepositorio.salvar(solicitacao);
        log.info("Fluxo redefinição: link fictício p/ e-mail={} com token={}", emailCorporativo, tokenOpaco);
    }

    @Transactional
    public void redefinirSenhaComTokenOpaco(DadosRedefinicaoSenha dados) {
        if (dados.novaSenhaAcesso() == null || dados.novaSenhaAcesso().length() < 6) {
            throw new RegraNegocioException("Senha precisa de ao menos 6 caracteres.");
        }
        var pendente = solicitacaoRedefinicaoSenhaRepositorio
                .buscarPorTokenAtivoNaoVencido(dados.tokenRedefinicao())
                .orElseThrow(
                        () -> new NaoEncontradoException("Token de redefinição inválido ou expirado."));
        if (pendente.isConsumido()) {
            throw new RegraNegocioException("Este link de redefinição já foi utilizado.");
        }
        if (pendente.getExpiraEm().isBefore(Instant.now())) {
            throw new RegraNegocioException("Token de redefinição expirado.");
        }
        var conta = contaAcessoRepositorio
                .buscarPorId(pendente.getIdContaProprietaria())
                .orElseThrow(() -> new NaoEncontradoException("Conta vinculada ao token não encontrada."));
        var nova = ContaAcesso.builder()
                .id(conta.getId())
                .emailCorporativo(conta.getEmailCorporativo())
                .hashSenha(portaCodificadorSenha.codificar(dados.novaSenhaAcesso()))
                .nomeApresentacao(conta.getNomeApresentacao())
                .ativo(conta.isAtivo())
                .nivelPapelAcesso(
                        conta.getNivelPapelAcesso() != null
                                ? conta.getNivelPapelAcesso()
                                : PapelAcessoSistema.USUARIO_CONVIDADO)
                .build();
        contaAcessoRepositorio.salvar(nova);
        solicitacaoRedefinicaoSenhaRepositorio.marcarComoConsumidoPorId(pendente.getId());
    }
}
