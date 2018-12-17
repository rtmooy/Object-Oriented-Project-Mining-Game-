import processing.core.PImage;
import java.util.List;

public abstract class ActiveEntity extends WorldEntity
{
    private int actionPeriod;

    public ActiveEntity(String id, Point position, int actionPeriod,
        List<PImage> images)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod()
    {
        return actionPeriod;
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler)
    {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore),
                getActionPeriod());
    }

    public abstract void executeActivity(ActiveEntity entity, WorldModel world,
        ImageStore imageStore, EventScheduler scheduler);
}
