# Guess - API

Aplicação que serve de bakcend para o jogo de cartas desenvolvido na faculdade na disciplina de ingles


## Tecnologias Utilizadas

- **Spring Boot 3**
- **Java 17**
- **Princípios SOLID**
- **Docker**

## Pré-requisitos

- **Java 17**: Certifique-se de ter o Java 17 instalado.
- **Maven**: Para construir e testar o projeto localmente.
- **Docker**: Para executar o projeto usando Docker Compose.

## Configuração

### 1. Clonar o Repositório

```bash
https://github.com/joaomn/guess.git
cd guessApi

```

### 1.1 Ou pode baixar apenas a imagem do docker hub

```bash
docker pull jhonyamn/guessapi:1.0

```

obs: veja no 

### 2. Configurar as Credenciais do Banco de Dados

```bash
spring.datasource.url=jdbc:mysql://seu_host:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

```

### 3. Executando a Aplicação

voce pode executar pelo docker compose utilizando o comando pelo terminal na pasta raiz do projeto

```bash
docker-compose up

```

ou pode rodar o arquivo aplication dentro do packge principal

### 4. Documentação dos Endpoints

os endpoints estão documentados no swagger

link do swagger : http://localhost:8080/api/swagger-ui/index.html#/
