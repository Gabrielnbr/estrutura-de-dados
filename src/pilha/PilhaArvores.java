package src.pilha;

import src.avl.ArvoreAVL;

public class PilhaArvores {
    private NoPilha topo;

    public PilhaArvores(){
        this.topo = null;
    }

    public void empilhar(ArvoreAVL arvore){
        NoPilha novo = new NoPilha(arvore);

        novo.proximo = topo;
        topo = novo;
    }

    public ArvoreAVL desempilhar(){
        if (vazia()){
            return null;
        }

        ArvoreAVL arvore = topo.arvore;
        topo = topo.proximo;

        return arvore;
    }

    public boolean vazia(){
        return topo == null;
    }
}
