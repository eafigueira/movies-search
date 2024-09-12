
# Movies Search Application

Este projeto é uma aplicação de busca em arquivos de filmes, onde o usuário pode procurar por uma sentença ou conjunto de palavras e obter os arquivos que contêm esses termos. A aplicação utiliza Java 21 e é executada via linha de comando.

# Requisitos

- **Java 21**: A aplicação utiliza funcionalidades disponíveis a partir do Java 21. Verifique se você tem o JDK 21 instalado.
- **Maven**: O projeto utiliza o Maven para gerenciamento de dependências e build.

## Configuração do Ambiente

1. **Instale o JDK 21**:
    - Baixe e instale o JDK 21 de [https://jdk.java.net/21/](https://jdk.java.net/21/).
    - Verifique a instalação do Java com o comando:
      ```bash
      java -version
      ```
    - A saída deve ser algo como:
      ```bash
      openjdk version "21"
      ```
2. **Clone o repositório**:
    - Clone o projeto.
    ```bash
    git clone https://github.com/eafigueira/movies-search.git
    cd movies-search
    ```
3. **Compile o projeto**:
  ```bash
  mvn package
  ```
4. **Como Executar**:
  ````bash
  java -jar target/movies-search.jar "caminho/para/diretorio" "termo de busca"
  ````
#### Parametros:
    O primeiro parâmetro é o diretório que contém os arquivos de texto a serem processados.
    O segundo parâmetro é o termo de busca (pode ser uma ou mais palavras).
*Exemplo de execução:*
  ````bash
  java -jar target/movies-search.jar "/home/user/movies/" "walt disney"
  ````
#### Saída esperada:
A aplicação exibirá:

- A quantidade de arquivos que contém o termo.
- Os arquivos encontrados, listados em ordem alfabética.
- O tempo total de execução da busca.

5. **Como rodar os testes**:

   Os testes unitários foram implementados utilizando o framework JUnit 5 e Mockito. Para rodar os testes, execute:

````bash
mvn test
````
6. **Estrutura do projeto:**
- **Main.java**: A classe principal que contém a lógica de construção do índice e busca.
- **Service.java**: Classe auxiliar responsável por serviços como criação do executor e listagem de arquivos.
- **Testes**: Os testes estão localizados na pasta src/test/java.

## Como Funciona:
Construção do Índice Invertido:

A aplicação processa os arquivos no diretório informado, criando um índice invertido que mapeia cada palavra aos arquivos que a contêm.
O índice é armazenado em memória.

## Busca de Termos:

A busca utiliza o índice invertido para encontrar arquivos que contenham todas as palavras fornecidas, independentemente da ordem.
O critério utilizado é o AND, ou seja, todos os termos precisam estar presentes em um arquivo para ele ser considerado relevante.

### Detalhes de Implementação
- A aplicação usa multithreading para processar arquivos em paralelo, utilizando um pool de threads igual ao número de núcleos disponíveis no processador.
- O índice invertido é armazenado em um ConcurrentHashMap para suportar a manipulação em múltiplas threads.
- O método de busca encontra os arquivos de maneira eficiente usando interseção de conjuntos.
