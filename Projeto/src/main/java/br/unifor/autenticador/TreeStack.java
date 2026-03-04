package br.unifor.autenticador;

public class TreeStack {
    private static class Node {
        private final AVLTree tree;
        private Node next;

        private Node(AVLTree tree) {
            this.tree = tree;
        }
    }

    private Node top;

    public void push(AVLTree tree) {
        Node node = new Node(tree);
        node.next = top;
        top = node;
    }

    public AVLTree pop() {
        if (top == null) {
            throw new IllegalStateException("Pilha vazia");
        }
        AVLTree tree = top.tree;
        top = top.next;
        return tree;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
