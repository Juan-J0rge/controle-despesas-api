# Gestão de Custos

Uma API REST que desenvolvi para gerenciar despesas pessoais. O projeto permite cadastrar e consultar despesas por e-mail e data, utilizando Java e Spring Boot.

Além das operações de cadastro e consulta, também implementei uma parte dedicada a testes de performance, comparando o tempo de resposta de consultas com e sem paginação em uma base com 150 mil registros.

---

## Funcionalidades

- Cadastro de despesas com validação dos campos obrigatórios.
- Consulta de despesas por e-mail.
- Filtro de despesas por e-mail e data.
- Tratamento de erros com respostas JSON padronizadas.
- Consultas com e sem paginação para comparação de desempenho.
- Geração de 150 mil registros para testes de performance.
- Persistência dos dados em banco H2, armazenado em arquivo.

---

## Tecnologias utilizadas

| Tecnologia | Aplicação no projeto |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 4.1.2 | Estrutura da aplicação |
| Spring Web MVC | Criação dos endpoints REST |
| Spring Data JPA / Hibernate | Persistência e acesso aos dados |
| H2 Database | Banco de dados |
| Lombok | Redução de código repetitivo |
| Maven | Gerenciamento de dependências |
| JUnit 5 | Testes automatizados |

---

## Arquitetura do projeto

```text
src/main/java/br/com/Juan_J0rge/gestao_custos
├── Controller/
│   └── GestaoDespesasController.java
├── Entity/
│   └── Despesas.java
├── useCases/
│   ├── CadastroDespesasUseCase.java
│   └── BuscarDespesasUseCase.java
├── repository/
│   └── DespesaRepository.java
├── custom_message/
│   └── ErrorMessage.java
├── performance/
│   ├── GestaoDespesasPerformance.java
│   └── GestaoDespesasSeeder.java
└── GestaoCustosApplication.java
```

A aplicação está organizada em camadas, separando as responsabilidades de cada parte do sistema.

O fluxo de uma requisição funciona da seguinte forma:

```text
Requisição → Controller → UseCase → Repository → Banco de dados
```

O Controller recebe as requisições, os UseCases concentram as regras de negócio e o Repository fica responsável pelo acesso aos dados.

---

## Como executar

### Pré-requisitos

- JDK 21 ou superior
- Git

Não é necessário instalar o Maven separadamente, pois o projeto já possui o Maven Wrapper.

### 1. Clone o repositório

```bash
git clone https://github.com/Juan-J0rge/gestao-custos.git
```

### 2. Entre na pasta

```bash
cd gestao-custos
```

### 3. Execute a aplicação

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### Banco de dados

O projeto utiliza o H2 em modo arquivo. Na primeira execução, o banco é criado automaticamente e os dados ficam armazenados localmente.

Configuração em `application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/gestao-despesa
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

---

## Endpoints da API

URL base:

```text
http://localhost:8080/gestao
```

### Cadastrar despesa

`POST /gestao/create`

Exemplo de requisição:

```json
{
  "descricao": "Almoço",
  "data": "2026-11-02",
  "valor": 35.90,
  "categoria": "Alimentação",
  "email": "usuario@email.com"
}
```

Resposta de sucesso (`200 OK`):

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

Resposta de erro (`400 Bad Request`):

```json
{
  "message": "Preencher todos os campos",
  "type": "INVALID_PARAMS"
}
```

### Consultar despesas por e-mail

`GET /gestao/{email}`

```bash
curl http://localhost:8080/gestao/usuario@email.com
```

### Consultar despesas por e-mail e data

`GET /gestao/{email}?data=AAAA-MM-DD`

```bash
curl "http://localhost:8080/gestao/usuario@email.com?data=2026-11-02"
```

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `email` | Path | Sim | E-mail utilizado na consulta |
| `data` | Query | Não | Filtra despesas por data |

### Endpoints de performance

Também criei endpoints específicos para comparar consultas com e sem paginação.

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/gestao/performance/sem-paginacao` | Retorna todas as despesas |
| `GET` | `/gestao/performance/com-paginacao?page=0&size=20` | Retorna despesas paginadas |
| `GET` | `/gestao/performance/com-paginacao/{email}?page=0&size=20` | Consulta despesas de um e-mail com paginação |

---

## Modelo de dados

A entidade `Despesas` representa os registros armazenados na tabela `despesa`.

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `id` | UUID | Não | Identificador gerado automaticamente |
| `descricao` | String | Sim | Descrição da despesa |
| `data` | LocalDate | Sim | Data da despesa |
| `valor` | BigDecimal | Sim | Valor registrado |
| `categoria` | String | Sim | Categoria da despesa |
| `email` | String | Sim | Identificação do usuário |
| `data_criacao` | LocalDate | Não | Data de criação do registro |

---

## Teste de performance

Uma das partes que desenvolvi foi um teste para observar como a paginação pode influenciar o desempenho das consultas quando existe um volume maior de registros.

Para isso, criei a classe `GestaoDespesasSeeder`, responsável por inserir 150.000 despesas fictícias no banco de dados.

### Como executar o teste

1. Abra a classe `GestaoDespesasSeeder`.
2. Descomente a anotação `@Component`.
3. Execute a aplicação para gerar os registros.
4. Depois da execução, comente novamente a anotação para evitar que os dados sejam inseridos novamente.
5. Acesse os endpoints de consulta com e sem paginação.

O tempo de execução de cada consulta é exibido no console:

```text
Tempo sem paginação = ... ms
Tempo com paginação = ... ms
```

Assim, é possível comparar os resultados e observar a diferença entre recuperar todos os registros de uma vez e consultar os dados em páginas menores.

---

## Demo online

A API também está disponível online:

[https://deploy-controle-despesas-api.onrender.com](https://deploy-controle-despesas-api.onrender.com)

Para realizar cadastros, utilize ferramentas como Postman, Insomnia ou Hoppscotch. As consultas GET também podem ser realizadas diretamente pelo navegador.

---

## Autor

**Juan Jorge**

Estudante de Ciência da Computação e desenvolvedor com foco em Java e Spring Boot.

[GitHub](https://github.com/Juan-J0rge) • [LinkedIn](https://www.linkedin.com/in/devjuan)
