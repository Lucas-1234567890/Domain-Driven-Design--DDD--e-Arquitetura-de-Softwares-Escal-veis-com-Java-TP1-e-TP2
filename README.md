# payment-service

Microsserviço de Pagamento extraído do monólito legado **[ecommerce-legado-ddd](https://github.com/leoinfnet/ecommerce-legado-ddd)**, aplicando DDD (Aggregate Root, Value Objects, Portas e Adaptadores) e a estratégia de migração **Strangler Fig + Branch by Abstraction**.

Este projeto é o material de apoio da Questão 14 do TP1 (DDD e Arquitetura de Softwares Escaláveis com Java).

## Prints

| # | Preview | Descrição |
|---|---|---|
| 1 | <img src="docs/screenshots/print1.png" width="220"/> | Estrutura em camadas do projeto no VSCode (`domain`, `application`, `config`, `infrastructure`) |
| 2 | <img src="docs/screenshots/print2.png" width="220"/> | Aplicação iniciada com sucesso via `mvn spring-boot:run` |
| 3 | <img src="docs/screenshots/print3.png" width="220"/> | Pagamento aprovado — `201 Created`, status `CONFIRMADO` |
| 4 | <img src="docs/screenshots/print4.png" width="220"/> | Pagamento recusado por limite de valor — `422 Unprocessable Entity` |
| 5 | <img src="docs/screenshots/print5.png" width="220"/> | Pagamento recusado por cartão bloqueado — `422 Unprocessable Entity` |
| 6 | <img src="docs/screenshots/print6.png" width="220"/> | Suíte de testes do agregado executada via `mvn test` |
| 7 | <img src="docs/screenshots/print7.png" width="220"/> | Console H2 exibindo os registros persistidos na tabela `pagamentos` |
| 8 | <img src="docs/screenshots/print8.png" width="220"/> | Repositório publicado no GitHub |


## Tecnologias

- Java 24 (testado nessa versão; compila em qualquer JDK 21+ ajustando `java.version` no `pom.xml`)
- Spring Boot 4.1.0
- Spring Web, Spring Data JPA
- Maven
- H2 em memória

## Requisitos

- JDK 24 (ou ajuste `<java.version>` no `pom.xml` para a sua versão — rode `java -version` pra conferir)
- Maven 3.6.3 ou superior

## Estrutura do projeto

```
src/main/java/com/exemplo/paymentservice/
├── PaymentServiceApplication.java
├── domain/                        # Aggregate Root, Value Objects, porta do repositório
│   ├── Pagamento.java              (Aggregate Root)
│   ├── Valor.java                  (Value Object)
│   ├── NumeroCartao.java           (Value Object)
│   ├── PagamentoId.java / PedidoId.java
│   ├── StatusPagamento.java
│   ├── PagamentoRepository.java    (porta)
│   └── *Exception.java
├── application/
│   └── PagamentoApplicationService.java   # caso de uso, sem anotação Spring
├── config/
│   └── BeanConfig.java             # liga a application service ao container
└── infrastructure/
    ├── web/                        # controller REST + DTOs + exception handler
    └── persistence/                # entidade JPA + mapper + adaptador da porta

docs/exemplo-monolito/              # NÃO compila aqui — cole no repositório
                                     # do monólito (ver seção "Integração com o monólito")
```

O domínio (`domain/`) não importa nada de Spring nem de JPA — ele é testável isoladamente (ver `src/test`).

![Estrutura do projeto no VSCode](docs/screenshots/print1.png)
*Estrutura em camadas do domínio, isolando `domain`, `application`, `config` e `infrastructure`.*

## Como rodar

1. Clone o repositório e abra a pasta no VSCode (extensão **Extension Pack for Java** + **Spring Boot Extension Pack** recomendadas).
2. No terminal integrado do VSCode:

```bash
mvn spring-boot:run
```

3. A API sobe em:

```
http://localhost:8081
```

4. Console H2 (opcional, pra inspecionar os dados):

```
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:mem:payment
User Name: sa
Password: (em branco)
```

![Aplicação rodando](docs/screenshots/print2.png)
*Aplicação iniciada com sucesso na porta 8081.*

## Testando

Use o arquivo `requests.http` (extensão **REST Client** do VSCode — clique em "Send Request" acima de cada bloco) ou `curl`:

```bash
curl -X POST http://localhost:8081/pagamentos \
  -H "Content-Type: application/json" \
  -d '{
    "pedidoId": 1,
    "valor": 250.00,
    "numeroCartao": "4111111111111111"
  }'
```

![Pagamento aprovado](docs/screenshots/print3.png)
*Requisição de pagamento aprovada, com o agregado retornando status `CONFIRMADO`.*

Cenários de recusa, evidenciando as invariantes de negócio encapsuladas no agregado:

```bash
curl -X POST http://localhost:8081/pagamentos \
  -H "Content-Type: application/json" \
  -d '{"pedidoId": 2, "valor": 15000.00, "numeroCartao": "4111111111111111"}'
```

![Pagamento recusado por limite](docs/screenshots/print4.png)
*Requisição recusada por exceder o limite de R$ 10.000,00.*

```bash
curl -X POST http://localhost:8081/pagamentos \
  -H "Content-Type: application/json" \
  -d '{"pedidoId": 3, "valor": 100.00, "numeroCartao": "4111111111110000"}'
```

![Pagamento recusado por cartão bloqueado](docs/screenshots/print5.png)
*Requisição recusada por cartão bloqueado.*

Suíte de testes do agregado, executável de forma isolada, sem dependência de HTTP ou banco:

```bash
mvn test
```

![Testes passando](docs/screenshots/print6.png)
*Suíte de testes do agregado `Pagamento` executada com sucesso.*

## Regras de negócio (equivalentes às do monólito legado)

- Valor menor ou igual a zero: recusado (`Valor` não deixa instanciar).
- Valor acima de R$ 10.000,00: recusado por limite.
- Cartão terminado em `0000`: bloqueado.
- Demais cartões com valor válido: aprovados.

A diferença para o monólito é **onde** essas regras vivem: aqui elas estão dentro do Aggregate Root `Pagamento`, não espalhadas por um service — é impossível existir um `Pagamento` em memória que viole essas invariantes.

![Console H2 com dados persistidos](docs/screenshots/print7.png)
*Registros persistidos na tabela `pagamentos`. Requisições recusadas não geram persistência, já que a validação ocorre antes de qualquer chamada ao repositório.*

## Integração com o monólito (Strangler Fig / Branch by Abstraction)

A pasta `docs/exemplo-monolito/` contém os arquivos ilustrativos de como fica a mudança **do lado do monólito** `ecommerce-legado-ddd`, que hoje chama um `PagamentoProcessador` concreto direto no `PedidoService`:

1. `PagamentoGateway.java` — a porta que substitui a chamada direta ao processador concreto.
2. `PagamentoGatewayLegado.java` — implementação inicial, só delega pro código legado (zero risco).
3. `PagamentoGatewayRemoto.java` — implementação que chama este payment-service via HTTP, com tradução de DTO (Anti-Corruption Layer).
4. `PagamentoGatewayConfig.java` — feature flag (`pagamento.usar-novo-servico`) que decide qual implementação é usada, permitindo rollback instantâneo.

Esses arquivos **não fazem parte do build deste projeto** — copie o conteúdo deles para o repositório do monólito quando for aplicar a extração de verdade.

## Passo a passo para subir no GitHub

```bash
cd payment-service
git init
git add .
git commit -m "feat: extrai contexto de Pagamento como microsservico (DDD + Strangler Fig)"

# crie o repositório vazio no GitHub antes (via github.com/new), depois:
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/payment-service.git
git push -u origin main
```

![Repositório no GitHub](docs/screenshots/print8.png)
*Repositório publicado no GitHub.*

## TP2 — Eventos de Domínio

Como evolução da implementação desenvolvida no TP1, o agregado `Pagamento` foi estendido para trabalhar com **Eventos de Domínio**.

A implementação introduz o evento `PagamentoConfirmadoEvent`, registrado pelo agregado sempre que um pagamento é confirmado. Dessa forma, a ocorrência de uma mudança relevante no domínio é representada por um evento, mantendo essa responsabilidade encapsulada no próprio agregado.

### Novos componentes

* `DomainEvent.java` — interface que define a abstração dos eventos de domínio.
* `PagamentoConfirmadoEvent.java` — evento gerado quando um pagamento é confirmado.
* `Pagamento.java` — agregado responsável pelo registro dos eventos gerados durante suas operações.
* `PagamentoTest.java` — teste que valida o registro do evento após a confirmação do pagamento.

### Prints

| # | Preview                                                 | Descrição                                                                            |
| - | ------------------------------------------------------- | ------------------------------------------------------------------------------------ |
| 1 | <img src="docs/screenshots/print1tp2.png" width="220"/> | Interface `DomainEvent`, utilizada como abstração para os eventos de domínio         |
| 2 | <img src="docs/screenshots/print2tp2.png" width="220"/> | Implementação do evento `PagamentoConfirmadoEvent`                                   |
| 3 | <img src="docs/screenshots/print3tp2.png" width="220"/> | Agregado `Pagamento` registrando o evento durante a execução do método `confirmar()` |
| 4 | <img src="docs/screenshots/print4tp2.png" width="220"/> | Execução da suíte de testes com a validação do registro do evento de domínio         |
| 5 | <img src="docs/screenshots/print5tp2.png" width="220"/> | Arquitetura proposta para publicação dos eventos utilizando Outbox e Kafka           |

### 1. Interface de evento de domínio

A interface `DomainEvent` estabelece a abstração utilizada pelos eventos do domínio, permitindo que diferentes eventos possam seguir uma mesma estrutura sem criar dependência com mecanismos externos de infraestrutura.

![Interface DomainEvent](docs/screenshots/print1tp2.png)

*Interface `DomainEvent`, responsável pela abstração dos eventos de domínio.*

### 2. Evento `PagamentoConfirmadoEvent`

Foi implementado o `PagamentoConfirmadoEvent` para representar, de forma explícita, a confirmação de um pagamento dentro do domínio.

O evento contém as informações necessárias para identificar a operação realizada e pode posteriormente ser utilizado pela camada de infraestrutura para publicação ou processamento.

![PagamentoConfirmadoEvent](docs/screenshots/print2tp2.png)

*Implementação do `PagamentoConfirmadoEvent` como representação da confirmação de um pagamento.*

### 3. Registro do evento no agregado

O agregado `Pagamento` passou a manter internamente os eventos gerados durante suas operações.

No método `confirmar()`, após a validação das regras de negócio e alteração do estado do pagamento, o `PagamentoConfirmadoEvent` é adicionado à coleção de eventos do agregado.

![Registro do evento no agregado](docs/screenshots/print3tp2.png)

*Registro do `PagamentoConfirmadoEvent` durante a execução do método `confirmar()`.*

Os eventos podem ser consultados através de `getEventos()` e removidos com `limparEventos()`. Dessa forma, o agregado permanece responsável por registrar os eventos decorrentes das alterações realizadas no domínio.

### 4. Testes

Foi incluído um teste específico para validar o comportamento relacionado aos eventos de domínio.

O teste `deveRegistrarEventoDeDominioAoConfirmar()` verifica se, após a confirmação de um pagamento, o evento `PagamentoConfirmadoEvent` é corretamente registrado pelo agregado.

A suíte pode ser executada através do Maven:

```bash
mvn test
```

![Testes do TP2](docs/screenshots/print4tp2.png)

*Execução da suíte de testes com sucesso, incluindo o teste de registro do evento de domínio.*

### 5. Arquitetura de publicação dos eventos

A implementação dos eventos de domínio possibilita sua integração posterior com mecanismos de mensageria.

A arquitetura representada utiliza o padrão **Outbox** para garantir a persistência dos eventos e o **Apache Kafka** para sua distribuição aos demais componentes interessados.

![Arquitetura de publicação dos eventos](docs/screenshots/print5tp2.png)

*Arquitetura proposta para persistência e publicação dos eventos de domínio utilizando Outbox e Kafka.*

---

**Instituto Infnet**
Disciplina: Domain-Driven Design (DDD) e Arquitetura de Softwares Escaláveis com Java
Professor: Leonardo Silva da Gloria
Aluno: Lucas Amorim Porciuncula
