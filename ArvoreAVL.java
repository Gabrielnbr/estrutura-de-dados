public class ArvoreAVL {

    private NoAVL raiz;

    private int altura(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return no.altura;
    }

    public ArvoreAVL() {
        this.raiz = null;
    }

    public void inserir(String valor) {
        raiz = inserirRecursivo(raiz, valor);
    }

    private void atualizaAltura(NoAVL no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private NoAVL inserirRecursivo(NoAVL no, String valor) {
        if (no == null) {
            return new NoAVL(valor);
        }

        int comparacao = valor.compareToIgnoreCase(no.valor);

        if (comparacao < 0) {
            no.esquerda = inserirRecursivo(no.esquerda, valor);
        } else if (comparacao > 0) {
            no.direita = inserirRecursivo(no.direita, valor);
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
            System.out.print(no.valor + " ");
            imprimirEmOrdemRecursivo(no.direita);
        }
    }
}
