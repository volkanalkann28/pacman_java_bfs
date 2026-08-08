public class Queue<T> {
    private T[] list;
    private int front;
    private int rear;
    private int count;
    private static final int INITIAL_CAPACITY = 100;

    @SuppressWarnings("unchecked")
    public Queue() {
        list = (T[]) new Object[INITIAL_CAPACITY];
        front = 0;
        rear = -1;
        count = 0;
    }

    public boolean isEmpty() { return count == 0; }
    public int size() { return count; }

    // add to queue and resize if needed
    public void enqueue(T item) {
        if (count == list.length) resize(2 * list.length);
        rear = (rear + 1) % list.length;
        list[rear] = item;
        count++;
    }

    public T dequeue() {
        if (isEmpty()) return null;
        T item = list[front];
        list[front] = null;
        front = (front + 1) % list.length;
        count--;
        return item;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        for (int i = 0; i < count; i++) {
            temp[i] = list[(front + i) % list.length];
        }
        list = temp;
        front = 0;
        rear = count - 1;
    }
}