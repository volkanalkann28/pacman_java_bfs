public class Stack<T> {
    private T[] list;
    private int count;
    private static final int INITIAL_CAPACITY = 100;

    @SuppressWarnings("unchecked")
    public Stack() {
        list = (T[]) new Object[INITIAL_CAPACITY];
        count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    // add item and grow array if full
    public void push(T item) {
        if (count == list.length) {
            resize(2 * list.length);
        }
        list[count++] = item;
    }

    public T pop() {
        if (isEmpty()) return null;
        T item = list[--count];
        list[count] = null;
        return item;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        for (int i = 0; i < count; i++) {
            temp[i] = list[i];
        }
        list = temp;
    }
}