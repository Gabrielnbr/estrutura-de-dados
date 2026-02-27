import java.util.List;

public class Main {
    public static void main(String[] args) {
        LeitorArquivo leitor = new LeitorArquivo();
        ProcessadorLinha processador = new ProcessadorLinha();
        ArvoreAVL arvore = new ArvoreAVL();

        List<String> linhas = leitor.lerLinhas("Text.txt");

        for (String linha : linhas) {
            System.out.println("Linha:");
            System.out.println(linha);

            List<String> palavras = processador.separarPalavras(linha);

            System.out.println("Lista de palavras:");

            for (String palavra : palavras) {
                for (int i = palavras.size() - 1; i>=0; i--){
                    arvore.inserir(palavras.get(i));
                }
            }

            System.out.println("------------------");
        }
    }
}
