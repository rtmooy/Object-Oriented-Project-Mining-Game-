import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MinerFull extends MinerEntity
{
    public MinerFull(String id, int resourceLimit, Point position,
            int actionPeriod, int animationPeriod, List<PImage> images)
    {
      super(id, resourceLimit, position, actionPeriod, animationPeriod, images);
    }

    public void executeActivity(ActiveEntity entity, WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<WorldEntity> fullTarget = world.findNearest(entity.getPosition(),
                "Blacksmith");

        if (fullTarget.isPresent() && moveTo((MobileEntity) entity, world,
            fullTarget.get(), scheduler))
        {
            transform((MinerEntity) entity, world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(entity,
                new Activity(entity, world, imageStore),
                entity.getActionPeriod());
        }
    }

    public boolean moveTo(MobileEntity miner, WorldModel world,
            WorldEntity target, EventScheduler scheduler)
    {
        if (miner.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Predicate<Point> canPassThrough = (Point p) -> !(world.isOccupied(p));

            BiPredicate<Point, Point> withinReach = (p1,p2) -> (
                    ((p1.getX() == p2.getX())
                            && ((p1.getY() == p2.getY()-1) || (p1.getY() == p2.getY()+1)))
                            || ((p1.getY() == p2.getY())
                                && ((p1.getX() == p2.getX()-1) || (p1.getX() == p2.getX()+1))));
            Function<Point, Stream<Point>> potentialNeighbors = PathingStrategy.CARDINAL_NEIGHBORS;
            PathingStrategy s = new AStarPathingStrategy();
            List<Point> path = s.computePath(miner.getPosition(), target.getPosition(), canPassThrough,
                    withinReach, potentialNeighbors);

            if(path.size() > 0)
            {
                Point nextPos = path.get(0);
                if (!miner.getPosition().equals(nextPos)) {
                    Optional<WorldEntity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent()) {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }
                    world.moveEntity(miner, nextPos);
                }
            }
            return false;
        }
    }

    public Point nextPosition(MobileEntity entity, WorldModel world,
            Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - entity.getPosition().getX());
        Point newPos = new Point(entity.getPosition().getX() + horiz,
                entity.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY()
                       - entity.getPosition().getY());
            newPos = new Point(entity.getPosition().getX(),
                    entity.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = entity.getPosition();
            }
        }

        return newPos;
    }

    public boolean transform(MinerEntity entity, WorldModel world,
            EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = new MinerNotFull(entity.getId(),
                entity.getResourceLimit(), entity.getPosition(),
                entity.getActionPeriod(), entity.getAnimationPeriod(),
                entity.getImages());

        world.removeEntity(entity);
        scheduler.unscheduleAllEvents(entity);

        world.addEntity(miner);
        miner.scheduleActions(world, imageStore, scheduler);

        return true;
    }

    public <Boolean> Boolean accept(EntityVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
