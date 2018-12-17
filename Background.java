import java.util.List;
import processing.core.PImage;

final class Background
{
   private String id;
   private List<PImage> images;
   private int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.setId(id);
      this.setImages(images);
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public List<PImage> getImages() {
      return images;
   }

   public void setImages(List<PImage> images) {
      this.images = images;
   }

   private int getImageIndex() {
      return imageIndex;
   }

   public PImage getCurrentImage()
   {
       return this.getImages().get(this.getImageIndex());
   }
}
