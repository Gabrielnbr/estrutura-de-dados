public class ArvoreAVL {

    private NoAVL raiz;

    public ArvoreAVL(){
        this.raiz = null;
    }

    public void inserir(String valor) {
        raiz = inserirRecursivo(raiz, valor);
    }

    private NoAVL inserirRecursivo(NoAVL no, String valor) {
        if (no == null) {
            return new NoAVL(valor);
        }

        int comparacao = valor.compareToIgnoreCase(no.valor);

        if (comparacao < 0) {
            no.esquerda = inserirRecursivo(no.esquerda, valor);
        } else if (comparacao > 0) {
            no.esquerda = inserirRecursivo(no.direita, valor);
        } else {
            return no;
        }

        return no;
    }
}
