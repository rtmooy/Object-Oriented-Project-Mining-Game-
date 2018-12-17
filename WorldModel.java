import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background[][] background;
   private WorldEntity[][] occupancy;
   private Set<WorldEntity> entities;
   private static final int ORE_REACH = 1;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new WorldEntity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.getBackground()[row], defaultBackground);
      }
   }

   private static Optional<WorldEntity> nearestEntity(List<WorldEntity> entities,
                                                 Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         WorldEntity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (WorldEntity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public void setBackground(Point pos,
                             Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
         && getOccupancyCell(pos) != null)
      {
         WorldEntity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         getEntities().remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public void removeEntity(WorldEntity entity)
   {
      this.removeEntityAt(entity.getPosition());
   }

   public void moveEntity(WorldEntity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   /*
            Assumes that there is no entity currently occupying the
            intended destination cell.
         */
   public void addEntity(WorldEntity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         getEntities().add(entity);
      }
   }

   public Optional<WorldEntity> findNearest(Point pos, String className)
   {
      List<WorldEntity> ofType = new LinkedList<>();
      for (WorldEntity entity : getEntities())
      {
         if (entity.getClass().getName().equals(className))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
         getOccupancyCell(pos) != null;
   }

   private boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < getNumRows() &&
         pos.getX() >= 0 && pos.getX() < getNumCols();
   }

   public void tryAddEntity(WorldEntity entity)
   {
      if (this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);
   }

   public Optional<WorldEntity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   private WorldEntity getOccupancyCell(Point pos)
   {
      return getOccupancy()[pos.getY()][pos.getX()];
   }

   private void setOccupancyCell(Point pos,
                                WorldEntity entity)
   {
      getOccupancy()[pos.getY()][pos.getX()] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return getBackground()[pos.getY()][pos.getX()];
   }

   private void setBackgroundCell(Point pos,
                                 Background background)
   {
      this.getBackground()[pos.getY()][pos.getX()] = background;
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
               !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   private Background[][] getBackground() {
      return background;
   }

   private WorldEntity[][] getOccupancy() {
      return occupancy;
   }

   public Set<WorldEntity> getEntities() {
      return entities;
   }
}
