# Diagnóstico do Projeto SiteLinkinPark

Após uma análise aprofundada do código-fonte do repositório `KaueOliveira-tec/SiteLinkinPark`, identifiquei as causas raiz de dois problemas críticos: o não carregamento das imagens e as falhas nos fluxos de cadastro e login.

Abaixo, detalho cada um dos problemas, suas causas técnicas e proponho as correções necessárias.

## 1. Problema de Carregamento de Imagens

**Sintoma:**
As imagens não estão sendo exibidas nas páginas do site, incluindo a logo no cabeçalho e as imagens nas páginas de conteúdo (como na página inicial e nas páginas de integrantes).

**Causa Técnica:**
O problema reside em uma incompatibilidade entre as rotas configuradas no Spring Security para permitir acesso público e os caminhos reais onde as imagens estão sendo servidas pela aplicação e referenciadas nos templates HTML.

No arquivo `SecurityConfig.java`, as regras de autorização estão definidas da seguinte forma:
```java
http
    .authorizeHttpRequests(authz -> authz
        .requestMatchers("/", "/index", "/sobre", "/css/**", "/js/**", "/images/**").permitAll()
        // ... outras regras
```

Observe que o Spring Security está configurado para liberar o acesso não autenticado a recursos sob o caminho `/images/**`.

No entanto, nos templates Thymeleaf (como `index.html`, `integrantes_originais.html`, etc.), as imagens estão sendo referenciadas utilizando o caminho `/img/`:
```html
<img th:src="@{/img/logo-hybrid-theory.png}" class="header__logo" alt="Logo...">
<img th:src="@{/img/elenco-atual-lp.jpg}" class="img-elenco-home" alt="Imagem...">
```

E fisicamente, as imagens estão localizadas no diretório `src/main/resources/static/img/`.

**Consequência:**
Como o caminho `/img/**` não está explicitamente liberado (`permitAll()`) no `SecurityConfig`, o Spring Security intercepta as requisições do navegador para buscar as imagens e exige autenticação. Como o usuário não está logado, o servidor retorna um erro de acesso negado (geralmente redirecionando para a página de login ou retornando 403 Forbidden), e o navegador não consegue carregar a imagem.

**Solução:**
No arquivo `src/main/java/com/example/SiteLinkinPark/config/SecurityConfig.java`, altere a regra `.requestMatchers` para incluir o diretório correto das imagens:

De:
`.requestMatchers("/", "/index", "/sobre", "/css/**", "/js/**", "/images/**").permitAll()`

Para:
`.requestMatchers("/", "/index", "/sobre", "/css/**", "/js/**", "/img/**").permitAll()`

---

## 2. Problemas no Cadastro e Login

**Sintoma:**
O usuário não consegue realizar o cadastro de novas contas nem efetuar o login no sistema.

**Causas Técnicas:**
O problema de autenticação é multifatorial e decorre de uma mistura arquitetural conflitante: a aplicação tenta implementar a segurança oficial do Spring Security, mas simultaneamente mantém um sistema de login e sessão manual (customizado) que não se comunicam adequadamente, além de possuir regras de bloqueio de rotas incorretas.

### A. Bloqueio das Rotas Públicas de Cadastro e Login
No arquivo `SecurityConfig.java`, as rotas responsáveis pelo cadastro de usuários não foram liberadas para acesso público.

O controller `UsuarioController.java` define as seguintes rotas para o fluxo de cadastro:
- `GET /form_user` (exibe o formulário)
- `POST /usuario` (processa o cadastro)

E para o login customizado:
- `POST /efetuarLogin` (processa a autenticação manual)

Nenhuma destas rotas está listada no `.permitAll()` do `SecurityConfig`. Consequentemente, a regra `.anyRequest().authenticated()` entra em ação, bloqueando o acesso a qualquer usuário que tente se cadastrar ou processar o login.

### B. Conflito entre Spring Security e Login Customizado (Sessão Manual)
A aplicação possui um conflito grave na forma como gerencia a autenticação.

1. **Spring Security vs. Login Customizado:**
   O `SecurityConfig` define um formulário de login padrão:
   ```java
   .formLogin(form -> form
       .loginPage("/login")
       .permitAll()
       .defaultSuccessUrl("/", true)
   )
   ```
   Isso instrui o Spring Security a interceptar o `POST /login` e autenticar usando o `CustomUserDetailsService`.

   Porém, o formulário HTML (`login.html`) não envia os dados para `/login`. Ele faz um `POST` para `@{/efetuarLogin}`:
   ```html
   <form class="form-content" th:action="@{/efetuarLogin}" method="post">
   ```
   Esse endpoint é tratado manualmente no `UsuarioController`, que verifica o banco e salva o usuário logado na `HttpSession` padrão do Java (`session.setAttribute("usuarioLogado", user)`).

