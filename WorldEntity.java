import processing.core.PImage;
import java.util.List;

public abstract class WorldEntity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public WorldEntity(String id, Point position, List<PImage> images)
    {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
    }

    public String getId()
    {
      return id;
    }

    public Point getPosition()
    {
      return position;
    }

    public void setPosition(Point position)
    {
      this.position = position;
    }

    public List<PImage> getImages()
    {
      return images;
    }

    private int getImageIndex()
    {
      return imageIndex;
    }

    public PImage getCurrentImage()
    {
      return this.getImages().get(this.getImageIndex());
    }

    public void nextImage()
    {
      imageIndex = (getImageIndex() + 1) % getImages().size();
    }

    public abstract <R> R accept(EntityVisitor<R> visitor);
}
