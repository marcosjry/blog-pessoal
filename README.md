# 📝 Acelera Maker - Blog Pessoal

Este projeto é uma aplicação backend desenvolvida como parte prática com o objetivo de criar uma API RESTful para gerenciamento de um blog pessoal com autenticação de usuários, temas e postagens.

---

## 🚀 Funcionalidades

A API oferece endpoints seguros para:

- 🔐 Autenticação de usuários via token JWT.
- 👥 Cadastro e gerenciamento de usuários.
- 📌 Criação, leitura, atualização e exclusão de postagens.
- 🏷️ Criação, leitura, atualização e exclusão de temas.

---

## 🛠️ Tecnologias Utilizadas

O projeto foi desenvolvido com as seguintes tecnologias:

### 🔧 Backend
- **Java 17**
- **Spring Boot 3.1.8**
    - `spring-boot-starter-web` – Para criação de APIs REST.
    - `spring-boot-starter-data-jpa` – Para persistência com JPA/Hibernate.
    - `spring-boot-starter-security` – Para segurança da aplicação com JWT.
    - `spring-boot-starter-validation` – Para validação dos dados com Jakarta Bean Validation.

### 🗃️ Banco de Dados
- **PostgreSQL** – Banco de dados principal (produção).
- **H2 Database** – Banco de dados em memória (para testes locais).

### 🔐 Autenticação
- **Auth0 Java JWT (4.5.0)** – Para criação e validação de tokens JWT.

### 📖 Documentação
- **SpringDoc OpenAPI (2.2.0)** – Geração automática da documentação da API com Swagger UI.

### 🧪 Testes
- `spring-boot-starter-test` – Framework de testes do Spring.
- `spring-security-test` – Testes com segurança Spring.

### 📊 Qualidade de Código
- **SonarQube** – Análise de qualidade de código e cobertura de testes com o plugin Maven.

---

## 📁 Estrutura do Projeto

```bash
src
├── main
│   ├── java/com/blog/pessoal/acelera/maker
│   │   ├── config          # Camada de configuração do sistema
│   │   ├── controller      # Camada de controladores REST
│   │   ├── DTO             # Camada de DTO's (Data transfer Object)
│   │   ├── exception       # Camada de exceções customizadas
│   │   ├── model           # Entidades JPA
│   │   ├── repository      # Interfaces de acesso ao banco (JPARepository)
│   │   ├── service         # Camada de serviços com regras de negócio
│   │   │   └── impl        # Camada de Implementações dos serviços
│   │   ├── specification   # Consultas consultas dinâmicas ao Banco
│   │   ├── util            # utilitários
│   └── resources
│       ├── application.properties
│       └── ...
└── test
    └── java/...            # Testes automatizados
```
----
## ▶️ Como executar o projeto localmente

### ✅ Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-started)
- [GitHub](https://docs.github.com/en/desktop/installing-and-authenticating-to-github-desktop/installing-github-desktop)

---

### ⬇️ 1. Clone o repositório

```bash
git clone (https://github.com/marcosjry/blog-pessoal.git) acelera-maker-blog
cd acelera-maker-blog
```
### 🐘 2. Suba o banco de dados PostgreSQL com Docker
Este projeto utiliza um banco de dados PostgreSQL. Rode o seguinte comando para criar um container:

```bash
docker run -d \
  --name sonarqube-db \
  -e POSTGRES_USER=sonar \
  -e POSTGRES_PASSWORD=sonar \
  -e POSTGRES_DB=sonarqube \
  postgres:latest
```

### 🔍 3. Suba o SonarQube com Docker
O SonarQube será utilizado para análise de qualidade do código. Utilize a imagem oficial da versão comunitária:
```bash
docker run -d \
  --name sonarqube \
  -p 9000:9000 \
  --link sonarqube-db \
  -e SONAR_JDBC_URL=jdbc:postgresql://sonarqube-db:5432/sonarqube \
  -e SONAR_JDBC_USERNAME=sonar \
  -e SONAR_JDBC_PASSWORD=sonar \
  sonarqube:community
```
### Acesse o SonarQube em http://localhost:9000

*Usuário padrão:* **admin**

*Senha padrão:* **admin**

### ⚙️ 4. Configure o banco de dados da aplicação
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sonarqube
spring.datasource.username=sonar
spring.datasource.password=sonar
spring.jpa.hibernate.ddl-auto=update
```
### 🚀 5. Execute o projeto
```bash
./mvnw spring-boot:run
```
ou
```bash
mvn spring-boot:run
```
### Documentação do Swagger disponível em: http://localhost:8080/swagger-ui/index.html 🚀
