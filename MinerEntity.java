import processing.core.PImage;
import java.util.List;

public abstract class MinerEntity extends MobileEntity
{
    private int resourceLimit;
    private int resourceCount;

    public MinerEntity(String id, int resourceLimit, Point position,
        int actionPeriod, int animationPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
        this.resourceLimit = resourceLimit;
        this.resourceCount = 0;
    }

    public int getResourceLimit()
    {
      return resourceLimit;
    }

    public int getResourceCount()
    {
      return resourceCount;
    }

    public void setResourceCount(int count)
    {
      resourceCount = count;
    }

    public abstract boolean transform(MinerEntity entity, WorldModel world,
        EventScheduler scheduler, ImageStore imageStore);
}
