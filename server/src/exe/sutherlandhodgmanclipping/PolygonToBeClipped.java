
package exe.sutherlandhodgmanclipping;

import java.lang.*;
import java.util.*;
import exe.*;

public class PolygonToBeClipped {
  protected ArrayList<Double> oldx;
  protected ArrayList<Double> oldy;
  protected ArrayList<Double> xvalues;
  protected ArrayList<Double> yvalues;
  protected static ArrayList<String> labels;
  protected ArrayList<String> colors;
  
  protected String color;
  
  protected int s;
  protected int p;
  
  public PolygonToBeClipped(ArrayList<Point> vertices, String color) {
    oldx = new ArrayList<Double>();
    oldy = new ArrayList<Double>();
    
    xvalues = new ArrayList<Double>();
    yvalues = new ArrayList<Double>();
    
    colors = new ArrayList<String>();
    labels = new ArrayList<String>();
    
    this.color = color;
        
    for(int i = 0; i < vertices.size(); ++i) {
      oldx.add(vertices.get(i).getX());
      oldy.add(vertices.get(i).getY());
      
      xvalues.add(vertices.get(i).getX());
      yvalues.add(vertices.get(i).getY());
      
      colors.add(color);
      labels.add("V" + i);
    }

  }
  
  public void setColor(String color) {
    this.color = color;
    for(int i = 0; i < colors.size(); ++i) {
      colors.set(i, color);
    }
  }
  
  public int getStartIndex() {
    return s;
  }
  
  public int getEndIndex() {
    return p;
  }
  
  public void setStartIndex(int s) {
    clear();
    this.s = s;
    colors.set(s, "#999900");
  }
  
  public void setEndIndex(int p) {
    this.p = p;
    colors.set(p, "#009999");

  }
  
  public void setStartText(String text) {
    labels.set(s, text);
    colors.set(s, "#999900");
  }
  
  public void setEndText(String text) {
    labels.set(p, text);
    colors.set(p, "#009999");
  }
  
  public void addToPrimitiveCollection(GAIGSprimitiveCollection pc) {
    double [] xvals = new double[xvalues.size()];
    double [] yvals = new double[yvalues.size()];
    for(int i = 0; i < xvalues.size(); ++i) {
      xvals[i] = ((Double)(xvalues.toArray()[i])).doubleValue();
      yvals[i] = ((Double)(yvalues.toArray()[i])).doubleValue();
    }
    pc.addPolygon(xvalues.size(), xvals, yvals, "#FFFFFF", color, "#000000", "");
    for(int i = 0; i < labels.size(); ++i) {
      pc.addCircle(xvalues.get(i).doubleValue(), yvalues.get(i).doubleValue(),
        .021, colors.get(i), colors.get(i), "#FFFFFF", labels.get(i));
    }
  }
  
  public void addStartAndEndToPC(GAIGSprimitiveCollection pc) {
    pc.addCircle(xvalues.get(s).doubleValue(), yvalues.get(s).doubleValue(),
      .021, colors.get(s), colors.get(s), "#FFFFFF", labels.get(s));
    pc.addCircle(xvalues.get(p).doubleValue(), yvalues.get(p).doubleValue(),
      .021, colors.get(p), colors.get(p), "#FFFFFF", labels.get(p));
  }
  
  public void addOriginalPolygon(GAIGSprimitiveCollection pc) {
    double [] xvals = new double[oldx.size()];
    double [] yvals = new double[oldy.size()];
    for(int i = 0; i < oldx.size(); ++i) {
      xvals[i] = ((Double)(oldx.toArray()[i])).doubleValue();
      yvals[i] = ((Double)(oldy.toArray()[i])).doubleValue();
    }
    pc.addPolygon(oldx.size(), xvals, yvals, "#FFFFFF", color, "#000000", "");  
  }
  
  public void fillToPrimitiveCollection(GAIGSprimitiveCollection pc, String fillColor) {
    double [] xvals = new double[xvalues.size()];
    double [] yvals = new double[yvalues.size()];
    for(int i = 0; i < xvalues.size(); ++i) {
      xvals[i] = ((Double)(xvalues.toArray()[i])).doubleValue();
      yvals[i] = ((Double)(yvalues.toArray()[i])).doubleValue();
    }
    pc.addPolygon(xvalues.size(), xvals, yvals, fillColor, color, "#000000", "");
  }
  
  public void update(ArrayList<Point> vertices) {
    xvalues.clear();
    yvalues.clear();
    colors.clear();
    labels.clear();
    
    for(int i = 0; i < vertices.size(); ++i) {
      xvalues.add(vertices.get(i).getX());
      yvalues.add(vertices.get(i).getY());
      colors.add(color);
      labels.add("V" + i);
    }
  }
  
  protected void clear() {
    for(int i = 0; i < labels.size(); ++i) {
      labels.set(i, "V"+i);
      colors.set(i, color);
    }
  }
  
  public ArrayList<Point> getVertices() {
    ArrayList<Point> ret = new ArrayList<Point>();
    for(int i = 0; i < xvalues.size(); ++i) {
      ret.add(new Point(xvalues.get(i), yvalues.get(i), 0));
    }
    return ret;
  }
  
  public static ArrayList<String> getLabels() {
    return labels;
  }
}