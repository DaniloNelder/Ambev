<h2>API REST Spring Boot</h2>

Para executar o projeto, utilize o terminal e digite os seguintes comandos:

```
docker-compose up -d
mvn spring-boot:run 
```

### Endereço do projeto:
```
http://localhost:8081/
```

### Pré-requisitos:
Antes de iniciar, assegure-se de ter os seguintes pré-requisitos instalados em seu ambiente:
* Java 11 ou versões superiores.
* Maven 3.8.1 ou versões superiores.
* Docker 3 ou versões superiores.
* Uma IDE exemplo Eclipse ou qualquer outra de sua escolha.

### Banco de dados:
Para testes locais, utilizei o H2 como banco de dados em memória. No seu arquivo pom.xml, certifique-se de que o H2 esteja incluído como uma dependência.

```
http://localhost:8081/h2-console  
```
* Nome do banco de dados: ambev
* Usuario: sa
* Senha:
* Adicionar na URL o nome do banco desta forma : jdbc:h2:mem:ambev

### Documetação: 
Para consultar a documentação da API, adicionei o Swagger ao projeto. Acesse a interface do Swagger através do link abaixo:

```
http://localhost:8081/swagger-ui/index.html
```

### FilaMQ:
Para consultar fila da API.

```
http://localhost:15672/#/
```

### Redis
Para consultar o redis. 
```
http://localhost:6379
```

### Endpoint Disponíveis:
1. Listar Todos os Pedidos:
* GET http://localhost:8081/api/pedidos 

2. Receber um Novo Pedido:
* POST http://localhost:8081/api/pedidos/receber

#Exemplo de Corpo da Requisição:
```
{
    "produtos": [
        {
            "descricao": "Produto 1",
            "precoUnitario": 10.00,
            "quantidade": 2
        },
        {
            "descricao": "Produto 2",
            "precoUnitario": 15.50,
            "quantidade": 1
        }
    ],
    "idExterno": "Pedido 1"
}
```


