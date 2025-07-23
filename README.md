## 📚 Literalura
Aplicação Java com Spring Boot que consome dados da API do Projeto Gutenberg e armazena informações de livros e autores
em um banco de dados relacional.

## 🔧 Tecnologias
- Java 21

- Spring Boot

- JPA / Hibernate

- PostgreSQL

- API REST (Gutenberg)

- Jackson (serialização JSON)

## ⚙️ Funcionalidades
- Buscar livros por nome via API

- Salvar livros e autores no banco de dados

- Listar livros salvos

- Listar autores registrados

- Listar autores vivos em um determinado ano

- Listar livros em um determinado idioma

- Listar Top 10 de livros mais baixados

## 🧪 Exemplo de uso
````
Digite o nome de um livro:
> Hamlet

---- LIVRO ----
Título: Hamlet
Autor: William Shakespeare
Idioma: en
Número de Downloads: 12345
------------
````


## Como executar
1- Certifique-se de ter o PostgreSQL rodando localmente

2- Configure as variáveis na IDE ou no terminal

Essas são as variáveis que precisam ser configuradas
````
DB_HOST=localhost
DB_USERNAME=seu_usuario
DB_PASS=sua_senha
````
