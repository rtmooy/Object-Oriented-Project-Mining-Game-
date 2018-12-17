public class Animation implements Action
{
    private AnimateEntity entity;
    private int repeatCount;

    public Animation(AnimateEntity entity, int repeatCount)
    {
        this.entity = entity;
        if (entity.accept(new QuakeVisitor()))
        {
            this.repeatCount = ((Quake) entity).getQuakeAnimationRepeatCount();
        }
        else
        {
            this.repeatCount = repeatCount;
        }
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity, new Animation(entity, Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
}