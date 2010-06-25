package exe.Loops;

public class Operations
{
    
    public String string;

    /**
     * Constructor for objects of class RelOp
     */
    public Operations(String s)
    {
        string  = s;
    }
    
    /**
     * Performs a mathematical operation on the two parameters
     * and returns the answer
     */
    public int compute(int op1, int op2) //throws UnknownOperatorException
    {
        if (string.equals( "+" ))
            return (op1 + op2 );
        else if (string.equals( "-" ))
            return (op1 - op2);
        else if (string.equals( "*" ))
            return (op1 * op2);
        else if (string.equals( "/" ))
            return (op1 / op2);
        else if (string.equals( "%" ))
            return (op1 % op2);
        else return (op1 + op2); //throw new UnknownOperatorException( string );
    }
    
    /**
     * Compare the two parameters and returns a boolean
     */
    public boolean testing(int op1, int op2) //throws UnknownOperatorException
    {
        if (string.equals( "<" ))
            return (op1 < op2 );
        else if (string.equals( ">" ))
            return (op1 > op2);
        else if (string.equals( ">=" ))
            return (op1 >= op2);
        else if (string.equals( "<=" ))
            return (op1 <= op2);
        else if (string.equals( "==" ))
            return (op1 == op2);
        else if (string.equals( "!=" ))
            return (op1 != op2);
        else return (op1 != op2); //throw new UnknownOperatorException( string );
    }
    public int conversion()
    {
        if (string.equals("+1"))
            return 1;
        else if (string.equals("-1"))
            return -1;
        else
            return 0;
    }
    
    
}
