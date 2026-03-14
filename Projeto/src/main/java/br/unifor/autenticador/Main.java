package br.unifor.autenticador;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Ponto de entrada do autenticador digital.
 *
 * <p>Este arquivo coordena todo o fluxo do trabalho: le o arquivo TXT,
 * transforma cada linha em uma lista dinamica de palavras, monta uma arvore
 * AVL para cada linha, empilha essas arvores e depois as desempilha para gerar
 * os hashes finais do documento.
 *
 * <p>Uso esperado:
 * <ul>
 * <li>primeiro argumento: caminho do arquivo TXT de entrada;</li>
 * <li>segundo argumento opcional: arquivo onde os hashes serao salvos;</li>
 * <li>flag opcional {@code --verbose}: mostra a montagem e desmontagem das
 * arvores no console.</li>
 * </ul>
 */
public class Main {
    /**
     * Executa o programa principal conforme os argumentos informados na linha
     * de comando.
     *
     * @param args argumentos de execucao do programa
     */
    public static void main(String[] args) {
        // Sem arquivo de entrada nao ha como iniciar o processamento.
        if (args.length == 0) {
            System.out.println("Uso: java -jar autenticador-estruturas.jar <arquivo.txt> [saida.txt] [--verbose]");
            return;
        }

        Path inputPath = Paths.get(args[0]);
        boolean verbose = false;
        Path outputPath = null;

        // Interpreta os argumentos opcionais: arquivo de saida e modo verboso.
        for (int i = 1; i < args.length; i++) {
            if ("--verbose".equalsIgnoreCase(args[i])) {
                verbose = true;
            } else if (outputPath == null) {
                outputPath = Paths.get(args[i]);
            }
        }

        // Interrompe cedo se o arquivo informado nao existir.
        if (!Files.exists(inputPath)) {
            System.err.println("Arquivo nao encontrado: " + inputPath);
            return;
        }

        TreeStack lineTreeStack = new TreeStack();

        try {
            List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
            final boolean verboseMode = verbose;

            int lineNumber = 0;
            // Processa cada linha do arquivo para montar uma arvore AVL independente.
            for (String line : lines) {
                lineNumber++;
                WordLinkedList words = WordLinkedList.fromLine(line);
                if (words.isEmpty()) {
                    // Linhas vazias nao geram estruturas nem hashes.
                    if (verboseMode) {
                        System.out.println("[Linha " + lineNumber + "] vazia, ignorada.");
                    }
                    continue;
                }

                AVLTree tree = new AVLTree();
                // No modo verboso, mostra o inicio da montagem da arvore da linha atual.
                if (verboseMode) {
                    System.out.println();
                    System.out.println("=== MONTANDO ARVORE DA LINHA " + lineNumber + " ===");
                    System.out.println("Texto: " + line);
                }
                // Insere as palavras em ordem reversa, registrando cada tentativa no console.
                words.insertReverseInto(tree, (word, inserted, currentTree) -> {
                    if (verboseMode) {
                        System.out.println("Inserindo \"" + word + "\" -> " + (inserted ? "ok" : "duplicada, ignorada"));
                    }
                });

                // Exibe a estrutura final da arvore antes de empilha-la.
                if (verboseMode) {
                    System.out.println("Arvore final da linha " + lineNumber + " (" + tree.size() + " palavras unicas):");
                    System.out.print(tree.toStructuredString());
                }
                lineTreeStack.push(tree);
                if (verboseMode) {
                    System.out.println("Empilhada.");
                }
            }

            StringBuilder output = new StringBuilder();
            boolean first = true;
            int popIndex = 1;
            // Desempilha as arvores na ordem inversa e gera um hash por linha processada.
            while (!lineTreeStack.isEmpty()) {
                AVLTree tree = lineTreeStack.pop();
                String hash = tree.computeRootHash();
                if (verboseMode) {
                    System.out.println();
                    System.out.println("=== DESEMPILHANDO ARVORE #" + popIndex + " ===");
                    System.out.print(tree.toStructuredString());
                    System.out.println("Hash da arvore: " + hash);
                }
                // Separa os hashes finais por quebra de linha, como pede o trabalho.
                if (!first) {
                    output.append(System.lineSeparator());
                }
                output.append(hash);
                first = false;
                popIndex++;
            }

            String result = output.toString();
            if (verboseMode) {
                System.out.println();
                System.out.println("=== HASHES FINAIS ===");
            }
            System.out.println(result);

            // Grava o resultado em arquivo apenas quando um caminho de saida for informado.
            if (outputPath != null) {
                Files.write(outputPath, result.getBytes(StandardCharsets.UTF_8));
                System.out.println("Hashes salvos em: " + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
        }
    }
}
