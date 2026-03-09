package src.pilha;

import src.avl.ArvoreAVL;

public class NoPilha {
    ArvoreAVL arvore;
    NoPilha proximo;

    public NoPilha(ArvoreAVL arvore){
        this.arvore = arvore;
        this.proximo = null;
    }
}
