# SiteLinkinPark - Linkin Park: De Fã para Fã

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-green.svg)](https://spring.io/projects/spring-boot)

## Descrição

Site acadêmico desenvolvido para a disciplina **Desenvolvimento para Servidores II** do curso **Sistemas para Internet** da **Fatec Rubens Lara**. 

O projeto é um site de fãs para a banda **Linkin Park**, apresentando informações sobre a história da banda, integrantes originais e atuais, biblioteca de músicas/álbuns, sistema de autenticação de usuários e playlists personalizadas.


## Funcionalidades
- Página inicial com biografia da banda Linkin Park.
- Páginas de integrantes originais e atuais.
- **Página de músicas e álbuns** com filtragem por álbum.
- **Sistema de autenticação** com login e logout.
- **Gerenciamento de conta**: cadastro, visualização de perfil, edição de dados e exclusão de conta.
- **Playlists personalizadas**: criar, editar, visualizar e deletar playlists com seleção de múltiplas músicas.
- **Sessão de usuário**: mantém dados do usuário logado na sessão.
- Navegação responsiva com assets estáticos (CSS, JS, imagens).

## INSERTs de teste

Usuários de teste podem ser criados através do formulário de cadastro em `/form_user`.


## Tech Stack
- **Backend**: Spring Boot 4.0.4 (WebMVC, Data JDBC, JPA/Hibernate, Session JDBC, Validation, Security).
- **Frontend**: Thymeleaf templates, CSS, JavaScript.
- **Database**: PostgreSQL (JDBC driver).
- **Build**: Maven (wrapper: mvnw).
- **Java**: 21.

## Pré-requisitos
- **Java 21** ou superior instalado.
- **PostgreSQL** rodando em `localhost:5432` com:
  - Database: `nome que você deu ao seu banco de dados dentro do PostgreSQL`
  - Usuário: `postgres`
  - Senha: `senha que você usou para conectar o servidor do banco de dados dentro do PostgreSQL`
- (O schema é criado automaticamente via `schema-postgresql.sql` e Hibernate).

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
SiteLinkinPark/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/SiteLinkinPark/
│   │   │   ├── controller/
│   │   │   │   ├── MenuController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── MusicaController.java
│   │   │   │   └── PlaylistController.java
│   │   │   ├── model/
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── UsuarioDAO.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   ├── Musica.java
│   │   │   │   ├── MusicaDAO.java
│   │   │   │   ├── MusicaService.java
│   │   │   │   ├── Playlist.java
│   │   │   │   ├── PlaylistDAO.java
│   │   │   │   └── PlaylistService.java
│   │   │   └── config/
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── schema-postgresql.sql
│   │       ├── static/(css/, img/, js/)
│   │       └── templates/(html templates)
│   └── test/
```

## Rotas Principais
| Rota                  | Método | Descrição              |
|-----------------------|--------|------------------------|
| `/`                   | GET    | Página inicial (biografia) |
| `/musicas`            | GET    | Músicas e álbuns       |
| `/integrantes_originais` | GET | Integrantes originais |
| `/integrantes_atuais` | GET    | Integrantes atuais     |
| `/form_user`          | GET    | Formulário de cadastro |
| `/usuario`            | POST   | Processa cadastro      |
| `/login`              | GET    | Página de login        |
| `/efetuarLogin`       | POST   | Autentica usuário      |
| `/perfil`             | GET    | Perfil do usuário logado |
| `/editar_usuario`     | GET    | Formulário de edição   |
| `/usuario/atualizar`  | POST   | Atualiza dados do usuário |
| `/usuario/excluir`    | POST   | Exclui conta do usuário |
| `/logout`             | GET    | Encerra sessão         |
| `/musicas`            | POST   | Cria playlist          |
| `/playlists`          | GET    | Lista playlists do usuário |
| `/playlist/{id}`      | GET    | Detalhes da playlist   |
| `/playlist/{id}/atualizar` | POST | Atualiza playlist |
| `/playlist/{id}/musica/{musicaId}/remover` | POST | Remove música da playlist |
| `/playlist/{id}/deletar` | POST | Deleta playlist |

## Autor
**Kauê de Oliveira Martins**  
Projeto acadêmico - Fatec Rubens Lara.

## Próximos Passos
- Melhorias na interface de usuário (UI/UX refinement).
- Adicionar funcionalidades de busca e recomendação de músicas.
- Implementar compartilhamento de playlists entre usuários.
- Adicionar sistema de avaliação/rating de músicas.
- Testes automatizados (unit e integração).
- Melhorias de segurança (senhas hash, proteção contra ataques).
- Deploy em ambiente de produção.

## Licença
Projeto acadêmico - sem licença comercial.