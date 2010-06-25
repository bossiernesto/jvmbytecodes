package exe.chuck_typeset;

public class Queue<E> {
    private Node<E> rear;
    private Node<E> front;
    private int size;
    
    public Queue() {
        size = 0;
        front = null;
        rear = null;
    }
    
    public boolean isEmpty() {
        if (this.size == 0)
            return true;
        return false;
    }
    
    public int size() {
        return size;
    }
    
    public void enqueue(E obj) {
        Node<E> n = new Node<E>(obj, null);
        size++;
        
        if (this.size == 1) {
            rear = n;
            front = rear;
        }
        else {
            rear.setLink(n);
            rear = n;
        }
    }
    
    public E atFront() {
        return front.getItem();
    }
    
    public E dequeue() throws NullPointerException {
        if (rear == null)
            throw new NullPointerException("dequeue called on empty queue");
        E itm = front.getItem();
        this.size--;
        front = front.getLink();
        if (this.size == 0)
            rear = null;
        return itm;
   }
        
}