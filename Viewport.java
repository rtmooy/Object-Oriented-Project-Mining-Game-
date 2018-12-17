final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

   public Point worldToViewport(int col, int row)
   {
      return new Point(col - getCol(), row - getRow());
   }

   public Point viewportToWorld(int col, int row)
   {
      return new Point(col + getCol(), row + getRow());
   }

   public boolean contains(Point p)
   {
      return p.getY() >= getRow() && p.getY() < getRow() + getNumRows() &&
         p.getX() >= getCol() && p.getX() < getCol() + getNumCols();
   }

   public void shift(int col, int row)
   {
      this.col = col;
      this.row = row;
   }

   public int getRow() {
      return row;
   }

   public int getCol() {
      return col;
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }
}
