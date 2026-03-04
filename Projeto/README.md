# Autenticador Digital - Estruturas de Dados

Projeto Java para o trabalho vivencial de Estruturas de Dados.

## Requisitos

- JDK 8+ (com `javac` e `java` no PATH)
- Opcional: Maven 3.8+

## Como executar (sem Maven)

```powershell
cd Projeto
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out src\main\java\br\unifor\autenticador\*.java
java -cp out br.unifor.autenticador.Main TEXTO.TXT
```

Para salvar a saida em arquivo:

```powershell
java -cp out br.unifor.autenticador.Main TEXTO.TXT hashes.txt
```

## Como executar (Maven)

```powershell
cd Projeto
mvn -q package
java -cp target\classes br.unifor.autenticador.Main TEXTO.TXT
```

## Fluxo implementado

1. Leitura do arquivo `.txt` linha a linha.
2. Cada linha vira uma lista dinamica de palavras (separacao por espacos em branco).
3. Insercao reversa das palavras em arvore AVL, sem duplicatas (`compareToIgnoreCase`).
4. Cada AVL da linha e empilhada.
5. Desempilhamento das arvores para gerar hash SHA-1 de cada linha.
6. Impressao final dos hashes concatenados por quebra de linha.
