package ccm.placeableTools.client.renderCrap;

public class Triangle implements IShape
{
    final Point2D[] points = new Point2D[3];
    public Triangle(Point2D p1, Point2D p2, Point2D p3)
    {
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
    }

    @Override
    public void setPoint(int i, Point2D point)
    {
        points[i] = point;
    }

    @Override
    public Point2D[] getPoints()
    {
        return points;
    }
}
