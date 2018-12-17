public class AStarNode
{
    private Point point;
    private int gScore;
    private int fScore;

    public AStarNode(Point point)
    {
        this.point = point;
        this.gScore = 1000;
        this.fScore = 1000;
    }

    public AStarNode(Point point, int gScore, int fScore)
    {
        this.point = point;
        this.gScore = gScore;
        this.fScore = fScore;
    }

    public Point getPoint() {
        return point;
    }

    public int getgScore() {
        return gScore;
    }

    public int getfScore() {
        return fScore;
    }
}