2. **Dependência Exclusiva da Sessão Manual:**
   As páginas protegidas (como `perfil.html`, `musicas.html` e os controllers de Playlist) dependem exclusivamente do atributo `usuarioLogado` da sessão manual. Eles não utilizam o contexto de segurança oficial do Spring Security (`SecurityContextHolder`).
   Se um usuário conseguisse passar pelo Spring Security, a aplicação continuaria considerando-o deslogado porque o objeto `usuarioLogado` não estaria na sessão.

### C. Inconsistência no Hash de Senhas
Existe uma grave inconsistência na forma como as senhas são armazenadas e verificadas.

1. **Cadastro Manual (Sem Hash):**
   O `UsuarioDAO.java` salva a senha em texto puro no banco de dados durante o cadastro:
   ```java
   public void cadastroUsuario(Usuario usuario) {
       // ...
       obj[3] = usuario.getSenha(); // Senha em texto puro
       jdbc.update(sql, obj);
   }
   ```
   A verificação no login manual (`/efetuarLogin`) também compara texto puro:
   ```java
   String sql = "SELECT * FROM usuario WHERE LOWER(email) = LOWER(?) AND senha = ?";
   ```

2. **Spring Security (Com Hash):**
   No entanto, o `SecurityConfig` define um `PasswordEncoder` BCrypt.
   O `AdminBootstrap.java` cria o usuário administrador usando BCrypt:
   ```java
   Usuario admin = new Usuario("Administrador", "admin@linkinpark.com", passwordEncoder.encode("admin123"));
   ```
   O `CustomUserDetailsService` retorna o usuário para o Spring Security validar. Como o Spring Security está configurado com BCrypt, ele tentará verificar a senha fornecida pelo usuário contra o hash no banco.
   - Para o admin (que tem hash), funcionaria (se usasse o fluxo do Spring Security).
   - Para usuários normais (cadastrados com texto puro), o Spring Security falhará sempre, pois tentará comparar um hash BCrypt com uma senha em texto puro.

**Soluções Recomendadas:**

Para resolver os problemas de login e cadastro, a arquitetura de autenticação precisa ser unificada. Recomendo adotar o **Spring Security como única fonte de verdade**, eliminando o controle manual de sessão.

1. **Liberar as rotas públicas:**
   Atualize o `SecurityConfig.java` para permitir o acesso ao cadastro e ao processamento de usuários:
   ```java
   .requestMatchers("/", "/index", "/sobre", "/css/**", "/js/**", "/img/**", "/form_user", "/usuario", "/form_sucesso").permitAll()
   ```

2. **Padronizar o Hash de Senhas (BCrypt):**
   Modifique o `UsuarioDAO.java` para não realizar a inserção manual com texto puro. O ideal é que o `UsuarioService.java` encripte a senha antes de enviar para o DAO:
   ```java
   @Autowired
   private PasswordEncoder passwordEncoder;

   public void cadastroUsuario(Usuario usuario){
       usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
       usuarioDAO.cadastroUsuario(usuario);
   }
   ```
   *(Nota: Será necessário resetar as senhas antigas no banco ou recriar o banco, pois as senhas antigas em texto puro não funcionarão com BCrypt).*

3. **Unificar o Fluxo de Login:**
   - No `login.html`, altere a action do formulário para o padrão do Spring Security:
     `th:action="@{/login}"`
   - Remova o endpoint `/efetuarLogin` do `UsuarioController.java`, deixando o Spring Security cuidar da autenticação automaticamente.
   - Altere os controllers (como `PlaylistController`) e os templates (como `perfil.html`) para verificar a autenticação através do Spring Security (ex: usando `Principal principal` nos controllers e `sec:authorize` no Thymeleaf) em vez de checar `session.getAttribute("usuarioLogado")`.

## Resumo
- **Imagens:** Altere `/images/**` para `/img/**` no `SecurityConfig.java`.
- **Cadastro:** Adicione `/form_user` e `/usuario` no `.permitAll()` do `SecurityConfig.java`.
- **Login:** Padronize o sistema para usar apenas o Spring Security com senhas hasheadas (BCrypt), ajustando a action do form de login para `/login` e removendo a gestão manual de `HttpSession`.
