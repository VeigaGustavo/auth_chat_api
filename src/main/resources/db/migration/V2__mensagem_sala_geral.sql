-- Histórico da sala geral de chat (mensagens persistidas ao enviar via STOMP)

CREATE TABLE mensagem_sala_geral (
    id UUID NOT NULL PRIMARY KEY,
    id_conta_remetente UUID NOT NULL,
    nome_exibicao_remetente VARCHAR(120) NOT NULL,
    conteudo_mensagem VARCHAR(4000) NOT NULL,
    instante_envio TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_mensagem_sala_geral_instante ON mensagem_sala_geral (instante_envio DESC);
