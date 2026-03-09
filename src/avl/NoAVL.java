package src.avl;
public class NoAVL {
    String palavra;
    String hash;
    int altura;
    NoAVL esquerda;
    NoAVL direita;

    public NoAVL(String palavra, String hash){
        this.palavra = palavra;
        this.hash = hash;
        this.altura = 1;
        this.esquerda = null;
        this.direita = null;
    }
    
}
