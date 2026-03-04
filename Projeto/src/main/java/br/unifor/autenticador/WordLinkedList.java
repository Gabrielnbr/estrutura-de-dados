package br.unifor.autenticador;

public class WordLinkedList {
    public interface InsertListener {
        void onInsertAttempt(String word, boolean inserted, AVLTree tree);
    }

    private static class Node {
        private final String value;
        private Node prev;
        private Node next;

        private Node(String value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public static WordLinkedList fromLine(String line) {
        WordLinkedList list = new WordLinkedList();
        if (line == null) {
            return list;
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return list;
        }

        String[] tokens = trimmed.split("\\s+");
        for (String token : tokens) {
            if (!token.trim().isEmpty()) {
                list.addLast(token);
            }
        }

        return list;
    }

    public void addLast(String value) {
        Node node = new Node(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insertReverseInto(AVLTree tree) {
        insertReverseInto(tree, null);
    }

    public void insertReverseInto(AVLTree tree, InsertListener listener) {
        Node current = tail;
        while (current != null) {
            boolean inserted = tree.insert(current.value);
            if (listener != null) {
                listener.onInsertAttempt(current.value, inserted, tree);
            }
            current = current.prev;
        }
    }
}
