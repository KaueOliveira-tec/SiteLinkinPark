# SiteLinkinPark - Linkin Park: De Fã para Fã

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green.svg)](https://spring.io/projects/spring-boot)

## Descrição

Site acadêmico desenvolvido para a disciplina **Desenvolvimento para Servidores II** do curso **Sistemas para Internet** da **Fatec Rubens Lara**. 

O projeto é um site de fãs para a banda **Linkin Park**, apresentando informações sobre a história da banda, integrantes originais e atuais, músicas/álbuns (em desenvolvimento) e um formulário de cadastro de usuários.


## Funcionalidades
- Página inicial com biografia da banda Linkin Park.
- Páginas de integrantes originais e atuais.
- Página de músicas e álbuns (placeholder para listas personalizadas).
- Formulário de cadastro de usuário (salva via serviço JPA).
- Navegação responsiva com assets estáticos (CSS, JS, imagens).
- Aba de perfil onde mostra os dados do usuário. Obs.: por enquanto tem que colocar o uuid na url para obter as informações.

## INSERTs de teste
Nome: Teste1 / E-mail: meuemail_legal@email.com / Senha: 1234
Nome: Teste2 / E-mail: algumemail_bacana@email.com / Senha: asdag efadf


## UUIDs utilizados para teste 
| ID                              |  Retorno
| ac31f154-3602-4416-96c6-c7efd06091cb | Nome: Teste1 / E-mail: meuemail_legal@email.com |
| 23747bb0-58bb-459a-957e-7a54647a604f | Nome: Teste2 / E-mail: algumemail_bacana@email.com |


## Tech Stack
- **Backend**: Spring Boot 4.0.3 (WebMVC, Data JDBC, JPA/Hibernate).
- **Frontend**: Thymeleaf templates, CSS, JavaScript.
- **Database**: PostgreSQL (JDBC driver).
- **Build**: Maven (wrapper: mvnw).
- **Java**: 25.

## Pré-requisitos
- **Java 25** instalado.
- **PostgreSQL** rodando em `localhost:5432` com:
  - Database: `nome que você deu ao seu banco de dados dentro do PostgreSQL`
  - Usuário: `postgres`
  - Senha: `senha que você usou para conectar o servidor do banco de dados dentro do PostgreSQL`
- (O schema é criado automaticamente via `schema-postgresql.sql` e Hibernate `create-drop`).

## Instalação e Execução
1. Clone o repositório:
   ```
   git clone <repo-url>
   cd nomeDoBancoDeDados
   ```
2. Inicie o PostgreSQL e crie o banco.
3. Execute o projeto
4. Acesse em: [http://localhost:8080](http://localhost:8080)

## Estrutura do Projeto
```
ProjetoSite/
├── pom.xml
├── src/
   ├── main/
   │   ├── java/com/example/SiteLinkinPark/
   │   │   ├── controller/MenuController.java
   │   │   └── model/(Usuario.java, UsuarioService.java)
   │   └── resources/
   │       ├── application.yaml
   │       ├── schema-postgresql.sql
   │       ├── static/(css/, img/, js/)
   │       └── templates/(index.html, musicas.html, form_user.html, etc.)
   └── test/
```

## Rotas Principais
| Rota                  | Descrição              |
|-----------------------|------------------------|
| `/`                   | Página inicial (biografia) |
| `/musicas`            | Músicas e álbuns       |
| `/integrantes_originais` | Integrantes originais |
| `/integrantes_atuais` | Integrantes atuais     |
| `/form_user` (GET)    | Formulário de cadastro |
| `/usuario` (POST)     | Processa cadastro      |
| `/perfil` (GET)     | Mostra os dados do usuário    |

## Autor
**Kauê de Oliveira Martins**  
Projeto acadêmico - Fatec Rubens Lara.

## Próximos Passos
- Implementar listas de músicas personalizadas por usuário.
- CRUD completo para usuários/músicas.
- Deploy.
- Autenticação/segurança.
- Melhorias visuais e testes.
- Fazer uma aba de login.
- Fazer um menu para cadastro, perfil e login.

## Licença
Projeto acadêmico - sem licença comercial.