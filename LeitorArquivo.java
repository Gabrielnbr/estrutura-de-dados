import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivo {
    public List<String> lerLinhas (String caminhoArquivo) {
        List<String> linhas = new ArrayList<>();

        try {
            BufferedReader leitor = new BufferedReader(
                new FileReader(caminhoArquivo)
            );

            String linha;

            while ((linha = leitor.readLine()) != null){
                linhas.add(linha);
            }

            leitor.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
            e.printStackTrace();
        }

        return linhas;
    }
}
