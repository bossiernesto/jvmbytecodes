package exe.chuck_typeset;

public class Node<E> {
    private E item;
    private Node<E> link;
    
    public Node(E itm, Node<E> l){
        item = itm;
        link = l;
    }
    
    public Node(E itm) {
        item = itm;
        link = null;
    }
    
    public Node<E> getLink() {
        return link;
    }
    
    public E getItem() {
        return item;
    }
    
    public void setLink(Node<E> l) {
        link = l;
    }
    
    public void setItem(E itm) {
        item = itm;
    }

    
}