# 💰 Gestão de Custos

API REST para controle de despesas pessoais, desenvolvida com **Java 21** e **Spring Boot**. Permite cadastrar despesas e consultá-las por e-mail e data, além de conter endpoints dedicados a comparar a **performance de consultas com e sem paginação** em uma base com grande volume de dados.

## 📑 Sumário

- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura do projeto](#-arquitetura-do-projeto)
- [Como executar](#-como-executar)
- [Endpoints da API](#-endpoints-da-api)
- [Modelo de dados](#-modelo-de-dados)
- [Teste de performance](#-teste-de-performance-com-e-sem-paginação)
- [Próximos passos](#-próximos-passos)
- [Autor](#-autor)

## ✨ Funcionalidades

- Cadastro de despesas com validação de campos obrigatórios
- Consulta de despesas por e-mail
- Consulta de despesas por e-mail e data
- Tratamento de erros com resposta JSON padronizada
- Listagem com e sem paginação para comparação de desempenho
- Seeder para gerar 150.000 registros de teste
- Persistência em banco H2 gravado em arquivo (os dados não se perdem ao reiniciar)

## 🛠 Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem |
| Spring Boot 4.1.2 | Framework principal |
| Spring Web MVC | Camada de API REST |
| Spring Data JPA / Hibernate | Persistência e acesso ao banco |
| H2 Database | Banco de dados em arquivo |
| Lombok | Redução de código repetitivo (getters/setters) |
| Maven (wrapper incluso) | Gerenciamento de dependências e build |
| JUnit 5 | Testes |

## 🗂 Arquitetura do projeto

```
src/main/java/br/com/Juan_J0rge/gestao_custos
├── Controller/
│   └── GestaoDespesasController.java     # Endpoints principais
├── Entity/
│   └── Despesas.java                     # Entidade JPA (tabela "despesa")
├── useCases/
│   ├── CadastroDespesasUseCase.java      # Regra de negócio do cadastro
│   └── BuscarDespesasUseCase.java        # Regra de negócio da busca
├── repository/
│   └── DespesaRepository.java            # Acesso ao banco (Spring Data JPA)
├── custom_message/
│   └── ErrorMessage.java                 # Formato padrão de erro
├── performance/
│   ├── GestaoDespesasPerformance.java    # Endpoints com/sem paginação
│   └── GestaoDespesasSeeder.java         # Gerador de dados de teste
└── GestaoCustosApplication.java          # Classe principal
```

O fluxo segue a separação em camadas:

```
Requisição → Controller → UseCase (regra de negócio) → Repository → Banco H2
```

## 🚀 Como executar

### Pré-requisitos

- [JDK 21](https://adoptium.net/) ou superior
- Git

> Não é necessário instalar o Maven: o projeto inclui o Maven Wrapper (`mvnw`).

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/Juan-J0rge/gestao-custos.git

# 2. Entre na pasta do projeto
cd gestao-custos

# 3. Execute a aplicação
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

A API ficará disponível em `http://localhost:8080`.

### Banco de dados

O banco H2 é criado automaticamente no primeiro start, no arquivo `./data/gestao-despesa.mv.db`. A configuração está em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/gestao-despesa
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

Com `ddl-auto=update`, as tabelas são criadas/atualizadas automaticamente a partir da entidade.

## 📡 Endpoints da API

URL base: `http://localhost:8080/gestao`

### Cadastrar despesa

`POST /gestao/create`

**Body (JSON):**

```json
{
  "descricao": "Almoço",
  "data": "2026-11-02",
  "valor": 35.90,
  "categoria": "Alimentação",
  "email": "usuario@email.com"
}
```

**Resposta de sucesso — `200 OK`:**

```json
{
  "id": "b7e0c1a4-3f5d-4c1e-9a52-1d6f0c2e8a11",
  "descricao": "Almoço",
  "data": "2026-11-02",
  "valor": 35.90,
  "categoria": "Alimentação",
  "email": "usuario@email.com",
  "data_criacao": "2026-11-02"
}
```

**Resposta de erro (campo obrigatório ausente) — `400 Bad Request`:**

```json
{
  "message": "Preencher todos os campos",
  "type": "INVALID_PARAMS"
}
```

### Buscar despesas por e-mail

`GET /gestao/{email}`

```bash
curl http://localhost:8080/gestao/usuario@email.com
```

### Buscar despesas por e-mail e data

`GET /gestao/{email}?data=AAAA-MM-DD`

```bash
curl "http://localhost:8080/gestao/usuario@email.com?data=2026-11-02"
```

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `email` | path | Sim | E-mail do usuário |
| `data` | query | Não | Filtra pela data da despesa (formato `AAAA-MM-DD`) |

### Endpoints de performance

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/gestao/performance/sem-paginacao` | Retorna **todas** as despesas de uma vez |
| `GET` | `/gestao/performance/com-paginacao?page=0&size=20` | Retorna as despesas paginadas |
| `GET` | `/gestao/performance/com-paginacao/{email}?page=0&size=20` | Retorna as despesas de um e-mail, paginadas |

## 🧱 Modelo de dados

Tabela `despesa`:

| Campo | Tipo | Obrigatório | Observações |
|---|---|---|---|
| `id` | UUID | — | Gerado automaticamente |
| `descricao` | String | Sim | Descrição da despesa |
| `data` | LocalDate | Sim | Data em que a despesa ocorreu |
| `valor` | BigDecimal | Sim | Valor da despesa |
| `categoria` | String (máx. 100) | Sim | Categoria da despesa |
| `email` | String | Sim | Identifica o dono da despesa |
| `data_criacao` | LocalDate | — | Preenchido automaticamente na criação |

## ⚡ Teste de performance (com e sem paginação)

O pacote `performance` existe para demonstrar, na prática, o impacto da paginação em consultas com muitos registros.

1. **Gerar os dados:** a classe `GestaoDespesasSeeder` insere **150.000 despesas** de teste (e-mail `performace@gmail.com`, categoria `TESTE`). Ela vem com a anotação `@Component` comentada; para usá-la, descomente-a:

   ```java
   @Component
   public class GestaoDespesasSeeder implements CommandLineRunner {
   ```

   Rode a aplicação **uma vez** e comente a anotação novamente para não duplicar os registros a cada start.

2. **Comparar os endpoints:** chame `sem-paginacao` e `com-paginacao`. O tempo de cada consulta é impresso no console, por exemplo:

   ```
   Tempo sem paginação = ... ms
   Tempo com paginação = ... ms
   ```

## 👨‍💻 Autor

Desenvolvido por **Juan Jorge** — [@Juan-J0rge](https://github.com/Juan-J0rge)
