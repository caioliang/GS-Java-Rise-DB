# Rise API

## 📋 Descrição do Projeto

Rise é a API backend para o projeto Rise — serviço que mantém currículos (resumes), experiências (work/educational experiences) e gera *insights* assíncronos usando um serviço de IA externo (HuggingFace / Spaces). A aplicação é construída com Spring Boot, persiste dados em PostgreSQL, usa RabbitMQ para enfileirar tarefas de geração de insights e emprega cache para melhorar performance.

## 📋 DB-ORACLE - Requisitos:
- **Conta Oracle Fiap**: Declarar variaveis de ambiente: ORACLE_USER e ORACLE_PASSWORD

## 👥 Autores
- **Allan Brito Moreira** - RM558948
- **Caio Liang** - RM558868
- **Levi Magni** - RM98276

## 🚀 Funcionalidades principais

- CRUD de usuários e currículos
- CRUD de experiências (work / educational)
- Geração assíncrona de insights via fila RabbitMQ
- Endpoints paginados para listas (mobile-friendly)
- Autenticação via JWT (Spring Security)
- Integração com um serviço de IA externo para análise/insights

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.5.x
- Spring Data JPA (Hibernate)
- Spring Security (JWT)
- Spring AMQP (RabbitMQ)
- Spring Cache
- Flyway (migrations)
- PostgreSQL
- Gradle (build)

## 📁 Estrutura (resumo)

- `src/main/java` — código principal (controllers, services, repositories, config)
- `src/main/resources/application.properties` — propriedades de configuração

## 🔧 Como rodar (desenvolvimento)

### Pré-requisitos
- Java 17
- Gradle (ou use o wrapper `./gradlew` / `.
\gradlew` no Windows)

### 1) Clonar repositório
```powershell
git clone <url-do-repositorio>
cd "C:\Users\Allan\Desktop\Repositórios\Java\GS 2 - Rise - Azure DevOps"
```

### 2) Rodar a aplicação
Nesta versão não é necessário subir containers separadamente — execute a aplicação Java diretamente. Ela irá utilizar as configurações presentes em `application.properties` (como host/porta do banco e do RabbitMQ) e, se configurado, inicializar os componentes necessários.

No PowerShell (a partir da raiz do projeto):
```powershell
# build
.\gradlew --no-daemon clean assemble

# rodar
.\gradlew --no-daemon bootRun
```

A aplicação iniciará por padrão em `http://localhost:8080`.

### 3) Configurar variáveis de ambiente (exemplos)
- Definir a URL da HuggingFace (ou service de insights) — a aplicação lê `spring.ai.hf.base-url` de `application.properties` ou da variável `INSIGHTS_KEY`:

PowerShell (temporário para sessão):
```powershell
$env:INSIGHTS_KEY = 'https://levmn-fiap-rise-ai.hf.space/gerar-insights'
```

- Configurar chave OpenAI (se usada):
```powershell
$env:API_KEY = 'sk-xxx'
```

## 🧪 Testes

- Para executar os testes:
```powershell
.\gradlew test
```

## 🔁 Rotas principais (endpoints)

Aqui estão os endpoints principais expostos pela API e sua finalidade (resumido):

- `POST /auth/login` — autentica usuário e retorna token JWT.

- `GET /users/{id}` — obtém dados do usuário.
- `POST /users` — registra um novo usuário (usa `RegisterDTO`).
- `PUT /users/{id}` — atualiza dados do usuário.
- `DELETE /users/{id}` — exclui o usuário.

- `GET /users/{userId}/resume` — recupera o `Resume` associado ao usuário.
- `POST /users/{userId}/resume` — cria o `Resume` para o usuário.
- `PUT /users/{userId}/resume` — atualiza o `Resume` do usuário.
- `DELETE /users/{userId}/resume` — remove o `Resume` do usuário.

- `GET /resumes/{resumeId}/work-experiences` — lista paginada de experiências profissionais.
- `GET /resumes/{resumeId}/work-experiences/{id}` — obtém uma experiência profissional específica.
- `POST /resumes/{resumeId}/work-experiences` — cria uma experiência profissional.
- `PUT /resumes/{resumeId}/work-experiences/{id}` — atualiza uma experiência profissional.
- `DELETE /resumes/{resumeId}/work-experiences/{id}` — remove uma experiência profissional.

- `GET /resumes/{resumeId}/educational-experiences` — lista paginada de experiências educacionais.
- `GET /resumes/{resumeId}/educational-experiences/{id}` — obtém uma experiência educacional específica.
- `POST /resumes/{resumeId}/educational-experiences` — cria uma experiência educacional.
- `PUT /resumes/{resumeId}/educational-experiences/{id}` — atualiza uma experiência educacional.
- `DELETE /resumes/{resumeId}/educational-experiences/{id}` — remove uma experiência educacional.

- `POST /users/{userId}/insights` — dispara (async) a geração de insights para o `Resume` do usuário (retorna 202 Accepted).
- `GET /users/{userId}/insights` — retorna o último insight em cache para o `Resume` do usuário, ou 204 se não houver.

- `POST /chat/message` — envia mensagem para serviço de chat/IA (requer autenticação).

> Observação: muitos endpoints aceitam/retornam DTOs (`UserDTO`, `ResumeDTO`, `WorkExperienceDTO`, `EducationalExperienceDTO`, `InsightDTO`). Veja a pasta `src/main/java/br/com/fiap/rise/dto` para o contrato completo.

## 🧩 Entidades de domínio

As principais entidades persistidas no banco são:

- `User` (TB_USER): representa o usuário do sistema — campos principais: `id`, `name`, `cpf`, `birthDate`.
- `Auth` (TB_AUTH): credenciais e informação de login — `email`, `password` (hash), referência ao `User`.
- `Resume` (TB_RESUME): currículo do usuário — `id`, `objective`, referência ao `User`, listas de `workExperiences` e `educationalExperiences`.
- `WorkExperience` (TB_WORK_EXPERIENCE): experiência profissional — `company`, `role`, `startDate`, `endDate`, `description`, referência ao `Resume`.
- `EducationalExperience` (TB_EDUCATIONAL_EXPERIENCE): experiência educacional — `institution`, `course`, `startDate`, `endDate`, `description`, referência ao `Resume`.
- `Insight` (TB_INSIGHTS): registros de insights gerados — `resumeId`, `payload` (JSON), `createdAt`.

Essas entidades têm contratos correspondentes em `src/main/java/br/com/fiap/rise/dto` usados nos endpoints para validação e transferência de dados.
```

## ⚙️ Configurações importantes (em `application.properties`)

- Banco de dados:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/rise
spring.datasource.username=rise
spring.datasource.password=rise
```

- RabbitMQ:
```
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=rise
spring.rabbitmq.password=rise
rise.rabbitmq.queue.insight=rise-insight-generation-queue
```

- Insights (HuggingFace / Spaces):
```
spring.ai.hf.base-url=${INSIGHTS_KEY}
```
Defina a variável `INSIGHTS_KEY` com a URL do serviço de geração de insights, ou altere diretamente `application.properties`.
