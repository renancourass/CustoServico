# Sistema de Custo Mensal de Serviço
## Java 17 + Spring Boot 3.2 + H2 Database + Thymeleaf

---

## 📋 PRÉ-REQUISITOS

- Java 17+ instalado (`java -version`)
- Maven 3.8+ instalado (`mvn -version`) **OU** usar o wrapper `./mvnw`
- Navegador web moderno

---

## 🚀 COMO EXECUTAR

### Opção 1 – Maven instalado na máquina
```bash
cd custo-servico
mvn spring-boot:run
```

### Opção 2 – Maven Wrapper (baixa automaticamente)
```bash
cd custo-servico
chmod +x mvnw
./mvnw spring-boot:run
```

### Opção 3 – Gerar JAR e executar
```bash
cd custo-servico
mvn clean package -DskipTests
java -jar target/custo-servico-1.0.0.jar
```

### Opção 4 – IntelliJ IDEA / Eclipse / VS Code
1. Importe como projeto Maven
2. Execute a classe `CustoServicoApplication.java`

---

## 🌐 ACESSAR O SISTEMA

Após iniciar, abra no navegador:
```
http://localhost:8080
```

---

## 👤 USUÁRIOS PADRÃO

| Perfil         | Username  | Senha     | Permissões                              |
|---------------|-----------|-----------|-----------------------------------------|
| Super Admin   | admin     | admin123  | Tudo: CRUD, Usuários, exclusão          |
| Usuário Final | usuario   | user123   | Criar/editar próprios registros, visualizar |

---

## 🗄️ BANCO DE DADOS

O sistema usa **H2** (banco embutido – zero configuração).

- Os dados são salvos em `./data/custoservico.mv.db` (mesmo diretório onde você executa)
- Para acessar o console H2: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:./data/custoservico`
  - User: `sa` | Password: `password`

### Migrar para PostgreSQL (produção)
Adicione no `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```
E em `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/custoservico
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📁 ESTRUTURA DO PROJETO

```
custo-servico/
├── pom.xml                                # Dependências Maven
├── src/main/
│   ├── java/com/custoservico/
│   │   ├── CustoServicoApplication.java   # Ponto de entrada
│   │   ├── model/
│   │   │   ├── Usuario.java               # Entidade usuário
│   │   │   └── CustoServico.java          # Entidade principal + cálculos
│   │   ├── repository/                    # Spring Data JPA
│   │   ├── service/
│   │   │   ├── UsuarioService.java
│   │   │   ├── CustoServicoService.java
│   │   │   └── DataInitializer.java       # Cria dados iniciais
│   │   ├── controller/
│   │   │   ├── LoginController.java
│   │   │   ├── DashboardController.java
│   │   │   ├── CustoServicoController.java
│   │   │   └── AdminController.java
│   │   └── security/
│   │       ├── SecurityConfig.java
│   │       └── CustomUserDetailsService.java
│   └── resources/
│       ├── application.properties
│       ├── templates/                     # Thymeleaf HTML
│       │   ├── login.html
│       │   ├── dashboard.html
│       │   ├── custo-form.html            # Formulário de entrada
│       │   ├── custo-visualizar.html      # Planilha visual
│       │   └── admin/
│       │       ├── usuarios.html
│       │       └── usuario-form.html
│       └── static/
│           ├── css/style.css              # Layout completo
│           └── js/calc.js                 # Cálculos em tempo real
```

---

## ⚙️ FUNCIONALIDADES

### Módulos do Custo (conforme planilha):
- **I – Remuneração**: Mensal + Gratificação → Subtotal I
- **II – Encargos Sociais**: % sobre Subtotal I → Subtotal II
- **III – Insumos**: Fardamento, Vale Transporte (-6% rem.), Alimentação, Plano de Saúde, Seguro de Vida → Subtotal III
- **IV – Bonificação**: % Despesas Adm. + % Lucro sobre (I+II+III) → Subtotal IV
- **V – Tributos sobre Faturamento**: IRPJ, CSLL, PIS, COFINS, ISS → calculados sobre preço final
- **VI – Preço Mensal**: Base / (1 - %tributos) — método de cálculo de preço de venda sobre tributos em cascata

### Sistema:
- ✅ Login com Spring Security (BCrypt)
- ✅ Dois perfis: SUPER_ADMIN e USUARIO
- ✅ Cálculo em tempo real no formulário (JavaScript)
- ✅ Visualização tipo planilha fiel ao original
- ✅ Dashboard com resumo
- ✅ CRUD completo de custos
- ✅ Gerenciamento de usuários (só admin)
- ✅ Banco de dados persistente H2
- ✅ Sidebar responsiva
- ✅ Design moderno com Inter + FontAwesome

---

## 🔒 SEGURANÇA

| Rota             | Acesso          |
|-----------------|-----------------|
| `/login`        | Público          |
| `/dashboard`    | Autenticado      |
| `/custos/**`    | Autenticado      |
| `/admin/**`     | SUPER_ADMIN only |
| `/h2-console`   | Local only       |

---

*Gerado por Claude – Anthropic*
"# CustoServico" 
