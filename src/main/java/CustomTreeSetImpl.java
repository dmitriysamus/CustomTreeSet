import java.util.Comparator;

public class CustomTreeSetImpl<T> implements CustomTreeSet<T> {
    private static class Node<T> {
        T item;
        Node<T> left, right;
        Node<T> parent;
        public Node(T item) {
            this.item = item;
        }
        void setLeftChild(Node<T> node) {
            this.left = node;
            if (this.left != null) {
                this.left.parent = this;
            }
        }
        void setRightChild(Node<T> node) {
            this.right = node;
            if (this.right != null) {
                this.right.parent = this;
            }
        }
    }

    private Node<T> root;
    private final Comparator<T> comparator;
    private int size;

    public CustomTreeSetImpl(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(T item) {
        if (root == null) {
            root = new Node<>(item);
            size = 1;
            return true;
        }

        return insertAfter(root, item);
    }

    private boolean insertAfter(Node<T> node, T newItem) {
        int result = comparator.compare(newItem, node.item);
        if (result == 0) {
            return false;
        }
        if (result < 0) {
            if (node.left == null) {
                node.left = new Node<>(newItem);
                node.left.parent = node;
                size++;
                return true;
            } else {
                return insertAfter(node.left, newItem);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(newItem);
                node.right.parent = node;
                size++;
                return true;
            } else {
                return insertAfter(node.right, newItem);
            }
        }
    }

    public boolean remove(Object item) {
        return false;
    }

    public boolean contains(Object item) {
        return false;
    }

    public Object[] toArray() {
        return new Object[0];
    }
}
