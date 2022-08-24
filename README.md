# IsiLanguage

## Checklist

O vídeo explicativo do projeto pode ser acessado [aqui]().

### Elementos padrão

- [x] Possuir 2 tipos de dados (pelo menos 1 String)  
- [x] Possuir a instrução de decisão (if/else)
- [x] Pelo menos 1 estrutura de repetição
- [x] Verificar Atribuições com compatibilidade de tipos (semântica)  
- [x] Possuir operações de Entrada e Saída
- [x] Aceitar números decimais  
- [x] Verificar declaração de variávies (não usar variáveis que não foram declaradas)
- [x] Verificar se há variáveis declaradas e não-utilizadas (warning)
- [x] Geração de pelo menos 1 linguagem destino (C/Java/Python)

### Elementos adicionais

- [ ] Nova instrução para Switch/Case (escolha/caso)
- [x] Mais tipos de dados
- [ ] Inclusão de novos operadores (exponenciação, raiz quadrada, logaritmos)
- [x] Geração de código para mais de uma linguagem diferente

### Elementos extraordinários

- [x] Criar um interpretador a partir da AST
- [ ] Criar um editor com Highlights de palavras reservadas (editor Desktop)
- [ ] Criar um editor Web para o código
- [x] Tornar o compilador um Webservice para receber programas e enviar respostas de possíveis erros

## Pré-requisitos

- Java 8
- Maven

Em ambiente Linux, para facilitar, é possível instalar os utilitários do Java usando o [SDKMAN!](https://sdkman.io/), executando os seguintes comandos:

```bash
# Para instalar o SDKMAN!
curl -s "https://get.sdkman.io" | bash

# Executar os comandos abaixo em um novo terminal.
# Para instalar a versão do Java
sdk install java 8.0.265-open

# Para instalar o Maven
sdk install maven
```

## Execução

Para executar o projeto, basta realizar o comando `mvn exec:java`. O arquivo que será utilizado para a compilação está definida dentro do arquivo `pom.xml`, portanto basta alterar nesse arquivo caso queira compilar outro arquivo `.isi`, da forma que se segue:

```xml
                ...
                <configuration>
                    <mainClass>br.com.professorisidro.isilanguage.main.MainClass</mainClass>
                    <arguments>
                        <!-- Substituir o arquivo para ser compilado aqui -->
                        <argument>inputs/1_input.isi</argument>
                    </arguments>
                </configuration>
                ...
```

Para gerar novamente os arquivos referente ao ANTLR4, basta executar o comando `mvn compile`, que automaticamente irá regerar os arquivos na pasta `parser` do projeto.

Por fim, como o projeto está estruturado como uma CLI, ele também pode ser executado utilizando o script `run.sh` que se encontra na raíz do projeto, usando o formato `./run.sh <comando> <argumentos>`.

### Comando `compile`

O comando `compile` recebe como argumento o caminho do programa para ser compilado e retorna o resultado da compilação no próprio terminal. Um exemplo de execução deste comando pode ser visto abaixo:

```bash
$ ./run.sh compile inputs/4_boolean.isi 
...
Compilation Successful
CommandEscrita [id="Teste boolean"]
CommandLeitura [id=a]
CommandLeitura [id=b]
CommandEscrita [id=a]
CommandEscrita [id=b]
CommandEscrita [id=a&&b]
```

### Comando `server`

O comando `server` não recebe argumentos e disponibiliza o compilador como um web service. Um exemplo de execução e utilização deste serviço pode ser visto abaixo:

```bash
$ ./run.sh server
...
Aug 24, 2022 12:03:12 AM org.restlet.engine.connector.NetServerHelper start
INFO: Starting the internal [HTTP/1.1] server on port 8080

# Em outro terminal.
$ curl -F file=@inputs/1_input.isi localhost:8080/compile
{"result":[{"type":"info","message":"Compilation successful"}],"error":null}
```

### Comando `interpret`

O comando `interpret` recebe como argumento o caminho do programa para ser interpretado e executa o programa no próprio terminal. Um exemplo de execução deste comando pode ser visto abaixo:

```bash
$ ./run.sh interpret inputs/1_input.isi 
...
Compilation Successful
Programa Teste
Digite A
...
```

## Participantes

- Dilermando Queiroz Neto (RA: 11201722993)
- Jonathan Takeshi Ywahashi (RA: 11201810503)
- Natália Satie Motokubo Halker (RA: 11025613)
- Yuri Neudine Tropeia (RA: 21005816)
