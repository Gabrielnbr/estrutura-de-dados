package br.unifor.autenticador;

/**
 * Implementa a arvore AVL usada para armazenar as palavras de uma linha.
 *
 * <p>Esta classe recebe as palavras em ordem reversa, ignora duplicatas com
 * base em comparacao lexicografica sem diferenciar maiusculas e minusculas e
 * mantem a arvore balanceada com rotacoes AVL. Depois, o hash final da linha
 * e calculado a partir da raiz, combinando recursivamente os hashes dos filhos
 * e do proprio no.
 *
 * <p>Uso esperado:
 * <ul>
 * <li>criar uma instancia com {@code new AVLTree()};</li>
 * <li>inserir palavras com {@link #insert(String)};</li>
 * <li>obter o hash final com {@link #computeRootHash()};</li>
 * <li>usar {@link #toStructuredString()} para visualizar a estrutura no modo
 * verboso.</li>
 * </ul>
 */
public class AVLTree {
    /**
     * Representa um no interno da arvore AVL.
     *
     * <p>Cada no guarda a palavra inserida, as referencias para os filhos e a
     * altura da subarvore enraizada nele. A altura e atualizada apos cada
     * insercao ou rotacao para permitir o calculo do fator de balanceamento.
     */
    private static class Node {
        private final String word;
        private Node left;
        private Node right;
        private int height;

        /**
         * Cria um novo no contendo a palavra informada.
         *
         * @param word palavra armazenada no no
         */
        private Node(String word) {
            this.word = word;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private boolean insertedInLastOperation;

    /**
     * Insere uma palavra na arvore se ela ainda nao existir.
     *
     * @param word palavra a ser inserida
     * @return {@code true} se a palavra foi inserida; {@code false} se era
     *     nula, vazia ou duplicada
     */
    public boolean insert(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }

        // Reinicia a flag para saber se esta chamada realmente criou um novo no.
        insertedInLastOperation = false;
        root = insert(root, word);
        if (insertedInLastOperation) {
            size++;
        }
        return insertedInLastOperation;
    }

    /**
     * Calcula o hash final da arvore usando a logica definida no trabalho.
     *
     * @return hash SHA-1 armazenado logicamente na raiz
     */
    public String computeRootHash() {
        if (root == null) {
            return Sha1.hash("");
        }
        return computeSubtreeHash(root);
    }

    /**
     * Informa quantas palavras unicas foram inseridas na arvore.
     *
     * @return quantidade de nos da arvore
     */
    public int size() {
        return size;
    }

    /**
     * Gera uma representacao textual da arvore para depuracao no console.
     *
     * @return texto com a estrutura atual da arvore
     */
    public String toStructuredString() {
        if (root == null) {
            return "(arvore vazia)";
        }
        StringBuilder sb = new StringBuilder();
        appendTree(root, "", true, sb);
        return sb.toString();
    }

    /**
     * Insere recursivamente uma palavra em uma subarvore e reaplica o
     * balanceamento AVL no caminho de volta da recursao.
     *
     * @param node raiz atual da subarvore analisada
     * @param word palavra a ser inserida
     * @return nova raiz da subarvore apos a insercao e possiveis rotacoes
     */
    private Node insert(Node node, String word) {
        if (node == null) {
            insertedInLastOperation = true;
            return new Node(word);
        }

        int cmp = word.compareToIgnoreCase(node.word);
        // Decide se a palavra deve descer para a esquerda, direita ou ser ignorada.
        if (cmp < 0) {
            node.left = insert(node.left, word);
        } else if (cmp > 0) {
            node.right = insert(node.right, word);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = balanceFactor(node);

        // Caso esquerda-esquerda: a nova palavra entrou na subarvore esquerda do filho esquerdo.
        if (balance > 1 && word.compareToIgnoreCase(node.left.word) < 0) {
            return rotateRight(node);
        }

        // Caso direita-direita: a nova palavra entrou na subarvore direita do filho direito.
        if (balance < -1 && word.compareToIgnoreCase(node.right.word) > 0) {
            return rotateLeft(node);
        }

        // Caso esquerda-direita: primeiro corrige o filho esquerdo, depois a raiz atual.
        if (balance > 1 && word.compareToIgnoreCase(node.left.word) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Caso direita-esquerda: primeiro corrige o filho direito, depois a raiz atual.
        if (balance < -1 && word.compareToIgnoreCase(node.right.word) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Calcula recursivamente o hash de uma subarvore.
     *
     * <p>Para folhas, retorna apenas o hash da propria palavra. Para nos
     * internos, concatena o hash do filho esquerdo, o hash do filho direito e
     * o hash da palavra do proprio no, nessa ordem, e aplica SHA-1 ao
     * resultado.
     *
     * @param node raiz da subarvore analisada
     * @return hash correspondente a subarvore informada
     */
    private String computeSubtreeHash(Node node) {
        String own = Sha1.hash(node.word);
        boolean hasLeft = node.left != null;
        boolean hasRight = node.right != null;

        // Se o no for folha, o hash final dele e apenas o hash da palavra armazenada.
        if (!hasLeft && !hasRight) {
            return own;
        }

        StringBuilder merged = new StringBuilder();
        // So concatena o hash do filho quando o filho realmente existe.
        if (hasLeft) {
            merged.append(computeSubtreeHash(node.left));
        }
        if (hasRight) {
            merged.append(computeSubtreeHash(node.right));
        }
        merged.append(own);

        return Sha1.hash(merged.toString());
    }

    /**
     * Monta uma representacao textual lateral da arvore.
     *
     * <p>O metodo percorre primeiro a direita, depois escreve o no atual e por
     * fim percorre a esquerda. Isso faz a impressao ficar parecida com uma
     * arvore desenhada no console.
     *
     * @param node no atual da travessia
     * @param prefix prefixo de indentacao acumulado
     * @param isTail indica se o no atual e o ultimo filho daquele nivel
     * @param sb acumulador do texto gerado
     */
    private void appendTree(Node node, String prefix, boolean isTail, StringBuilder sb) {
        // Imprime primeiro a subarvore da direita para que ela apareca acima no console.
        if (node.right != null) {
            appendTree(node.right, prefix + (isTail ? "|   " : "    "), false, sb);
        }
        sb.append(prefix)
          .append(isTail ? "\\-- " : "/-- ")
          .append(node.word)
          .append(System.lineSeparator());
        // Imprime depois a subarvore da esquerda para que ela apareca abaixo no console.
        if (node.left != null) {
            appendTree(node.left, prefix + (isTail ? "    " : "|   "), true, sb);
        }
    }

    /**
     * Retorna a altura de um no.
     *
     * @param node no consultado
     * @return altura do no ou zero quando ele nao existe
     */
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Calcula o fator de balanceamento de um no.
     *
     * @param node no consultado
     * @return diferenca entre as alturas da esquerda e da direita
     */
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    /**
     * Executa uma rotacao simples para a direita.
     *
     * @param y raiz da subarvore desbalanceada
     * @return nova raiz da subarvore apos a rotacao
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        // Move o filho esquerdo para a raiz da subarvore e reposiciona a antiga raiz.
        x.right = y;
        y.left = t2;

        // Recalcula as alturas para manter o balanceamento consistente.
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    /**
     * Executa uma rotacao simples para a esquerda.
     *
     * @param x raiz da subarvore desbalanceada
     * @return nova raiz da subarvore apos a rotacao
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        // Move o filho direito para a raiz da subarvore e reposiciona a antiga raiz.
        y.left = x;
        x.right = t2;

        // Recalcula as alturas para manter o balanceamento consistente.
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }
}
