public class Activity implements Action
{
    private ActiveEntity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(ActiveEntity entity, WorldModel world,
                    ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(entity, world, imageStore, scheduler);
    }
}