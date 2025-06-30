# 💰 Sistema de Controle de Finanças Pessoais Familiar

## 🎯 Objetivo do Projeto

Este projeto tem como foco o desenvolvimento de um sistema web seguro e simples para gerenciar receitas, despesas, categorias e contas recorrentes de uma família. O sistema permite que diferentes membros acessem e acompanhem seus lançamentos financeiros, facilitando a organização das finanças familiares.

---

## 🧱 Arquitetura Geral

O sistema é dividido em duas camadas principais para garantir modularidade e organização:

### Frontend (Vue 2)

Uma interface limpa e direta com Vue 2 + Vue Router, contemplando:

- **Login**: Autenticação dos membros da família.
- **Dashboard Financeiro**: Visualização de saldo total e últimas transações.
- **Transações**: Cadastro e filtro de receitas e despesas.
- **Categorias**: Cadastro de categorias personalizadas por usuário.
- **Contas Recorrentes**: Gestão de contas fixas mensais (como aluguel, energia etc.).

### Backend (Spring Boot)

Uma API REST robusta com Spring Boot, responsável por toda a lógica de negócio, autenticação e persistência dos dados.

- **Segurança com JWT**: Controle de sessão via token.
- **Camadas bem definidas**: Domain, Application, Infrastructure.
- **Validações de regras financeiras**.
- **Controle por usuário**: Cada transação pertence a um usuário.

---

## 🔐 Autenticação e Autorização

O sistema utiliza Spring Security com JWT para autenticação e autorização.

- **Login via e-mail e senha**.
- **Token JWT** é emitido após autenticação e enviado nas requisições seguintes.
- **Controle por perfil**:
  - `ROLE_USER`: pode cadastrar, editar e visualizar suas próprias finanças.
  - `ROLE_ADMIN`: gerenciamento global de usuários e dados (futuramente).
- **Filtros de segurança** garantem que somente usuários autenticados acessem os dados.

---

## 📦 Entidades Principais

### Usuario
- `id`, `nome`, `email`, `senha`, `perfil` (enum), `ativo`

### Categoria
- `id`, `nome`, `tipo` (RECEITA/DESPESA), `usuario_id`, `ativa`

### Transacao
- `id`, `descricao`, `valor`, `data_transacao`, `tipo` (RECEITA/DESPESA), `categoria_id`, `usuario_id`, `observacoes`

### ContaRecorrente
- `id`, `descricao`, `valor`, `dia_vencimento`, `tipo`, `categoria_id`, `usuario_id`, `ativa`, `observacoes`

---

## 🔁 Fluxo de Uso

1. **Usuário realiza login com e-mail e senha**.
2. Recebe um token JWT que será usado nas requisições futuras.
3. Pode visualizar seu saldo, adicionar novas transações, editar ou excluir registros.
4. Cadastra contas recorrentes e as replica automaticamente para o mês atual.
5. Utiliza filtros por período, tipo ou categoria.
6. A qualquer momento pode consultar o histórico completo.

---

## ⚙️ Funcionalidades Resumidas

### Módulo de Finanças
- CRUD de transações (com filtros).
- Cálculo de saldo por período.
- Lançamento de contas recorrentes.
- Cadastro e edição de categorias.

### Módulo de Usuário
- Cadastro e login.
- Proteção por autenticação.
- Perfis e ativação de conta.

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**, **Spring Boot**, **Spring Security**, **JWT**
- **PostgreSQL** (banco de dados)
- **Flyway** (migrations versionadas)
- **Swagger (OpenAPI)** (documentação da API)

### Frontend
- **Vue 2**
- **Vue Router**
- **Axios** (com interceptor para JWT)

---

## 🔮 Futuras Evoluções

- **Geração de gráficos de gastos/receitas**.
- **Notificações por e-mail sobre vencimento de contas**.
- **Lembretes de lançamentos mensais automáticos**.
- **Importação de extratos bancários (CSV)**.
- **Relatórios financeiros PDF**.
- **Perfil ADMIN com visão consolidada de todos os membros da família**.

---

## 👨‍👩‍👧‍👦 Sobre o Projeto

Este sistema foi desenvolvido com o objetivo de facilitar o controle financeiro familiar, ajudando no planejamento, redução de desperdícios e melhoria na saúde financeira de forma simples e acessível a todos da casa.

