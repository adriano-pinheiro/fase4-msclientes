# Tech Challenge - Fase 4 

### Projeto: Sistema de Gerenciamento de Pedidos

### Microsserviço de Clientes - Porta 8081

<br>

### Funções Principais:

1. Cadastrar Clientes
2. Listar Clientes
3. Atualizar informações dos Clientes
4. Excluir Clientes

### Ferramentas e Tecnologias:

1. Linguagens de Programação: Java.
2. Banco de dados: MySQL
3. Ferramentas de API: Swagger, Postman, etc.
5. Controle de Versão: Git
6. Docker

### API Endpoints:

1. POST `/api/v1/clientes` - para adicionar um novo cliente
2. GET `/api/v1/clientes` - para listar todos os clientes
3. GET `/api/v1/clientes/{clienteId}` - para ver detalhes de um cliente
4. PUT `/api/v1/clientes/{clienteId}` - para atualizar um cliente
5. DELETE `/api/v1/clientes/{clienteId}` - para excluir um cliente

### Arquivo para importação da collection no Postman com os payloads das requisições:

1. externalfiles/fase4.postman_collection.json

### Instrução para a subida do container docker:

1. docker-compose -p msclientes-mysql up -d
