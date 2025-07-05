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



---
## ⚙️ Configuração do Projeto

Para configurar e executar o projeto localmente, siga as instruções abaixo para o Frontend e o Backend.

### Configuração do Frontend

O Frontend é desenvolvido em Vue 2. Certifique-se de ter o Node.js instalado em sua máquina. Se não tiver, você pode baixá-lo em [nodejs.org](https://nodejs.org/).

1. **Instalar Dependências:**
   Navegue até o diretório `frontend` do projeto e execute o seguinte comando para instalar as dependências:
   ```bash
   npm install
   ```

2. **Instalar Vue CLI (se ainda não tiver):**
   O projeto utiliza o Vue CLI. Se você ainda não o tem instalado globalmente, execute:
   ```bash
   npm install -g @vue/cli
   ```

3. **Iniciar o Servidor de Desenvolvimento:**
   Após a instalação das dependências, inicie o servidor de desenvolvimento:
   ```bash
   npm run serve
   ```
   O frontend estará disponível em `http://localhost:8080` (ou outra porta, se 8080 estiver em uso).

### Configuração do Backend

O Backend é desenvolvido em Spring Boot e requer Java 17. Certifique-se de ter o JDK 17 instalado. Você pode baixá-lo no site da Oracle ou usar um gerenciador de versões como o SDKMAN!.

1. **Configuração do Banco de Dados:**
   O projeto utiliza PostgreSQL e Flyway para gerenciar as migrações do banco de dados. Você precisa criar um banco de dados local com o nome `controle_financas`.

   As tabelas serão geradas automaticamente pelo Flyway na primeira execução do backend.

2. **Atualizar Credenciais do Banco de Dados:**
   No arquivo `src/main/resources/application-dev.properties` (ou similar, dependendo da sua configuração de perfil), você precisará atualizar as variáveis de usuário e senha do seu banco de dados local. Procure pelas seguintes linhas e ajuste-as conforme necessário:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/controle_financas
   spring.datasource.username=postgres
   spring.datasource.password=aluno
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.hibernate.ddl-auto=none
   spring.jpa.show-sql=true
   spring.flyway.url=jdbc:postgresql://localhost:5432/controle_financas
   spring.flyway.user=postgres
   spring.flyway.password=aluno
   spring.flyway.schemas=public
   ```
   **Observação:** Altere `postgres` e `aluno` para o seu usuário e senha do PostgreSQL, respectivamente.

3. **Executar o Backend:**
   Você pode executar o backend a partir da sua IDE (IntelliJ IDEA, Eclipse, etc.) ou via linha de comando:
   ```bash
   ./mvnw spring-boot:run
   ```
   (ou `gradle bootRun` se estiver usando Gradle)

   O backend estará disponível em `http://localhost:8081` (ou outra porta configurada).

### Acesso aos Endpoints (Postman/Insomnia)

Para acessar os endpoints da API, especialmente para testes com ferramentas como Postman ou Insomnia, siga o fluxo de autenticação:

1. **Criar um Usuário:**
   Primeiro, crie um novo usuário enviando uma requisição `POST` para a rota de criação de usuários (ex: `/api/usuarios`). Certifique-se de incluir os dados necessários (nome, email, senha, etc.) no corpo da requisição.

2. **Realizar Login e Obter Token:**
   Em seguida, faça uma requisição `POST` para a rota de login (ex: `/api/auth/login`), fornecendo o email e a senha do usuário criado. A resposta incluirá um `access_token`.

3. **Autorizar Requisições:**
   Para todas as requisições subsequentes aos endpoints protegidos, você deve incluir o `access_token` no cabeçalho `Authorization` no formato `Bearer <seu_access_token>`.

   Exemplo de cabeçalho:
   ```
   Authorization: Bearer seu_access_token_aqui
   ```
   Substitua `seu_access_token_aqui` pelo token que você obteve na etapa de login.

---


