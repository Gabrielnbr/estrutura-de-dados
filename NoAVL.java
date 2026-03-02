public class NoAVL {
    String valor;
    NoAVL esquerda;
    NoAVL direita;
    int altura;

    public NoAVL(String valor){
        this.valor = valor;
        this.esquerda = null;
        this.direita = null;
        this.altura = 1;
    }
    
}
