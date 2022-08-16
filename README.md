# IsiLanguage

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
                        <argument>input2.isi</argument>
                    </arguments>
                </configuration>
                ...
```

Para gerar novamente os arquivos referente ao ANTLR4, basta executar o comando `mvn compile`, que automaticamente irá regerar os arquivos na pasta `parser` do projeto.

## Nome e RA dos integrantes do grupo
- [Nome], [RA]
- [Nome], [RA]
- [Nome], [RA]
- [Nome], [RA]

## Checklist
- [ ] Possuir 2 tipos de dados (pelo menos 1 String) 	
- [x] Possuir a instrução de decisão (if/else)	
- [ ] Pelo menos 1 estrutura de repetição	
- [ ] Verificar Atribuições com compatibilidade de tipos (semântica) 	
- [ ] Possuir operações de Entrada e Saída	
- [ ] Aceitar números decimais 	
- [ ] Verificar declaração de variávies (não usar variáveis que não foram declaradas)	
- [ ] Verificar se há variáveis declaradas e não-utilizadas (warning)	
- [ ] Geração de pelo menos 1 linguagem destino (C/Java/Python)