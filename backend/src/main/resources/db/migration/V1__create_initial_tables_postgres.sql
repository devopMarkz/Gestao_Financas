-- V1__create_initial_tables_postgresql.sql

CREATE TABLE usuario (
                         id BIGSERIAL PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         perfil VARCHAR(50) NOT NULL, -- ENUM: ROLE_ADMIN, ROLE_USER
                         ativo BOOLEAN NOT NULL
);

CREATE TABLE livro (
                       id BIGSERIAL PRIMARY KEY,
                       titulo VARCHAR(255) NOT NULL,
                       autor VARCHAR(255) NOT NULL,
                       ano INT,
                       descricao TEXT,
                       isbn VARCHAR(20) UNIQUE,
                       editora VARCHAR(255),
                       genero VARCHAR(255)
);

CREATE TABLE copia_livro (
                             id BIGSERIAL PRIMARY KEY,
                             livro_id BIGINT NOT NULL,
                             numero_serie VARCHAR(255) NOT NULL UNIQUE,
                             status VARCHAR(50) NOT NULL, -- ENUM: DISPONIVEL, EMPRESTADO, EM_MANUTENCAO, PERDIDO, DANIFICADO
                             condicao VARCHAR(50), -- ENUM: NOVO, BOM, REGULAR, RUIM
                             data_aquisicao DATE,
                             FOREIGN KEY (livro_id) REFERENCES livro(id)
);

CREATE TABLE reserva (
                         id BIGSERIAL PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         livro_id BIGINT NOT NULL,
                         data_reserva TIMESTAMP NOT NULL,
                         data_expiracao TIMESTAMP,
                         status VARCHAR(50) NOT NULL, -- ENUM: PENDENTE, APROVADA, CANCELADA
                         data_aprovacao TIMESTAMP,
                         FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                         FOREIGN KEY (livro_id) REFERENCES livro(id)
);

CREATE TABLE emprestimo (
                            id BIGSERIAL PRIMARY KEY,
                            reserva_id BIGINT,
                            usuario_id BIGINT NOT NULL,
                            copia_livro_id BIGINT NOT NULL,
                            data_retirada TIMESTAMP NOT NULL,
                            data_prevista_devolucao TIMESTAMP NOT NULL,
                            data_devolucao_real TIMESTAMP,
                            status VARCHAR(50) NOT NULL, -- ENUM: EM_ANDAMENTO, DEVOLVIDO, ATRASADO, CANCELADO
                            multa DECIMAL(10, 2),
                            FOREIGN KEY (reserva_id) REFERENCES reserva(id),
                            FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                            FOREIGN KEY (copia_livro_id) REFERENCES copia_livro(id)
);

CREATE TABLE log_auditoria (
                               id BIGSERIAL PRIMARY KEY,
                               usuario_responsavel_id BIGINT NOT NULL,
                               acao VARCHAR(255) NOT NULL,
                               entidade_afetada VARCHAR(255) NOT NULL,
                               data_hora TIMESTAMP NOT NULL,
                               descricao TEXT,
                               FOREIGN KEY (usuario_responsavel_id) REFERENCES usuario(id)
);