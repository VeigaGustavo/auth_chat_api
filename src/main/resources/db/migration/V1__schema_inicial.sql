-- =============================================================================
-- Auth Chat API — schema inicial (PostgreSQL)
-- Alinhado às entidades JPA atuais (conta_acesso com dados cifrados em repouso).
-- Execute numa base vazia ou faça baseline Flyway se já existirem tabelas antigas.
-- =============================================================================

CREATE TABLE conta_acesso (
    id UUID NOT NULL PRIMARY KEY,
    chave_indice_busca_email VARCHAR(64) NOT NULL,
    material_email_cifrado VARCHAR(2048) NOT NULL,
    material_nome_completo_cifrado VARCHAR(4096) NOT NULL,
    material_hash_senha_cifrado VARCHAR(1024) NOT NULL,
    ativo BOOLEAN NOT NULL,
    nivel_papel_acesso VARCHAR(40) NOT NULL,
    CONSTRAINT uk_conta_acesso_indice_email UNIQUE (chave_indice_busca_email)
);

COMMENT ON TABLE conta_acesso IS 'Contas de acesso; PII e hash BCrypt persistidos em material cifrado.';
COMMENT ON COLUMN conta_acesso.chave_indice_busca_email IS 'HMAC determinístico do e-mail normalizado (login sem expor texto).';

CREATE TABLE solicitacao_redefinicao_senha (
    id UUID NOT NULL PRIMARY KEY,
    id_conta_proprietaria UUID NOT NULL,
    indice_busca_token_redefinicao VARCHAR(64) NOT NULL,
    expira_em TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    consumido BOOLEAN NOT NULL,
    CONSTRAINT fk_solicitacao_redef_conta FOREIGN KEY (id_conta_proprietaria)
        REFERENCES conta_acesso (id) ON DELETE CASCADE,
    CONSTRAINT uk_solicitacao_indice_token UNIQUE (indice_busca_token_redefinicao)
);

CREATE INDEX idx_solicitacao_conta ON solicitacao_redefinicao_senha (id_conta_proprietaria);

CREATE TABLE grupo_colaboracao (
    id UUID NOT NULL PRIMARY KEY,
    nome_titulo VARCHAR(200) NOT NULL,
    texto_descricao_opcional VARCHAR(2000),
    id_conta_propietaria_criadora UUID NOT NULL,
    instante_registro TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_grupo_criadora ON grupo_colaboracao (id_conta_propietaria_criadora);

CREATE TABLE convite_participacao_grupo (
    id UUID NOT NULL PRIMARY KEY,
    id_grupo_colaboracao UUID NOT NULL,
    chave_indice_busca_email_participante VARCHAR(64) NOT NULL,
    material_email_participante_cifrado VARCHAR(2048) NOT NULL,
    instante_registro_convite TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_convite_grupo FOREIGN KEY (id_grupo_colaboracao)
        REFERENCES grupo_colaboracao (id) ON DELETE CASCADE,
    CONSTRAINT uk_grupo_indice_email_participante UNIQUE (
        id_grupo_colaboracao,
        chave_indice_busca_email_participante
    )
);
