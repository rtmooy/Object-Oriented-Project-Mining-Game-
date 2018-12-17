final class Point
{
   private final int x;
   private final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public int distanceSquared(Point p2)
   {
      int deltaX = getX() - p2.getX();
      int deltaY = getY() - p2.getY();

      return deltaX * deltaX + deltaY * deltaY;
   }

   public boolean adjacent(Point p2)
   {
      return (getX() == p2.getX() && Math.abs(getY() - p2.getY()) == 1) ||
         (getY() == p2.getY() && Math.abs(getX() - p2.getX()) == 1);
   }

   public String toString()
   {
      return "(" + getX() + "," + getY() + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point) other).getX() == this.getX() &&
         ((Point) other).getY() == this.getY();
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + getX();
      result = result * 31 + getY();
      return result;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }
}
