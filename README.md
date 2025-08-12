# 🚀 Sales System

Este projeto foi desenvolvido durante o curso da **EBAC** como um sistema de vendas monolítico em **Java**, utilizando **Jakarta Faces (JSF)** e **PrimeFaces** para a camada de interface.  
Ele integra de forma completa o **cadastro e gerenciamento de clientes, produtos e vendas**, proporcionando uma aplicação funcional para controle comercial.  
Na persistência, conta com **Hibernate** e **PostgreSQL**, garantindo robustez e confiabilidade no armazenamento de dados, enquanto o **Maven** é utilizado para o gerenciamento de dependências e build do projeto.

## 🛠️ Tecnologias Utilizadas

- ☕ [Java](https://www.java.com/) - Linguagem de programação principal do projeto
- 📦 [Maven](https://maven.apache.org/) - Gerenciamento de dependências e build
- 🧪 [JUnit](https://junit.org/) - Framework para testes unitários em Java
- 🌐 [Jakarta Faces](https://jakarta.ee/specifications/faces/) - Framework para construção de interfaces web em Java
- 🎨 [PrimeFaces](https://www.primefaces.org/) - Componentes ricos para aplicações JSF
- 🛠️ [Hibernate](https://hibernate.org/) - Mapeamento objeto-relacional (ORM)
- 🐘 [PostgreSQL](https://www.postgresql.org/) - Banco de dados relacional robusto e escalável

## 📦 Instalação e Execução

Para rodar o projeto localmente, siga os passos abaixo:

```sh
# Clone este repositório
git clone https://github.com/CauaMotta/sales-system

# Acesse a pasta do projeto
cd sales-system

# Compile o projeto com Maven
mvn clean install

# Execute o servidor de aplicação (Neste caso Wildfly)
# Certifique-se de configurar o banco de dados PostgreSQL e as credenciais no persistence.xml
