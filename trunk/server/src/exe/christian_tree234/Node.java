/* 
This file is part of JHAVE -- Java Hosted Algorithm Visualization
Environment, developed by Tom Naps, David Furcy (both of the
University of Wisconsin - Oshkosh), Myles McNally (Alma College), and
numerous other contributors who are listed at the http://jhave.org
site

JHAVE is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your
option) any later version.

JHAVE is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with the JHAVE. If not, see:
<http://www.gnu.org/licenses/>.
*/

package exe.christian_tree234;

class Node
{

    Node()
    {
        childArray = new Node[4];
        itemArray = new DataItem[3];
    }

    public void connectChild(int i, Node node)
    {
        childArray[i] = node;
        if(node != null)
            node.parent = this;
    }

    public Node disconnectChild(int i)
    {
        Node node = childArray[i];
        childArray[i] = null;
        return node;
    }

    public Node getChild(int i)
    {
        return childArray[i];
    }

    public Node getParent()
    {
        return parent;
    }

    public boolean isLeaf()
    {
        return childArray[0] == null;
    }

    public int getNumItems()
    {
        return numItems;
    }

    public DataItem getItem(int i)
    {
        return itemArray[i];
    }

    public boolean isFull()
    {
        return numItems == 3;
    }

    public int findItem(long l)
    {
        for(int i = 0; i < 3; i++)
        {
            if(itemArray[i] == null)
                break;
            if(itemArray[i].dData == l)
                return i;
        }

        return -1;
    }

    public int insertItem(DataItem dataitem)
    {
        numItems++;
        long l = dataitem.dData;
        for(int i = 2; i >= 0; i--)
            if(itemArray[i] != null)
            {
                long l1 = itemArray[i].dData;
                if(l < l1)
                {
                    itemArray[i + 1] = itemArray[i];
                } else
                {
                    itemArray[i + 1] = dataitem;
                    return i + 1;
                }
            }

        itemArray[0] = dataitem;
        return 0;
    }

    public DataItem removeItem()
    {
        DataItem dataitem = itemArray[numItems - 1];
        itemArray[numItems - 1] = null;
        numItems--;
        return dataitem;
    }

    public String displayNode()
    {
    	String strTreeLabel = "";
        for(int i = 0; i < numItems; i++) {
        	strTreeLabel = strTreeLabel +" "+ itemArray[i].displayItem();
        }
        // System.out.println("/");
        return strTreeLabel;
    }

    private static final int ORDER = 4;
    private int numItems;
    private Node parent;
    private Node childArray[];
    private DataItem itemArray[];
}
