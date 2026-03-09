package src.avl;
import src.hash.GeradorSHA1;

public class ArvoreAVL {

    private NoAVL raiz;

    public NoAVL getRaiz() {
        return raiz;
    }

    public ArvoreAVL() {
        this.raiz = null;
    }

    private int altura(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return no.altura;
    }

    public void inserir(String palavra) {
        String hash = GeradorSHA1.gerar(palavra);
        raiz = inserirRecursivo(raiz, palavra, hash);
    }

    private void atualizaAltura(NoAVL no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private NoAVL inserirRecursivo(NoAVL no, String palavra, String hash) {
        if (no == null) {
            return new NoAVL(palavra, hash);
        }

        int comparacao = palavra.compareToIgnoreCase(no.palavra);

        if (comparacao < 0) {
            no.esquerda = inserirRecursivo(no.esquerda, palavra, hash);
        } else if (comparacao > 0) {
            no.direita = inserirRecursivo(no.direita, palavra, hash);
        } else {
            return no;
        }

        atualizaAltura(no);

        return balancear(no);
    }

    private int fatorBalanceamento(NoAVL no) {
        if (no == null) {
            return 0;
        }

        return altura(no.esquerda) - altura(no.direita);
    }

    private NoAVL rotacionar(NoAVL no, boolean direita) {
        NoAVL novaRaiz;
        NoAVL subArvore;
        if (direita) {
            novaRaiz = no.esquerda;
            subArvore = novaRaiz.direita;
            novaRaiz.direita = no;
            no.esquerda = subArvore;
        } else {
            novaRaiz = no.direita;
            subArvore = novaRaiz.esquerda;
            novaRaiz.esquerda = no;
            no.direita = subArvore;
        }
        atualizaAltura(no);
        atualizaAltura(novaRaiz);
        return novaRaiz;
    }

    private NoAVL balancear(NoAVL no) {
        int fator = fatorBalanceamento(no);

        if (fator > 1) {
            if (fatorBalanceamento(no.esquerda) < 0) {
                no.esquerda = rotacionar(no.esquerda, false);
            }
            return rotacionar(no, true);
        } else if (fator < -1) {
            if (fatorBalanceamento(no.direita) > 0) {
                no.direita = rotacionar(no.direita, true);
            }
            return rotacionar(no, false);
        }
        return no;
    }

    public void imprimirEmOrdem() {
        imprimirEmOrdemRecursivo(raiz);
        System.out.println();
    }

    private void imprimirEmOrdemRecursivo(NoAVL no) {
        if (no != null) {
            imprimirEmOrdemRecursivo(no.esquerda);
            System.out.print("(" + no.altura + ")" + no.palavra + "|");
            imprimirEmOrdemRecursivo(no.direita);
        }
    }

    public String calcularHash(){
        return calcularHashRecursivo(raiz);
    }

    public String calcularHashRecursivo(NoAVL no) {
        if (no == null){
            return "";
        }

        String hashEsquerda = calcularHashRecursivo(no.esquerda);
        String hashDireita = calcularHashRecursivo(no.direita);

        String combinado = "";

        if (!hashEsquerda.isEmpty()){
            combinado += hashEsquerda;
        }

        if (!hashDireita.isEmpty()){
            combinado += hashDireita;
        }

        combinado += no.hash;

        return GeradorSHA1.gerar(combinado);
    }
}
