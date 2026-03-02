import java.util.List;

public class Main {
    public static void main(String[] args) {
        LeitorArquivo leitor = new LeitorArquivo();
        ProcessadorLinha processador = new ProcessadorLinha();
        ArvoreAVL arvore = new ArvoreAVL();

        List<String> linhas = leitor.lerLinhas("Text.txt");
        List<String> linha = processador.separarPalavras(linhas.get(0));

        System.out.println("linha: " + linha);
        for (String palavra : linha) {
            arvore.inserir(palavra);
        }
        System.out.println("Imprimindo em ordem");
        arvore.imprimirEmOrdem();
    }
}
