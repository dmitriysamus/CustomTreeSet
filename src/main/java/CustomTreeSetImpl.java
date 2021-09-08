import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Множество на основе бинарного дерева поиска, реализует интерфейс CustomTreeSet<T>,
 * может хранить объекты любого типа.
 */
public class CustomTreeSetImpl<T> implements CustomTreeSet<T> {
    /**
     * Узел бинарного дерева.
     */
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

    /**
     * Конструктор объекта, принимающий на вход comparator.
     */
    public CustomTreeSetImpl(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Возвращает размер омнножества.
     */
    public int size() {
        return this.size;
    }

    /**
     * Проверяет множество на наличие объектов.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет объект в множество.
     */
    public boolean add(T item) {
        if (root == null) {
            root = new Node<>(item);
            size = 1;
            return true;
        }

        return insertAfter(root, item);
    }

    /**
     * Рекурсивно пробегается по дереву для добавления объекта.
     */
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

    /**
     * Удаляет объект из множества.
     */
    public boolean remove(T item) {
        if (root == null) {
            return false;
        }
        if (root.item == item) {
            removeThrough(root);
            return true;
        }

        return removeBefore(root, item);
    }

    /**
     * Рекурсивно пробегается по дереву для удаления объекта и сдвига узлов.
     */
    private void removeThrough(Node<T> node) {
        if (node.left == null && node.right == null) {
            if (node.parent == null) {
                root = null;
                size = 0;
                return;
            }
            if (node.parent.left == node) {
                node.parent.left = null;
                --size;
                return;
            }
            if (node.parent.right == node) {
                node.parent.right = null;
                --size;
                return;
            }
        }

        if (node.right == null) {
            swap(node, node.left);
        } else {
            if (node.right.left == null) {
                node.right.setLeftChild(node.left);
                swap(node, node.right);
            } else {
                Node<T> tmpLeft = node.right;
                Node<T> tmpPrevious = null;
                while (tmpLeft.left != null) {
                    tmpPrevious = tmpLeft;
                    tmpLeft = tmpLeft.left;
                }
                if (tmpLeft.right != null) {
                    tmpPrevious.left = tmpLeft.right;
                } else {
                    tmpPrevious.left = null;
                }
                tmpLeft.setRightChild(node.right);
                tmpLeft.setLeftChild(node.left);
                swap(node, tmpLeft);
            }
        }

    }

    /**
     * Меняет узлы местами при удалении узла.
     */
    private void swap(Node<T> node, Node<T> changeNode) {
        if (node.parent == null) {
            changeNode.parent = null;
            root = changeNode;
        } else if (node.parent.left == node) {
            node.parent.left = changeNode;
        } else if (node.parent.right == node) {
            node.parent.right = changeNode;
        }
        size--;
    }

    /**
     * Удаляет объект из множества и упорядочивает множество.
     */
    private boolean removeBefore(Node<T> node, T delItem) {
        int result = comparator.compare(delItem, node.item);
        if (result == 0) {
            removeThrough(node);
            return true;
        }
        if (result < 0) {
            if (node.left == null) {
                return false;
            } else {
                return removeBefore(node.left, delItem);
            }
        } else {
            if (node.right == null) {
                return false;
            } else {
                return removeBefore(node.right, delItem);
            }
        }
    }

    /**
     * Проверяет наличие объекта в множестве.
     */
    public boolean contains(T item) {
        if (root == null) {
            return false;
        }
        return recursiveSearch(root, item);
    }

    /**
     * Рекурсивно ищет объекта в дереве по значению.
     */
    private boolean recursiveSearch(Node<T> node, T newItem) {
        int result = comparator.compare(newItem, node.item);
        if (result == 0) {
            return true;
        }
        if (result < 0) {
            if (node.left == null) {
                return false;
            } else {
                return recursiveSearch(node.left, newItem);
            }
        } else {
            if (node.right == null) {
                return false;
            } else {
                return recursiveSearch(node.right, newItem);
            }
        }
    }

    /**
     * Возвращает объекты множества в виде массива.
     */
    public Object[] toArray() {
        if (size == 0) {
            return new Object[0];
        }
        ArrayList<T> array = new ArrayList<>();
        fillRecursive(root, array);
        Object[] newData = new Object[size];
        for (int i = 0; i < size; i++) {
            newData[i] = array.get(i);
        }

        return newData;
    }

    /**
     * Возвращает строковое представление дерева.
     */
    @Override
    public String toString() {
        StringBuilder cd = new StringBuilder();
        if (size == 0) {
            return "[]";
        }
        Collection<T> array = new ArrayList<>();
        fillRecursive(root, array);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (T item : array) {
            sb.append(" " + item);
        }
        sb.append(" ]");

        return sb.toString();
    }

    /**
     * Заполняет массив объектами из множества.
     */
    private void fillRecursive(Node<T> node, Collection<T> array) {

        if (node.left != null) {
            fillRecursive(node.left, array);
        }
        array.add(node.item);
        if (node.right != null) {
            fillRecursive(node.right, array);
        }
    }

}
