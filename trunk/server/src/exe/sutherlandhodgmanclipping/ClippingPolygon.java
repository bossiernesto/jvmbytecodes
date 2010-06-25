package exe.sutherlandhodgmanclipping;

import java.lang.*;
import java.util.*;
import exe.*;

public class ClippingPolygon {
  protected ArrayList<Double> xvalues;
  protected ArrayList<Double> yvalues;
  protected ArrayList<Edge> edges;
  
  protected String color;
  protected String edgeColor;
  
  protected int edgeIndex;
  
  
  public ClippingPolygon(ArrayList<Point> vertices, String color, String edgeColor) {
    xvalues = new ArrayList<Double>();
    yvalues = new ArrayList<Double>();
    
    for(int i = 0; i < vertices.size(); ++i) {
      xvalues.add(vertices.get(i).getX());
      yvalues.add(vertices.get(i).getY());
    }
    
    this.color = color;
    this.edgeColor = edgeColor;
    this.edgeIndex = -1;
    
    computeEdges();
  }
  
  protected void computeEdges() {
    edges = new ArrayList<Edge>();
    
    for(int i = 0; i < xvalues.size(); ++i) {
      edges.add(new Edge(new Point(xvalues.get(i).doubleValue(), yvalues.get(i).doubleValue(), 0),
                         new Point(xvalues.get((i+1) % xvalues.size()).doubleValue(),
                                   yvalues.get((i+1) % yvalues.size()).doubleValue(), 0)));
    }
  }
  
  public ArrayList<Edge> getEdges() {
    return edges;
  }
  
  public void setEdgeIndex(int index) {
    edgeIndex = index;
  }
  
  public Edge getCurrentEdge() {
    if(edgeIndex == -1) {
      return null;
    }
    return edges.get(edgeIndex);
  }
  
  public void setColor(String color) {
    this.color = color;
  }
  
  public void setEdgeColor(String edgeColor) {
    this.edgeColor = edgeColor;
  }
  
  public void addToPrimitiveCollection(GAIGSprimitiveCollection pc) {
    double [] xvals = new double[xvalues.size()];
    double [] yvals = new double[yvalues.size()];
    for(int i = 0; i < xvalues.size(); ++i) {
      xvals[i] = ((Double)(xvalues.toArray()[i])).doubleValue();
      yvals[i] = ((Double)(yvalues.toArray()[i])).doubleValue();
    }
    pc.addPolygon(xvalues.size(), xvals, yvals, "#FFFFFF", color, "#000000", "");
    
    if(edgeIndex >= 0 && edgeIndex < xvalues.size()) {
      double [] edgex = { edges.get(edgeIndex).getP1().getX(), edges.get(edgeIndex).getP2().getX() };
      double [] edgey = { edges.get(edgeIndex).getP1().getY(), edges.get(edgeIndex).getP2().getY() };
      double xval = (edgex[1] - edgex[0]);
      if(xval != 0) {
        double m = (edgey[1] - edgey[0]) / xval;
        double b = edgey[1] - m * edgex[1];
        double [] x = {0, 1};
        double [] y = {b, m + b};
        pc.addLine(x, y, edgeColor, "#000000", "");
      } else {
        double [] x = {edgex[0], edgex[0]};
        double [] y = {0, 1};
        pc.addLine(x, y, edgeColor, "#000000", "");
      }
    }
  }
}