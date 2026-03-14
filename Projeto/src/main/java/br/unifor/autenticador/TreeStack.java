package br.unifor.autenticador;

/**
 * Implementa a pilha de arvores AVL usada no processamento do documento.
 *
 * <p>Cada linha valida do texto gera uma arvore AVL. Esta classe armazena
 * essas arvores em estrutura LIFO para que, ao final da leitura do arquivo, o
 * programa as desempilhe e gere os hashes na ordem inversa da insercao.
 *
 * <p>Uso esperado:
 * <ul>
 * <li>empilhar uma arvore com {@link #push(AVLTree)};</li>
 * <li>remover a proxima arvore com {@link #pop()};</li>
 * <li>verificar se ainda existem elementos com {@link #isEmpty()}.</li>
 * </ul>
 */
public class TreeStack {
    /**
     * Representa um no da pilha encadeada.
     *
     * <p>Cada no guarda uma arvore AVL e uma referencia para o proximo no
     * abaixo dele na pilha.
     */
    private static class Node {
        private final AVLTree tree;
        private Node next;

        /**
         * Cria um novo no para armazenar uma arvore na pilha.
         *
         * @param tree arvore associada ao no
         */
        private Node(AVLTree tree) {
            this.tree = tree;
        }
    }

    private Node top;

    /**
     * Empilha uma arvore no topo da pilha.
     *
     * @param tree arvore a ser armazenada
     */
    public void push(AVLTree tree) {
        Node node = new Node(tree);
        // O novo elemento sempre entra no topo da pilha.
        node.next = top;
        top = node;
    }

    /**
     * Remove e retorna a arvore que esta no topo da pilha.
     *
     * @return arvore removida do topo
     * @throws IllegalStateException se a pilha estiver vazia
     */
    public AVLTree pop() {
        // Nao existe elemento para remover quando o topo e nulo.
        if (top == null) {
            throw new IllegalStateException("Pilha vazia");
        }
        AVLTree tree = top.tree;
        // Avanca o topo para o proximo elemento, removendo o atual da pilha.
        top = top.next;
        return tree;
    }

    /**
     * Informa se a pilha esta vazia.
     *
     * @return {@code true} quando nao ha arvores empilhadas
     */
    public boolean isEmpty() {
        return top == null;
    }
}
