import java.util.ArrayList;
import java.util.List;

public class ProcessadorLinha {
    public List<String> separarPalavras(String linha){
        List<String> palavras = new ArrayList<>();
        String[] partes = linha.split(" ");

        for (String palavra : partes) {
            palavras.add(palavra);
        }

        return palavras;
    }
}
