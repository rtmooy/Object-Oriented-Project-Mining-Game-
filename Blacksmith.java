import processing.core.PImage;

import java.util.List;

public class Blacksmith extends WorldEntity
{
    public Blacksmith(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public <Boolean> Boolean accept(EntityVisitor<Boolean> visitor) {
            return visitor.visit(this);
    }
}
