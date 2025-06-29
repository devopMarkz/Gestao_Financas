# 📚 Sistema de Biblioteca Online

## 🎯 Objetivo do Projeto

Este projeto visa desenvolver um sistema web completo para o gerenciamento de uma biblioteca. Ele permitirá que usuários pesquisem livros, façam reservas e acompanhem seus empréstimos de forma intuitiva. Administradores terão controle total sobre o acervo, usuários e todo o ciclo de empréstimos, garantindo segurança e a aplicação de regras de negócio reais para uma gestão eficiente.




## 🧱 Arquitetura Geral

A arquitetura do sistema é dividida em duas partes principais, garantindo modularidade, escalabilidade e manutenibilidade:

### Frontend (Vue 2)

Uma interface de usuário simples e responsiva, desenvolvida com Vue 2 e Vue Router para navegação fluida entre as seguintes páginas:

*   **Login:** Autenticação de usuários.
*   **Lista de Livros:** Exibição do acervo disponível para pesquisa.
*   **Detalhes do Livro:** Informações detalhadas sobre um livro específico.
*   **Minhas Reservas:** Acompanhamento das reservas e empréstimos do usuário.
*   **Página Administrativa:** Um resumo simples para administradores.

### Backend (Spring Boot)

Um backend robusto e seguro, construído com Spring Boot, responsável por toda a lógica de negócio, persistência de dados e autenticação. Inclui:

*   **Autenticação JWT:** Para segurança e controle de acesso.
*   **Regras de Negócio:** Implementação de lógicas complexas para o gerenciamento da biblioteca.
*   **Múltiplas Entidades e Relacionamentos:** Estrutura de dados bem definida para gerenciar livros, usuários, reservas, empréstimos e auditoria.




## 🔐 Autenticação e Autorização (Spring Security + JWT)

O sistema implementa um robusto mecanismo de autenticação e autorização baseado em JSON Web Tokens (JWT) e Spring Security, garantindo que apenas usuários autorizados acessem os recursos apropriados.

*   **Login:** Usuários se autenticam com e-mail e senha.
*   **Token JWT:** Após o login bem-sucedido, um token JWT é retornado e utilizado em todas as requisições subsequentes para validar a identidade e as permissões do usuário.
*   **Permissões por Perfil:** O sistema define diferentes níveis de acesso:
    *   `ROLE_USER`: Usuários comuns podem pesquisar e reservar livros, além de visualizar seu histórico de reservas e empréstimos.
    *   `ROLE_ADMIN`: Administradores possuem privilégios para gerenciar o acervo de livros, usuários e todo o ciclo de empréstimos.
*   **Filtros de Segurança:** Filtros de segurança baseados em JWT são aplicados para proteger os endpoints da API, garantindo que apenas requisições válidas e autorizadas sejam processadas.




## 📦 Entidades Principais

A modelagem de dados é fundamental para a robustez do sistema. As entidades principais e seus relacionamentos são:

### Usuario
*   `id` (PK)
*   `nome`
*   `email` (único)
*   `senha` (criptografada)
*   `perfil` (enum: ADMIN, USER)
*   `ativo` (boolean)

### Livro
*   `id` (PK)
*   `titulo`
*   `autor`
*   `ano`
*   `descricao`
*   `isbn` (único, para identificação padrão)
*   `editora`
*   `genero`

### CopiaLivro
Representa uma cópia física específica de um livro, permitindo o gerenciamento individual de cada exemplar.
*   `id` (PK)
*   `livro` (FK para Livro)
*   `numeroSerie` (identificador único da cópia)
*   `status` (enum: DISPONIVEL, EMPRESTADO, EM_MANUTENCAO, PERDIDO, DANIFICADO)
*   `condicao` (enum: NOVO, BOM, REGULAR, RUIM)
*   `dataAquisicao`

### Reserva
*   `id` (PK)
*   `usuario` (FK para Usuario)
*   `livro` (FK para Livro - a reserva é para o título, a cópia é atribuída no empréstimo)
*   `dataReserva`
*   `dataExpiracao` (data limite para o usuário retirar o livro)
*   `status` (enum: PENDENTE, APROVADA, CANCELADA)
*   `dataAprovacao`

### Emprestimo
*   `id` (PK)
*   `reserva` (FK para Reserva - opcional, pode ser empréstimo direto)
*   `usuario` (FK para Usuario)
*   `copiaLivro` (FK para CopiaLivro - qual cópia específica foi emprestada)
*   `dataRetirada`
*   `dataPrevistaDevolucao`
*   `dataDevolucaoReal`
*   `status` (enum: EM_ANDAMENTO, DEVOLVIDO, ATRASADO, CANCELADO)
*   `multa` (valor da multa, se houver)

