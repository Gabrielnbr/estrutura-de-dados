package src.main;

import java.util.List;
import java.util.ArrayList;

import src.avl.ArvoreAVL;
import src.io.LeitorArquivo;
import src.pilha.PilhaArvores;
import src.processamento.ProcessadorLinha;
import src.validador.ValidadorDocumento;

public class Main {
    public static void main(String[] args) {
        LeitorArquivo leitor = new LeitorArquivo();
        ProcessadorLinha processador = new ProcessadorLinha();
        ValidadorDocumento validador = new ValidadorDocumento();

        List<String> texto_original = leitor.lerLinhas("docs/text.txt");
        PilhaArvores pilha = new PilhaArvores();

        for (String linha : texto_original) {
            ArvoreAVL arvore = new ArvoreAVL();
            List<String> palavras = processador.separarPalavras(linha);

            for (int i = palavras.size() - 1; i >= 0; i--) {
                arvore.inserir(palavras.get(i));
            }
            pilha.empilhar(arvore);
        }
        List<String> hashString = new ArrayList<>();
        System.out.println("HASHES do documento:");
        while (!pilha.vazia()) {
            ArvoreAVL arvore = pilha.desempilhar();
            String hash = arvore.calcularHash();
            hashString.add(hash);
            System.out.println(hash);
        }

        // Novo documento
        List<String> texto_Novo = leitor.lerLinhas("docs/text_alterado.txt");
        PilhaArvores pilha2 = new PilhaArvores();

        for (String linha : texto_Novo) {
            ArvoreAVL arvore = new ArvoreAVL();
            List<String> palavras = processador.separarPalavras(linha);

            for (int i = palavras.size() - 1; i >= 0; i--) {
                arvore.inserir(palavras.get(i));
            }
            pilha2.empilhar(arvore);
        }
        List<String> hashStringNovo = new ArrayList<>();
        while (!pilha2.vazia()) {
            ArvoreAVL arvore = pilha2.desempilhar();
            String hash = arvore.calcularHash();
            hashStringNovo.add(hash);
        }

        System.out.println("\nResultado:");
        boolean validado = validador.validarDocumento(hashString, hashStringNovo);
        if (validado) {
            System.out.println("Documento não alterado.");
        } else {
            System.out.println("Documento alterado.");
        }
    }
}
