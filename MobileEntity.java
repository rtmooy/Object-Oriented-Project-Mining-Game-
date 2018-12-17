import processing.core.PImage;
import java.util.List;

public abstract class MobileEntity extends AnimateEntity
{
    public MobileEntity(String id, Point position, int actionPeriod,
        int animationPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    public abstract boolean moveTo(MobileEntity entity, WorldModel world,
        WorldEntity target, EventScheduler scheduler);

    public abstract Point nextPosition(MobileEntity entity, WorldModel world,
        Point destPos);
}