### LogAuditoria
Registra todas as ações administrativas para fins de auditoria e rastreabilidade.
*   `id` (PK)
*   `usuarioResponsavel` (FK para Usuario)
*   `acao`
*   `entidadeAfetada`
*   `dataHora`
*   `descricao`




## 🔁 Fluxo de Uso

O sistema foi projetado para um fluxo de uso intuitivo e eficiente para usuários e administradores:

1.  **Autenticação:** Usuário acessa a página de login e autentica-se com suas credenciais.
2.  **Sessão Segura:** Um token JWT é salvo e utilizado automaticamente em todas as requisições subsequentes, garantindo a segurança da sessão.
3.  **Visualização de Livros:** Na página inicial, o usuário visualiza os livros disponíveis no acervo.
4.  **Reserva de Livro:** O usuário pode reservar um livro de seu interesse.
5.  **Acesso Administrativo:** O administrador acessa a área de administração para gerenciar as operações da biblioteca.
6.  **Gerenciamento de Reservas:** O administrador visualiza todas as reservas pendentes.
7.  **Aprovação/Negação de Reservas:** O administrador aprova ou nega reservas. Reservas aprovadas são convertidas em empréstimos.
8.  **Cálculo de Devolução:** O sistema calcula a data prevista de devolução para cada empréstimo.
9.  **Relatórios de Atraso:** Empréstimos atrasados são destacados em relatórios gerenciais.
10. **Auditoria:** Toda ação realizada por um administrador é registrada na entidade de auditoria para rastreabilidade e segurança.




## ⚙️ Funcionalidades Resumidas

### Usuário
*   Cadastro e Login.
*   Busca de livros no acervo.
*   Reserva de livros.
*   Visualização do histórico de reservas e empréstimos.

### Administrador
*   **CRUD (Create, Read, Update, Delete) de Livros:** Gerenciamento completo do acervo.
*   Visualização e gerenciamento de todas as reservas.
*   Aprovação ou recusa de reservas.
*   Registro de devoluções de livros.
*   **Relatórios Gerenciais:**
    *   Livros mais emprestados.
    *   Usuários com mais empréstimos.
    *   Empréstimos atrasados.
    *   Relatório de desempenho geral da biblioteca.




## 📊 Extras

*   **Auditoria de Ações Administrativas:** Registro detalhado de todas as operações realizadas por administradores para conformidade e segurança.
*   **Sistema de Logs:** Logs abrangentes para análise de uso, depuração e monitoramento de performance.
*   **Relatório de Desempenho da Biblioteca:** Visão geral e detalhada sobre o funcionamento da biblioteca, incluindo métricas de empréstimos e reservas.




## 🛠️ Tecnologias Utilizadas

### Backend
*   **Java 17:** Linguagem de programação principal.
*   **Spring Boot (Web, JPA, Security, Validation):** Framework para desenvolvimento rápido e robusto de APIs.
*   **JWT + Spring Security:** Para autenticação e autorização seguras.
*   **MySQL ou PostgreSQL:** Banco de dados relacional para persistência de dados.
*   **Flyway (opcional):** Ferramenta para controle de versão de banco de dados.
*   **Swagger (OpenAPI):** Para documentação interativa da API.

### Frontend
*   **Vue 2:** Framework JavaScript para construção da interface do usuário.
*   **Vue Router:** Para gerenciamento de rotas na aplicação frontend.
*   **Axios:** Cliente HTTP para realizar requisições à API backend.




## 🔮 Evoluções Futuras

Para expandir ainda mais as funcionalidades e aprimorar a experiência do usuário, as seguintes evoluções podem ser consideradas:

*   **Upload de Imagem:** Funcionalidade para upload de imagens de capa dos livros.
*   **Notificações por E-mail:** Envio automático de e-mails para reservas, devoluções e atrasos.
*   **Sistema de Pontuação:** Implementação de um sistema de pontuação para usuários ativos, incentivando o engajamento.
*   **Múltiplas Bibliotecas:** Suporte para gerenciamento de múltiplas unidades de biblioteca, com controle de acervo e usuários por unidade.
*   **Integração com APIs Externas:** Possibilidade de integrar com APIs de catálogos de livros (ex: Google Books) para preenchimento automático de dados de livros.
*   **Busca Avançada:** Implementação de busca full-text com tecnologias como Elasticsearch para resultados mais relevantes e rápidos.
*   **Sistema de Avaliação/Comentários:** Usuários podem avaliar e comentar sobre os livros.
