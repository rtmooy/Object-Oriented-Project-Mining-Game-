import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy
{
    private Set<AStarNode> openSet = new HashSet<>();
    private Set<AStarNode> closedSet = new HashSet<>();
    private List<Point> path = new ArrayList<>();

    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        //Setup
        AStarNode current = new AStarNode(start, 0, manhattanDist(start, end));
        closedSet.add(current);
        for (Point neighbor : potentialNeighbors.apply(current.getPoint())
                .collect(Collectors.toList()))
        {
            if (canPassThrough.test(neighbor))
            {
                openSet.add(new AStarNode(neighbor, 1, manhattanDist(neighbor, end)));
            }
        }

        while (openSet.size() > 0 && openSet.size() < 750)
        {
            if (withinReach.test(current.getPoint(), end))
            {
                return path;
            }

            //Determine next step
            AStarNode next = null;
            for (AStarNode node : openSet)
            {
                if (next == null)
                {
                    next = node;
                }
                else if (node.getfScore() < next.getfScore())
                {
                    next = node;
                }
            }

            //Update next
            openSet.remove(next);
            closedSet.add(next);
            path.add(next.getPoint());
            current = next;

            //Update new neighbors
            for (Point neighbor : potentialNeighbors.apply(current.getPoint())
                    .collect(Collectors.toList()))
            {
                if (canPassThrough.test(current.getPoint()))
                {
                    openSet.add(new AStarNode(neighbor, manhattanDist(neighbor, start), manhattanDist(neighbor, end)));
                }
            }
        }
        //Failure, stay in place until next time
        path.clear();
        path.add(start);
        return path;
    }

    public int manhattanDist(Point p1, Point p2)
    {
        return Math.abs(p2.getX() - p1.getX()) + Math.abs(p2.getY() - p1.getY());
    }
}
