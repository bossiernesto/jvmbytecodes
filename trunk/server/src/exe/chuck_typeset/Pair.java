package exe.chuck_typeset;


public class Pair {
    private int x;
    private int y;
    /** Creates a new instance of Pair */
    public Pair() {
  
    }
    
    public Pair(int x,int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String toString() {
        return "("+x+","+y+")";
    }
}