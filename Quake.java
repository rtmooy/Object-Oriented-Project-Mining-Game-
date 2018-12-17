import processing.core.PImage;
import java.util.List;

public class Quake extends AnimateEntity
{
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images)
    {
        super(ImageStore.getQuakeId(), position, QUAKE_ACTION_PERIOD,
            QUAKE_ANIMATION_PERIOD, images);
    }

    public int getQuakeAnimationRepeatCount()
    {
        return QUAKE_ANIMATION_REPEAT_COUNT;
    }

    public void executeActivity(ActiveEntity entity, WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(entity);
        world.removeEntity(entity);
    }

    public <Boolean> Boolean accept(EntityVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
