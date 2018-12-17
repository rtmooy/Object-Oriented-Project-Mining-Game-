import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Ore extends ActiveEntity
{
    private Random rand;
    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    public Ore(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
        this.rand = new Random();
    }

    private String getBlobKey()
    {
        return BLOB_KEY;
    }

    private String getBlobIdSuffix()
    {
        return BLOB_ID_SUFFIX;
    }

    private int getBlobPeriodScale()
    {
        return BLOB_PERIOD_SCALE;
    }

    private int getBlobAnimationMin()
    {
        return BLOB_ANIMATION_MIN;
    }

    private int getBlobAnimationMax()
    {
        return BLOB_ANIMATION_MAX;
    }

    public void executeActivity(ActiveEntity entity, WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = entity.getPosition();  // store current position before removing

        world.removeEntity(entity);
        scheduler.unscheduleAllEvents(entity);

        OreBlob blob = new OreBlob(entity.getId() + getBlobIdSuffix(),
                pos, entity.getActionPeriod() / getBlobPeriodScale(),
                getBlobAnimationMin()
                    + rand.nextInt(getBlobAnimationMax()
                        - getBlobAnimationMin()),
                imageStore.getImageList(getBlobKey()));

        world.addEntity(blob);
        blob.scheduleActions(world, imageStore, scheduler);
    }

    public <Boolean> Boolean accept(EntityVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
