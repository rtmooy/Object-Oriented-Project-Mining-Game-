import processing.core.PImage;
import java.util.List;

public abstract class AnimateEntity extends ActiveEntity
{
    private int animationPeriod;

    public AnimateEntity(String id, Point position, int actionPeriod,
        int animationPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod()
    {
        return animationPeriod;
    }

    //Overrides version in ActiveEntity
    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler)
    {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore),
                getActionPeriod());
        eventScheduler.scheduleEvent(this, new Animation(this, 0),
                getAnimationPeriod());
    }
}
