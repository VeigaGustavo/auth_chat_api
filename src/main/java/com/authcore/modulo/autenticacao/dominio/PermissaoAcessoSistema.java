package com.authcore.modulo.autenticacao.dominio;

/**
 * Conjunto mínimo de gates que o restante do sistema (ou o cliente) pode validar.
 * O papel {@link PapelAcessoSistema#PROPRIETARIO_PLATAFORMA} aplica <strong>todas</strong> elas.
 */
public enum PermissaoAcessoSistema {
    GERENCIAR_TODAS_CONTAS,
    MODERAR_CONTEUDO_DIGITAL,
    CONFIGURAR_INTEGRACOES,
    ACESSO_AUDITORIA,
    DRENAR_CACHES,
    USAR_CONVERSA_SALAS
}
