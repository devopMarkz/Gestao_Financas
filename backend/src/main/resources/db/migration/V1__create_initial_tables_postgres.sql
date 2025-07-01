-- Tabela de Usuários
CREATE TABLE tb_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL, -- ENUM: ROLE_ADMIN, ROLE_USER
    ativo BOOLEAN NOT NULL
);

-- Tabela de Categorias
CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- ENUM: RECEITA, DESPESA
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
);

-- Tabela de Transações
CREATE TABLE tb_transacao (
    id BIGSERIAL PRIMARY KEY,
    categoria_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    data_transacao TIMESTAMP NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- ENUM: RECEITA, DESPESA
    observacoes TEXT,
    FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id),
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
);

-- Tabela de Contas Recorrentes
CREATE TABLE tb_conta_recorrente (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    dia_vencimento SMALLINT NOT NULL, -- Ex: 5 (dia 5 de cada mês)
    tipo VARCHAR(50) NOT NULL, -- ENUM: RECEITA, DESPESA
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes TEXT,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
    FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id)
);

-- Criação da tabela de orçamentos mensais por categoria
CREATE TABLE tb_orcamento (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    mes INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    ano INTEGER NOT NULL CHECK (ano >= 2000),
    valor_limite DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
    FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id),
    CONSTRAINT uq_orcamento_usuario_categoria_mes UNIQUE (usuario_id, categoria_id, mes, ano)
);