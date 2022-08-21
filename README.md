# IsiLanguage

## Checklist

### Elementos padrão

- [x] Possuir 2 tipos de dados (pelo menos 1 String)  
- [x] Possuir a instrução de decisão (if/else)
- [x] Pelo menos 1 estrutura de repetição
- [x] Verificar Atribuições com compatibilidade de tipos (semântica)  
- [x] Possuir operações de Entrada e Saída
- [x] Aceitar números decimais  
- [x] Verificar declaração de variávies (não usar variáveis que não foram declaradas)
- [ ] Verificar se há variáveis declaradas e não-utilizadas (warning)
- [x] Geração de pelo menos 1 linguagem destino (C/Java/Python)

### Elementos adicionais

- [ ] Nova instrução para Switch/Case (escolha/caso)
- [ ] Mais tipos de dados
- [ ] Inclusão de novos operadores (exponenciação, raiz quadrada, logaritmos)
- [ ] Geração de código para mais de uma linguagem diferente

### Elementos extraordinários

- [ ] Criar um interpretador a partir da AST
- [ ] Criar um editor com Highlights de palavras reservadas (editor Desktop)
- [ ] Criar um editor Web para o código
- [ ] Tornar o compilador um Webservice para receber programas e enviar respostas de possíveis erros

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
