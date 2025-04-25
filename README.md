# Logica Challenge API

API REST para gerenciamento de torneios de lógica, desenvolvida com Java 17 e Spring Boot.

## 📦 Funcionalidades

- Cadastro e gerenciamento de jogadores
- Criação e finalização de torneios
- Participação de jogadores nos torneios
- Desafios de lógica: Fibonacci, Palíndromo e Ordenação
- Registro de pontuação por desafio
- Rankings globais e por torneio

## 🚀 Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- JUnit 5 & Mockito
- Jacoco (cobertura de testes)
- Maven

## ▶️ Executando o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/faustoneris/logicalgame.git
   ```

2. Execute com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

3. Acesse:
   - API: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:testdb`

## 🧪 Testes

Para rodar os testes e gerar relatório de cobertura:

```bash
./mvnw test
./mvnw jacoco:report
```

Relatório de cobertura será gerado em:
```
target/site/jacoco/index.html
```

## 📘 Endpoints Principais

### Players
- `POST /players`
- `GET /players/{playerId}`
- `GET /players`
- `GET /players/name/{name}`
- `PUT /players/{playerId}`
- `DELETE /players/{playerId}`

### Tournaments
- `GET /tournaments/{tournamentId}/players`
- `POST /tournaments`
- `POST /tournaments/{tournamentId}/finish`
- `POST /tournaments/{tournamentId}/upload`
- `PUT /tournaments/{tournamentId}/players/{playerId}`
- `DELETE /tournaments/{tournamentId}/players/{playerId}`

### Challenges
- `POST /challenges/fibonacci?n=10`
- `POST /challenges/palindrome?input=arara`
- `POST /challenges/sort`
- `POST /challenges/score`

### Rankings
- `GET /rankings/global`
- `GET /rankings/tournament/{tournamentId}`
