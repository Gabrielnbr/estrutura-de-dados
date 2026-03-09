package src.validador;
import java.util.List;

public class ValidadorDocumento {
    public boolean validarDocumento(List<String> hashOriginal, List<String> hashNovo){
        if(hashOriginal.size() != hashNovo.size()){
            return false;
        }

        for(int i = 0; i < hashOriginal.size(); i++){
            if (!hashOriginal.get(i).equals(hashNovo.get(i))){
                return false;
            }
        }
        return true;
    }
}
