package br.unifor.autenticador;

public class AVLTree {
    private static class Node {
        private final String word;
        private Node left;
        private Node right;
        private int height;

        private Node(String word) {
            this.word = word;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private boolean insertedInLastOperation;

    public boolean insert(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        insertedInLastOperation = false;
        root = insert(root, word);
        if (insertedInLastOperation) {
            size++;
        }
        return insertedInLastOperation;
    }

    public String computeRootHash() {
        if (root == null) {
            return Sha1.hash("");
        }
        return computeSubtreeHash(root);
    }

    public int size() {
        return size;
    }

    public String toStructuredString() {
        if (root == null) {
            return "(arvore vazia)";
        }
        StringBuilder sb = new StringBuilder();
        appendTree(root, "", true, sb);
        return sb.toString();
    }

    private Node insert(Node node, String word) {
        if (node == null) {
            insertedInLastOperation = true;
            return new Node(word);
        }

        int cmp = word.compareToIgnoreCase(node.word);
        if (cmp < 0) {
            node.left = insert(node.left, word);
        } else if (cmp > 0) {
            node.right = insert(node.right, word);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = balanceFactor(node);

        if (balance > 1 && word.compareToIgnoreCase(node.left.word) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && word.compareToIgnoreCase(node.right.word) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && word.compareToIgnoreCase(node.left.word) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && word.compareToIgnoreCase(node.right.word) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private String computeSubtreeHash(Node node) {
        String own = Sha1.hash(node.word);
        boolean hasLeft = node.left != null;
        boolean hasRight = node.right != null;

        if (!hasLeft && !hasRight) {
            return own;
        }

        StringBuilder merged = new StringBuilder();
        if (hasLeft) {
            merged.append(computeSubtreeHash(node.left));
        }
        if (hasRight) {
            merged.append(computeSubtreeHash(node.right));
        }
        merged.append(own);

        return Sha1.hash(merged.toString());
    }

    private void appendTree(Node node, String prefix, boolean isTail, StringBuilder sb) {
        if (node.right != null) {
            appendTree(node.right, prefix + (isTail ? "|   " : "    "), false, sb);
        }
        sb.append(prefix)
          .append(isTail ? "\\-- " : "/-- ")
          .append(node.word)
          .append(System.lineSeparator());
        if (node.left != null) {
            appendTree(node.left, prefix + (isTail ? "    " : "|   "), true, sb);
        }
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }
}
