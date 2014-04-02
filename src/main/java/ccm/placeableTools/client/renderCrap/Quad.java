package ccm.placeableTools.client.renderCrap;

public class Quad implements IShape
{
    final Point2D[] points = new Point2D[4];
    public Quad(Point2D p1, Point2D p2, Point2D p3, Point2D p4)
    {
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
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
