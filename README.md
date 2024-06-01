# **Agregador de Investimentos**

## **Ferramentas Utilizadas para Desenvolvimento**

```
IntelliJ IDEA
DBeaver
Postman
Spring Boot   
Java
Docker
MySQL
```

## **Dependências**

O desenvolvimento de código em Java, em geral, usufrui de um significativo conjunto de bibliotecas e _frameworks_. Esta
reutilização é incorporada em um projeto por meio de dependências. Para gerenciar foi utilizado o _Maven_.

```
Spring Web MVC
Spring JPA
MySQL Driver
OpenFeign
```

## **Métodos**

Requisições para a API devem seguir os padrões:

| Método | Descrição |
|---|---|
| `GET` | Retorna informações de um ou mais registros. |
| `POST` | Utilizado para criar um novo registro. |

## **Respostas**

| Status | Descrição                                                          |
|--------|--------------------------------------------------------------------|
| `200`  | Requisição executada com sucesso (success).                        |
| `201`  | Requisição executada com sucesso (success).                        |
| `400`  | Erros de validação ou os campos informados não existem no sistema. |
| `409`  | Conflito.                                                          |
| `405`  | Método não implementado.                                           |

# **Recursos da API**

| Método     | Endpoint                                             |
|------------|---------------------------------------------------|
| `GET`      | /v1/users                                         |
| `GET`      | /v1/users/{userId}                                |
| `GET`      | /v1/users/{userId}/accounts                       |
| `GET`      | /v1/accounts/{accountId}/stocks                   |
| `POST`     | /v1/users/{userId}/accounts                       |
| `POST`     | /v1/stocks                                        |
| `POST`     | /v1/users                                         |
| `POST`     | /v1/accounts/{accountId}/stocks                   |
