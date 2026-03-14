package br.unifor.autenticador;

/**
 * Implementa a lista dinamica de palavras exigida pelo trabalho.
 *
 * <p>Esta classe recebe o texto de uma linha, separa as palavras por espacos
 * em branco e as armazena em uma lista duplamente encadeada. Depois, os
 * elementos podem ser percorridos do fim para o inicio para alimentar a arvore
 * AVL na ordem pedida pelo enunciado.
 *
 * <p>Uso esperado:
 * <ul>
 * <li>criar uma lista a partir de uma linha com {@link #fromLine(String)};</li>
 * <li>verificar se a linha gerou elementos com {@link #isEmpty()};</li>
 * <li>inserir os itens em uma AVL com {@link #insertReverseInto(AVLTree)} ou
 * {@link #insertReverseInto(AVLTree, InsertListener)}.</li>
 * </ul>
 */
public class WordLinkedList {
    /**
     * Interface de apoio para acompanhar cada tentativa de insercao na arvore.
     */
    public interface InsertListener {
        /**
         * Recebe o resultado de cada tentativa de insercao na AVL.
         *
         * @param word palavra analisada
         * @param inserted indica se a palavra entrou na arvore
         * @param tree referencia da arvore atual
         */
        void onInsertAttempt(String word, boolean inserted, AVLTree tree);
    }

    /**
     * Representa um no da lista duplamente encadeada.
     *
     * <p>Cada no guarda uma palavra e referencias para o elemento anterior e o
     * proximo, o que permite percorrer a lista tanto do inicio para o fim
     * quanto do fim para o inicio.
     */
    private static class Node {
        private final String value;
        private Node prev;
        private Node next;

        /**
         * Cria um novo no para armazenar uma palavra da linha.
         *
         * @param value palavra associada ao no
         */
        private Node(String value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    /**
     * Cria uma lista de palavras a partir de uma linha do texto.
     *
     * @param line linha original do arquivo
     * @return lista dinamica contendo as palavras encontradas
     */
    public static WordLinkedList fromLine(String line) {
        WordLinkedList list = new WordLinkedList();
        if (line == null) {
            return list;
        }

        String trimmed = line.trim();
        // Se a linha tiver apenas espacos, ela nao gera nenhuma palavra valida.
        if (trimmed.isEmpty()) {
            return list;
        }

        String[] tokens = trimmed.split("\\s+");
        // Adiciona cada palavra identificada pela separacao por espacos em branco.
        for (String token : tokens) {
            if (!token.trim().isEmpty()) {
                list.addLast(token);
            }
        }

        return list;
    }

    /**
     * Adiciona uma palavra ao final da lista.
     *
     * @param value palavra a ser armazenada
     */
    public void addLast(String value) {
        Node node = new Node(value);
        // Se a lista estiver vazia, o novo no passa a ser inicio e fim.
        if (head == null) {
            head = node;
            tail = node;
        } else {
            // Caso contrario, encadeia o novo no ao final atual.
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    /**
     * Verifica se a lista nao possui elementos.
     *
     * @return {@code true} quando a lista esta vazia
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Percorre a lista do fim para o inicio e insere os elementos na AVL.
     *
     * @param tree arvore que recebera as palavras
     */
    public void insertReverseInto(AVLTree tree) {
        insertReverseInto(tree, null);
    }

    /**
     * Percorre a lista do fim para o inicio e tenta inserir cada palavra na
     * arvore, notificando um observador a cada tentativa.
     *
     * @param tree arvore que recebera as palavras
     * @param listener observador opcional para o processo de insercao
     */
    public void insertReverseInto(AVLTree tree, InsertListener listener) {
        Node current = tail;
        // Percorre a lista do ultimo elemento para o primeiro, como exige o enunciado.
        while (current != null) {
            boolean inserted = tree.insert(current.value);
            // Notifica o observador quando o modo verboso precisar acompanhar o processo.
            if (listener != null) {
                listener.onInsertAttempt(current.value, inserted, tree);
            }
            current = current.prev;
        }
    }
}
