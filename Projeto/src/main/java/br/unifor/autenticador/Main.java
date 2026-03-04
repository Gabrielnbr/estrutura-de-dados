package br.unifor.autenticador;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java -jar autenticador-estruturas.jar <arquivo.txt> [saida.txt] [--verbose]");
            return;
        }

        Path inputPath = Paths.get(args[0]);
        boolean verbose = false;
        Path outputPath = null;

        for (int i = 1; i < args.length; i++) {
            if ("--verbose".equalsIgnoreCase(args[i])) {
                verbose = true;
            } else if (outputPath == null) {
                outputPath = Paths.get(args[i]);
            }
        }

        if (!Files.exists(inputPath)) {
            System.err.println("Arquivo nao encontrado: " + inputPath);
            return;
        }

        TreeStack lineTreeStack = new TreeStack();

        try {
            List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
            final boolean verboseMode = verbose;

            int lineNumber = 0;
            for (String line : lines) {
                lineNumber++;
                WordLinkedList words = WordLinkedList.fromLine(line);
                if (words.isEmpty()) {
                    if (verboseMode) {
                        System.out.println("[Linha " + lineNumber + "] vazia, ignorada.");
                    }
                    continue;
                }

                AVLTree tree = new AVLTree();
                if (verboseMode) {
                    System.out.println();
                    System.out.println("=== MONTANDO ARVORE DA LINHA " + lineNumber + " ===");
                    System.out.println("Texto: " + line);
                }
                words.insertReverseInto(tree, (word, inserted, currentTree) -> {
                    if (verboseMode) {
                        System.out.println("Inserindo \"" + word + "\" -> " + (inserted ? "ok" : "duplicada, ignorada"));
                    }
                });

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
            while (!lineTreeStack.isEmpty()) {
                AVLTree tree = lineTreeStack.pop();
                String hash = tree.computeRootHash();
                if (verboseMode) {
                    System.out.println();
                    System.out.println("=== DESEMPILHANDO ARVORE #" + popIndex + " ===");
                    System.out.print(tree.toStructuredString());
                    System.out.println("Hash da arvore: " + hash);
                }
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

            if (outputPath != null) {
                Files.write(outputPath, result.getBytes(StandardCharsets.UTF_8));
                System.out.println("Hashes salvos em: " + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
        }
    }
}
