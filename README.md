# Framework de atendimentos
O objetivo desse projeto é ser usado por aplicações que 
realizam o agendamento de atendimentos, por exemplo: 
consulta médica, horário no salão, horário em um restaurante, entre outros.

### Pontos fixos
* Gerência de atendimentos
* Gerenciar contas dos usuários(CRUD)
* Controle e verificação de agenda do usuário que oferece o atendimento
* Usuário validador e usuário cliente 
  
O diagrama de classes dos pontos fixos pode ser consultado aqui: 
[diagrama](https://drive.google.com/file/d/1mRGbllfh7acFB5qEV-17uaJbUu-n29mU/view)

### Pontos flexíveis
* Usuário que oferece os atendimentos
* Mensagem de notificação para possível retorno
* Cálculo de vagas nos horários de atendimento

O diagrama de classes dos pontos flexíveis pode ser consultado aqui: 
[diagrama](https://drive.google.com/file/d/1FZ0ExZ484MJIgi66t7n1s5vtPfOuK5f1/view)

# Instâncias da aplicação

### Sistema de Saúde
### Salão de beleza
### Restaurante

# Tecnologias utilizadas

### Front-end
* HTML
* Javascript
* CSS
* Bootstrap
* Jquery
* Thymeleaf

## Back-end
* Java
* Spring
* PostgreSQL

# Como executar este projeto

Para executar localmente é necessário ter instalado na sua máquina: Java, PostgreSQL. Em seguida, clone o projeto através de:
```
git@github.com:karinamaria/PDSgrupo5.git
```
Abra o projeto em uma IDE de sua preferência. Logo, depois execute o projeto através do maven:
```
mvn spring-boot:run
```
Ou execute a classe:
```
PdSgrupo5Application.java
```
Abra seu navegador de preferência e coloque em: `localhost:8080`

# Desenvolvedoras

* Karina Maria [@karinamaria](https://github.com/karinamaria/)
* Maria Eduarda [@mariaeloi](https://github.com/mariaeloi/)
