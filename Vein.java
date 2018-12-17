import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity
{
    private Random rand;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    public Vein(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
        this.rand = new Random();
    }

    private static String getOreIdPrefix()
    {
        return ORE_ID_PREFIX;
    }

    private static int getOreCorruptMin()
    {
        return ORE_CORRUPT_MIN;
    }

    private static int getOreCorruptMax()
    {
        return ORE_CORRUPT_MAX;
    }

    public void executeActivity(ActiveEntity entity, WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(entity.getPosition());

        if (openPt.isPresent())
        {
            Ore ore = new Ore(getOreIdPrefix() + getId(),
                    openPt.get(), getOreCorruptMin()
                            + rand.nextInt(getOreCorruptMax()
                                    - getOreCorruptMin()),
                    imageStore.getImageList(ImageStore.getOreKey()));
            world.addEntity(ore);
            ore.scheduleActions(world, imageStore, scheduler);
        }

        scheduler.scheduleEvent(entity,
                new Activity(entity, world, imageStore),
                entity.getActionPeriod());
    }

    public <Boolean> Boolean accept(EntityVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
