# Habitare

API REST de gestao imobiliaria: cadastro de imoveis e corretores e agendamento de visitas.
Construida em Java 21 com Spring Boot, persistencia em MySQL e autenticacao stateless via JWT.

Autor: Marcos Gabriel Falcao.
Projeto da disciplina de Programacao Web, desenvolvido a partir do template fornecido pelo professor.

## Tecnologias

- Java 21
- Spring Boot 4 (Web MVC)
- Spring Security + JWT (java-jwt)
- Spring Data JPA / Hibernate
- MySQL 8
- MapStruct
- Bean Validation (Jakarta Validation)
- ArchUnit
- Docker / Docker Compose

## Arquitetura

Package by Feature. Cada feature concentra entidade, DTOs, repository, mapper, servicos e controller:

```
br.com.onwebtec.habitare
├── auth        autenticacao, usuarios e emissao de token
├── imovel      imoveis
├── corretor    corretores
├── visita      visitas agendadas
└── shared      seguranca e tratamento de excecoes
    ├── security
    └── exception
```

Fluxo de uma requisicao:

```
Controller  ->  ApplicationService  ->  DomainService (regras de negocio)
                       |                        |
                       +--> Repository ---------+
                       +--> Mapper (DTO <-> entidade)
```

- Controllers falam apenas com o `ApplicationService` e nunca recebem ou devolvem entidades JPA.
- `ApplicationService` e `@Transactional`, orquestra o repositorio, chama o Domain Service e converte com o Mapper.
- `DomainService` concentra as regras de negocio.
- DTOs sao `record` com Bean Validation; todo `@RequestBody` usa `@Valid` (inclusive PUT e PATCH).
- Erros de negocio sao excecoes tratadas por um `@RestControllerAdvice` em `shared/exception`, com respostas no formato `ProblemDetail` (RFC 9457) e sem stack trace.
- A API e stateless (`SessionCreationPolicy.STATELESS`), com CSRF desabilitado; senhas sao gravadas com BCrypt.

## Regras de negocio

- Codigo de imovel e unico; CRECI e e-mail de corretor sao unicos.
- Um imovel com status `VENDIDO` nao pode retornar para outro status.
- Visita so pode ser agendada para imovel com status `DISPONIVEL`.
- Nao e permitido agendar duas visitas para o mesmo imovel no mesmo horario.
- Cada usuario enxerga e cancela apenas as proprias visitas; cancelar visita de terceiros retorna 404.

## Endpoints

| Metodo | Rota | Acesso | Descricao |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | publico | Autentica e devolve o token JWT |
| POST | `/api/auth/register` | publico | Cadastra usuario com perfil USER |
| GET | `/api/imoveis` | publico | Lista imoveis |
| GET | `/api/imoveis/{imovelId}` | publico | Busca imovel por id |
| POST | `/api/imoveis` | ADMIN | Cadastra imovel (201 com `Location`) |
| PUT | `/api/imoveis/{imovelId}` | ADMIN | Atualiza imovel |
| PATCH | `/api/imoveis/{imovelId}` | ADMIN | Altera apenas o status |
| DELETE | `/api/imoveis/{imovelId}` | ADMIN | Remove imovel |
| GET | `/api/corretores` | autenticado | Lista corretores |
| GET | `/api/corretores/{corretorId}` | autenticado | Busca corretor por id |
| POST | `/api/corretores` | ADMIN | Cadastra corretor (201 com `Location`) |
| PUT | `/api/corretores/{corretorId}` | ADMIN | Atualiza corretor |
| DELETE | `/api/corretores/{corretorId}` | ADMIN | Remove corretor |
| GET | `/api/visitas` | autenticado | Lista as visitas do usuario logado |
| POST | `/api/visitas` | autenticado | Agenda visita (201 com `Location`) |
| DELETE | `/api/visitas/{visitaId}` | autenticado | Cancela visita propria |

Todas as rotas nao listadas como publicas exigem o cabecalho `Authorization: Bearer <token>`.

## Perfis e permissoes

- `ROLE_USER`: leitura de imoveis e corretores e gestao das proprias visitas.
- `ROLE_ADMIN`: tudo do USER, mais a escrita de imoveis e corretores (o perfil ADMIN tambem possui `ROLE_USER`).
- O cadastro publico (`/api/auth/register`) cria sempre usuarios com perfil USER.
- O administrador inicial e criado no perfil `dev` pelo `AdminDevSeeder` (e-mail `admin@imob.com`). A senha vem da propriedade `app.admin.password`, que em producao deve ser definida pela variavel de ambiente `ADMIN_PASSWORD`; no ambiente local de desenvolvimento, a senha padrao de desenvolvimento definida no `AdminDevSeeder` e utilizada.

## Formato dos erros

```json
// 400 - erro de validacao (campo "erros" com uma entrada por campo)
{ "type": "about:blank", "title": "Erro de validacao de dados de entrada",
  "status": 400, "detail": "Um ou mais campos estao invalidos. Corrija e tente novamente.",
  "erros": { "preco": "O preco deve ser positivo." }, "timestamp": "2026-01-01T10:00:00Z" }

// 401
{ "status": 401, "title": "Falha na autenticacao", "detail": "Credenciais invalidas. Verifique e-mail e senha." }

// 403
{ "status": 403, "title": "Acesso negado", "detail": "Seu perfil nao possui permissao para este recurso." }

// 404
{ "status": 404, "title": "Recurso nao encontrado", "detail": "Imovel nao encontrado para o identificador: 42" }

// 409
{ "status": 409, "title": "Conflito de dados", "detail": "Ja existe imovel cadastrado com o codigo: AP-001" }
```

## Como rodar localmente

Com Docker (recomendado), na raiz do projeto:

```bash
docker compose up --build
```

A API sobe em <http://localhost:8080> e o MySQL em `localhost:3307`. O perfil padrao e `dev`.

Sem Docker, e necessario Java 21, Maven e um MySQL acessivel conforme `application-dev.properties`:

```bash
mvn spring-boot:run
```

## Deploy

O arquivo `docker-compose.prod.yml` publica a API em `127.0.0.1:8090`, atras de um proxy reverso
(nginx/CloudPanel) que encerra o HTTPS. As variaveis abaixo devem estar em um arquivo `.env` no
mesmo diretorio do compose:

```
MYSQL_ROOT_PASSWORD
DB_PASSWORD
JWT_SECRET
ADMIN_PASSWORD
```

Demo: <https://project.onwebtec.com.br>

## Testes

Testes de arquitetura com ArchUnit:

```bash
mvn test -Dtest=HabitareArquiteturaTestes
```

A colecao do Postman esta em `docs/postman/`. Importe a colecao e ajuste as variaveis `baseUrl`
e `adminPassword`.
