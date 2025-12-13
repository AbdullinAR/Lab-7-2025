package functions;

public class FunctionPoint implements Cloneable {
  private double x;
  private double y;
  private double DEFAULT_EPSILON = 1e-9;
  
  public double getX() {
    return x;
  }
  public double getY() {
    return y;
  }

  public void setX(double source_x) { x = source_x;}
  public void setY(double source_y) { y = source_y;}
  
  public FunctionPoint() {
    x = 0;
    y = 0;
  }
  public FunctionPoint(double source_x, double source_y) {
    x = source_x;
    y = source_y;
  }
  public FunctionPoint(FunctionPoint point) {
    x = point.getX();
    y = point.getY();
  }

  public String toString() {
        return ("(" + x + "; " + y + ")");
  }
  public boolean equals(Object o) {
      if (this.getClass() != o.getClass()) return false;
      FunctionPoint pointO = (FunctionPoint) o;
      return (Math.abs(pointO.getX() - x) < DEFAULT_EPSILON && Math.abs(pointO.getY() - y) < DEFAULT_EPSILON);
  }
  public int hashCode() {
      long xBits = Double.doubleToLongBits(x);
      long yBits = Double.doubleToLongBits(y);
      int xHash = (int)(xBits ^ (xBits >>> 32));
      int yHash = (int)(yBits ^ (yBits >>> 32));
      return xHash ^ yHash;
  }
  public Object clone() {
      try {
          return super.clone();
      } catch (CloneNotSupportedException e) {
          throw new AssertionError(e);
      }
  }
}

