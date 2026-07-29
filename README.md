# 📩 Notification Service

Microsserviço em **Spring Boot** responsável por consumir eventos de criação de usuário via **RabbitMQ** e enviar um **e-mail de boas-vindas** automaticamente, de forma assíncrona.

---

## 🛠️ Tecnologias Utilizadas

<p align="center">
  <img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white" alt="RabbitMQ"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/Gmail_SMTP-EA4335?style=for-the-badge&logo=gmail&logoColor=white" alt="Gmail SMTP"/>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger / OpenAPI"/>
  <img src="https://img.shields.io/badge/Lombok-CC0000?style=for-the-badge&logo=lombok&logoColor=white" alt="Lombok"/>
</p>

| Tecnologia | Função no projeto |
|---|---|
| **Java 21** | Linguagem principal da aplicação |
| **Spring Boot** | Framework base (Web, AMQP, Mail) |
| **RabbitMQ** | Fila de mensageria para desacoplar o cadastro do envio de e-mail |
| **Spring Mail (SMTP)** | Envio real de e-mails via Gmail |
| **Docker / Docker Compose** | Sobe o RabbitMQ localmente sem precisar instalar nada manualmente |
| **Maven** | Gerenciador de dependências e build |
| **Springdoc OpenAPI (Swagger)** | Documentação e teste interativo dos endpoints |
| **Lombok** | Reduz código repetitivo (getters/setters/construtores) |
| **spring-dotenv** | Carrega variáveis do arquivo `.env` para o Spring |

---

## 🏗️ Arquitetura / Fluxo da Aplicação

<img width="620" height="481" alt="Captura de tela 2026-07-28 222634" src="https://github.com/user-attachments/assets/3ef0ef86-d8db-47c5-92ae-3c2a45dfbfd3" />

1. O cliente faz uma requisição `POST /usuarios` com nome e e-mail.
2. O `UsuarioController` publica um evento `UsuarioCriadoEvent` na fila `email.boasvindas` do RabbitMQ.
3. O `EmailConsumer` escuta essa fila (`@RabbitListener`) e, ao receber o evento, monta e envia o e-mail de boas-vindas.
4. O envio é feito via SMTP (Gmail), usando autenticação com **senha de app**.
5. O usuário recebe o e-mail de boas-vindas na caixa de entrada.

---

## ⚙️ Configuração do Ambiente (.env)

Crie um arquivo `.env` na raiz do projeto (mesma pasta do `pom.xml`):

```env
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASS=guest

MAIL_USERNAME=seuemail@gmail.com
MAIL_PASSWORD=suasenhadeapp16digitos
```


### Como gerar a senha de app do Gmail

1. Ative a **verificação em duas etapas**: `https://myaccount.google.com/security`
2. Gere a senha de app em: `https://myaccount.google.com/apppasswords`
3. Copie a senha de 16 caracteres **sem espaços** e use no `MAIL_PASSWORD`.

> 💡 Se estiver rodando pela IDE (IntelliJ/Eclipse) e o `.env` não for reconhecido, configure as variáveis diretamente na *Run Configuration* da IDE (`Run` → `Edit Configurations` → `Environment variables`).

---

## 🚀 Como Rodar o Projeto — Passo a Passo Completo

### 1. Clonar o repositório
```bash
git clone <url-do-repositorio>
cd notification-service
```

### 2. Configurar o `.env`
Siga a seção [Configuração do Ambiente](#️-configuração-do-ambiente-env) acima.

### 3. Subir o RabbitMQ com Docker
```bash
docker compose up -d
```
Confirme que subiu:
```bash
docker ps
```
Painel de administração do RabbitMQ: `http://localhost:15672` (login `guest` / `guest`)

### 4. Rodar a aplicação Spring Boot
```bash
./mvnw spring-boot:run
```
A aplicação sobe por padrão em `http://localhost:8080`.

### 5. Testar via Swagger
Acesse:
```
http://localhost:8080/swagger-ui.html
```
Encontre o endpoint `POST /usuarios` e envie um corpo de teste:
```json
{
  "nome": "Seu Nome",
  "email": "destinatario@exemplo.com"
}
```

### 6. Ou testar via `curl`
```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Seu Nome","email":"destinatario@exemplo.com"}'
```

### 7. Verificar o fluxo completo
- A API deve responder `201 Created`.
- No log da aplicação deve aparecer:
  ```
  Mensagem recebida da fila! Enviado e-mail real para : ...
  E-mail REAL enviado com sucesso para: ...
  ```
- O destinatário deve receber o e-mail de boas-vindas (verifique também a caixa de **spam**).

---


---

## 🧩 Estrutura do Projeto

```
notification-service/
├── docker-compose.yml
├── pom.xml
├── .env                        # variáveis de ambiente (não versionado)
└── src/main/java/br/notification_service/
    ├── NotificationServiceApplication.java
    ├── config/
    │   └── RabbitMQConfig.java     # fila + conversor de mensagens JSON
    ├── consumer/
    │   └── EmailConsumer.java      # escuta a fila e envia o e-mail
    ├── controller/
    │   └── UsuarioController.java  # endpoint POST /usuarios
    └── dto/
        ├── UsuarioRequest.java
        └── UsuarioCriadoEvent.java
```

---
## 📸 Resultados
<img width="697" height="145" alt="Captura de tela 2026-07-28 210056" src="https://github.com/user-attachments/assets/9c1127f3-5cf8-42cc-817e-8c59b7fdc4e6" />
<img width="1131" height="132" alt="Captura de tela 2026-07-28 195145" src="https://github.com/user-attachments/assets/38ff5585-83c0-474c-85c3-362d13229706" />
<img width="433" height="228" alt="Captura de tela 2026-07-28 205315" src="https://github.com/user-attachments/assets/9483cdea-fd9f-45a4-8bd8-202258113942" />






