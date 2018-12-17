import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class OreBlob extends MobileEntity
{
    public OreBlob(String id, Point position, int actionPeriod,
            int animationPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    public void executeActivity(ActiveEntity entity, WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<WorldEntity> blobTarget = world.findNearest(
                entity.getPosition(), "Vein");
        long nextPeriod = entity.getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo((MobileEntity) entity, world, blobTarget.get(), scheduler))
            {
                Quake quake = new Quake(tgtPos,
                        imageStore.getImageList(ImageStore.getQuakeKey()));

                world.addEntity(quake);
                nextPeriod += entity.getActionPeriod();
                quake.scheduleActions(world, imageStore, scheduler);
            }
        }

        scheduler.scheduleEvent(entity,
                new Activity(entity, world, imageStore),
                nextPeriod);
    }

    public boolean moveTo(MobileEntity blob, WorldModel world,
            WorldEntity target, EventScheduler scheduler)
    {
        if (blob.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
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
            List<Point> path = s.computePath(blob.getPosition(), target.getPosition(), canPassThrough, withinReach, potentialNeighbors);

            if(path.size() > 0)
            {
                Point nextPos = path.get(0);
                if (!blob.getPosition().equals(nextPos)) {
                    Optional<WorldEntity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent()) {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }
                    world.moveEntity(blob, nextPos);
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

        Optional<WorldEntity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent()
                && !(occupant.get().accept(new OreVisitor()))))
        {
            int vert = Integer.signum(destPos.getY() - entity.getPosition().getY());
            newPos = new Point(entity.getPosition().getX(),
                    entity.getPosition().getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent()
                    && !(occupant.get().accept(new OreVisitor()))))
            {
                newPos = entity.getPosition();
            }
        }

        return newPos;
    }

    public <Boolean> Boolean accept(EntityVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
